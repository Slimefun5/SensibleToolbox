package io.github.thebusybiscuit.sensibletoolbox.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/** Reflective helpers for the 1.9+ off-hand API (getHand/getItemInOffHand/EquipmentSlot), so handlers run on 1.8. */
public final class HandCompat {

    private HandCompat() {}

    /** True if the interaction used the main hand. Always true before 1.9 (no off-hand exists). */
    public static boolean isMainHand(Object event) {
        try {
            Method getHand = event.getClass().getMethod("getHand");
            Object slot = getHand.invoke(event);
            return slot == null || !"OFF_HAND".equals(slot.toString());
        } catch (ReflectiveOperationException e) {
            return true;
        }
    }

    /** The off-hand item, or AIR on versions that have no off-hand. */
    public static ItemStack offHandItem(PlayerInventory inv) {
        try {
            Method m = inv.getClass().getMethod("getItemInOffHand");
            return (ItemStack) m.invoke(inv);
        } catch (ReflectiveOperationException e) {
            return new ItemStack(Material.AIR);
        }
    }

    /** Sets the off-hand item reflectively; no-op on versions that have no off-hand. */
    public static void setOffHandItem(PlayerInventory inv, ItemStack item) {
        try {
            Method m = inv.getClass().getMethod("setItemInOffHand", ItemStack.class);
            m.invoke(inv, item);
        } catch (ReflectiveOperationException e) {
            // no off-hand on this version; nothing to do
        }
    }

    /**
     * Builds a {@link BlockPlaceEvent} using the 1.9+ constructor (with the {@code EquipmentSlot hand}
     * obtained reflectively from the triggering interaction) when available, falling back to the 1.8
     * constructor that has no hand parameter.
     */
    public static BlockPlaceEvent newBlockPlaceEvent(Block placedBlock, BlockState replacedBlockState, Block placedAgainst, ItemStack itemInHand, Player player, boolean canBuild, Object sourceEvent) {
        for (Constructor<?> c : BlockPlaceEvent.class.getConstructors()) {
            Class<?>[] params = c.getParameterTypes();

            if (params.length == 7 && "EquipmentSlot".equals(params[6].getSimpleName())) {
                try {
                    Object hand = sourceEvent.getClass().getMethod("getHand").invoke(sourceEvent);
                    return (BlockPlaceEvent) c.newInstance(placedBlock, replacedBlockState, placedAgainst, itemInHand, player, canBuild, hand);
                } catch (ReflectiveOperationException e) {
                    // fall through to the legacy constructor
                }
            }
        }

        try {
            Constructor<BlockPlaceEvent> legacy = BlockPlaceEvent.class.getConstructor(Block.class, BlockState.class, Block.class, ItemStack.class, Player.class, boolean.class);
            return legacy.newInstance(placedBlock, replacedBlockState, placedAgainst, itemInHand, player, canBuild);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No supported BlockPlaceEvent constructor available", e);
        }
    }
}
