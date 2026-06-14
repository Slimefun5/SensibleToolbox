package io.github.thebusybiscuit.sensibletoolbox.utils;

import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * Java-8 universal port: routes every {@code org.bukkit.block.data.*} operation through reflection so
 * those modern (MC 1.13+) types never appear in any other class's bytecode.
 * <p>
 * The JVM verifier eagerly loads every type named in a method body when the owning class links, even
 * inside branches that never run. Referencing {@code BlockData}/{@code Ageable}/{@code Farmland}/etc.
 * directly therefore throws {@code NoClassDefFoundError} at class-load on 1.8 regardless of any runtime
 * guard. Keeping all of those references confined to {@link Class#forName(String)} string literals here
 * is what lets the rest of the addon load on 1.8, where every operation degrades to a no-op/false.
 */
public final class BlockDataCompat {

    private BlockDataCompat() {}

    private static final boolean AVAILABLE;

    private static final Method GET_BLOCK_DATA;
    private static final Method SET_BLOCK_DATA;
    private static final Method MATERIAL_CREATE_BLOCK_DATA;

    private static final Class<?> AGEABLE;
    private static final Method AGEABLE_GET_AGE;
    private static final Method AGEABLE_GET_MAX_AGE;
    private static final Method AGEABLE_SET_AGE;

    private static final Class<?> FARMLAND;
    private static final Method FARMLAND_GET_MOISTURE;
    private static final Method FARMLAND_GET_MAX_MOISTURE;
    private static final Method FARMLAND_SET_MOISTURE;

    private static final Class<?> DIRECTIONAL;
    private static final Method DIRECTIONAL_GET_FACING;

    private static final Method PLAYER_SEND_BLOCK_CHANGE;

    static {
        boolean ok;

        Method getBlockData = null;
        Method setBlockData = null;
        Method materialCreateBlockData = null;
        Class<?> ageable = null;
        Method ageableGetAge = null;
        Method ageableGetMaxAge = null;
        Method ageableSetAge = null;
        Class<?> farmland = null;
        Method farmlandGetMoisture = null;
        Method farmlandGetMaxMoisture = null;
        Method farmlandSetMoisture = null;
        Class<?> directional = null;
        Method directionalGetFacing = null;
        Method playerSendBlockChange = null;

        try {
            Class<?> blockData = Class.forName("org.bukkit.block.data.BlockData");
            getBlockData = Block.class.getMethod("getBlockData");
            setBlockData = Block.class.getMethod("setBlockData", blockData, boolean.class);
            materialCreateBlockData = Material.class.getMethod("createBlockData");

            ageable = Class.forName("org.bukkit.block.data.Ageable");
            ageableGetAge = ageable.getMethod("getAge");
            ageableGetMaxAge = ageable.getMethod("getMaximumAge");
            ageableSetAge = ageable.getMethod("setAge", int.class);

            farmland = Class.forName("org.bukkit.block.data.type.Farmland");
            farmlandGetMoisture = farmland.getMethod("getMoisture");
            farmlandGetMaxMoisture = farmland.getMethod("getMaximumMoisture");
            farmlandSetMoisture = farmland.getMethod("setMoisture", int.class);

            directional = Class.forName("org.bukkit.block.data.Directional");
            directionalGetFacing = directional.getMethod("getFacing");

            playerSendBlockChange = Player.class.getMethod("sendBlockChange", Location.class, blockData);

            ok = true;
        } catch (Throwable t) {
            ok = false;
        }

        AVAILABLE = ok;
        GET_BLOCK_DATA = getBlockData;
        SET_BLOCK_DATA = setBlockData;
        MATERIAL_CREATE_BLOCK_DATA = materialCreateBlockData;
        AGEABLE = ageable;
        AGEABLE_GET_AGE = ageableGetAge;
        AGEABLE_GET_MAX_AGE = ageableGetMaxAge;
        AGEABLE_SET_AGE = ageableSetAge;
        FARMLAND = farmland;
        FARMLAND_GET_MOISTURE = farmlandGetMoisture;
        FARMLAND_GET_MAX_MOISTURE = farmlandGetMaxMoisture;
        FARMLAND_SET_MOISTURE = farmlandSetMoisture;
        DIRECTIONAL = directional;
        DIRECTIONAL_GET_FACING = directionalGetFacing;
        PLAYER_SEND_BLOCK_CHANGE = playerSendBlockChange;
    }

    public static boolean isModern() {
        return AVAILABLE;
    }

    private static Object getBlockData(Block block) {
        try {
            return GET_BLOCK_DATA.invoke(block);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private static void setBlockData(Block block, Object data) {
        try {
            SET_BLOCK_DATA.invoke(block, data, false);
        } catch (ReflectiveOperationException ignored) {
            // no-op on failure
        }
    }

    public static boolean isAgeable(Block block) {
        if (!AVAILABLE) {
            return false;
        }

        Object data = getBlockData(block);
        return data != null && AGEABLE.isInstance(data);
    }

    /**
     * @return true if the block is an {@code Ageable} crop that has reached its maximum age.
     */
    public static boolean isFullyGrown(Block block) {
        if (!AVAILABLE) {
            return false;
        }

        Object data = getBlockData(block);

        if (data == null || !AGEABLE.isInstance(data)) {
            return false;
        }

        try {
            int age = (int) AGEABLE_GET_AGE.invoke(data);
            int max = (int) AGEABLE_GET_MAX_AGE.invoke(data);
            return age >= max;
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    /**
     * Reset an {@code Ageable} crop to age 0. No-op if the block is not ageable or on legacy MC.
     */
    public static void resetAge(Block block) {
        setAge(block, 0);
    }

    private static void setAge(Block block, int age) {
        if (!AVAILABLE) {
            return;
        }

        Object data = getBlockData(block);

        if (data == null || !AGEABLE.isInstance(data)) {
            return;
        }

        try {
            AGEABLE_SET_AGE.invoke(data, age);
            setBlockData(block, data);
        } catch (ReflectiveOperationException ignored) {
            // no-op on failure
        }
    }

    /**
     * Advance an {@code Ageable} crop by one growth stage, capped at its maximum age. No-op on legacy MC.
     */
    public static void growByOne(Block block) {
        if (!AVAILABLE) {
            return;
        }

        Object data = getBlockData(block);

        if (data == null || !AGEABLE.isInstance(data)) {
            return;
        }

        try {
            int age = (int) AGEABLE_GET_AGE.invoke(data);
            int max = (int) AGEABLE_GET_MAX_AGE.invoke(data);

            if (age < max) {
                AGEABLE_SET_AGE.invoke(data, age + 1);
                setBlockData(block, data);
            }
        } catch (ReflectiveOperationException ignored) {
            // no-op on failure
        }
    }

    /**
     * Increase a {@code Farmland} block's moisture by one, capped at its maximum. No-op on legacy MC.
     */
    public static void increaseMoisture(Block block) {
        if (!AVAILABLE) {
            return;
        }

        Object data = getBlockData(block);

        if (data == null || !FARMLAND.isInstance(data)) {
            return;
        }

        try {
            int moisture = (int) FARMLAND_GET_MOISTURE.invoke(data);
            int max = (int) FARMLAND_GET_MAX_MOISTURE.invoke(data);

            if (moisture < max) {
                FARMLAND_SET_MOISTURE.invoke(data, moisture + 1);
                setBlockData(block, data);
            }
        } catch (ReflectiveOperationException ignored) {
            // no-op on failure
        }
    }

    /**
     * @return the facing of a {@code Directional} block, or null if not directional / on legacy MC.
     */
    public static BlockFace getFacing(Block block) {
        if (!AVAILABLE) {
            return null;
        }

        Object data = getBlockData(block);

        if (data == null || !DIRECTIONAL.isInstance(data)) {
            return null;
        }

        try {
            return (BlockFace) DIRECTIONAL_GET_FACING.invoke(data);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * Place the given (wall-sign) material at the block, oriented toward {@code face} when the material's
     * block data is {@code Directional}. Falls back to a plain {@code setType} on legacy MC.
     */
    public static void placeDirectional(Block block, Material material, BlockFace face) {
        if (!AVAILABLE) {
            block.setType(material);
            return;
        }

        try {
            Object data = MATERIAL_CREATE_BLOCK_DATA.invoke(material);

            if (DIRECTIONAL.isInstance(data)) {
                DIRECTIONAL.getMethod("setFacing", BlockFace.class).invoke(data, face);
            }

            block.setType(material);
            setBlockData(block, data);
        } catch (ReflectiveOperationException e) {
            block.setType(material);
        }
    }

    /**
     * Send a fake block change of the given material to a single player. No-op on legacy MC.
     */
    public static void sendBlockChange(Player player, Location location, Material material) {
        if (!AVAILABLE) {
            return;
        }

        try {
            Object data = MATERIAL_CREATE_BLOCK_DATA.invoke(material);
            PLAYER_SEND_BLOCK_CHANGE.invoke(player, location, data);
        } catch (ReflectiveOperationException ignored) {
            // no-op on failure
        }
    }

    /**
     * Send the block's own (real) block data to a player, undoing a previous fake change. No-op on legacy MC.
     */
    public static void restoreBlockChange(Player player, Block block) {
        if (!AVAILABLE) {
            return;
        }

        Object data = getBlockData(block);

        if (data == null) {
            return;
        }

        try {
            PLAYER_SEND_BLOCK_CHANGE.invoke(player, block.getLocation(), data);
        } catch (ReflectiveOperationException ignored) {
            // no-op on failure
        }
    }
}
