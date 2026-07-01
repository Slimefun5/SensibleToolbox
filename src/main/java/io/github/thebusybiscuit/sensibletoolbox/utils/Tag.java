package io.github.thebusybiscuit.sensibletoolbox.utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.BukkitKeys;

/**
 * Java-8 universal port: drop-in replacement for {@code org.bukkit.Tag} (1.13+).
 * <p>
 * {@code org.bukkit.Tag} is declared {@code Tag<T extends Keyed>} and absent on 1.8, so a direct
 * reference fails class-loading there. The constants Slimefun's core already ships are delegated to
 * {@link io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag}; the few STB-only tags absent from
 * core ({@code WALL_SIGNS}, {@code FLOWERS}, {@code WOOL}, {@code CARPETS}) are resolved here against
 * the server's real tag reflectively, with a name-based {@link Material} fallback on legacy versions.
 * <p>
 * Call sites only need to swap {@code import org.bukkit.Tag} for this class; {@code Tag.X.isTagged(...)}
 * and {@code Tag.X.getValues()} read identically.
 */
public interface Tag<T> {

    Set<T> getValues();

    boolean isTagged(T value);

    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> LOGS = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.LOGS;
    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> PLANKS = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.PLANKS;
    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> LEAVES = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.LEAVES;
    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> SAPLINGS = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.SAPLINGS;
    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> SMALL_FLOWERS = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.SMALL_FLOWERS;
    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> WOODEN_SLABS = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.WOODEN_SLABS;
    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> STANDING_SIGNS = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.STANDING_SIGNS;
    io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag<Material> SIGNS = io.github.thebusybiscuit.slimefun5.utils.compatibility.Tag.SIGNS;

    Tag<Material> WALL_SIGNS = new VanillaTag("blocks", "wall_signs", "WALL_SIGN", "OAK_WALL_SIGN", "SPRUCE_WALL_SIGN", "BIRCH_WALL_SIGN", "JUNGLE_WALL_SIGN", "ACACIA_WALL_SIGN", "DARK_OAK_WALL_SIGN");
    Tag<Material> FLOWERS = new VanillaTag("blocks", "flowers", "DANDELION", "POPPY", "RED_ROSE", "YELLOW_FLOWER", "BLUE_ORCHID", "ALLIUM", "AZURE_BLUET", "RED_TULIP", "ORANGE_TULIP", "WHITE_TULIP", "PINK_TULIP", "OXEYE_DAISY", "SUNFLOWER", "LILAC", "ROSE_BUSH", "PEONY", "DOUBLE_PLANT");
    Tag<Material> WOOL = new VanillaTag("blocks", "wool", "WOOL", "WHITE_WOOL", "ORANGE_WOOL", "MAGENTA_WOOL", "LIGHT_BLUE_WOOL", "YELLOW_WOOL", "LIME_WOOL", "PINK_WOOL", "GRAY_WOOL", "LIGHT_GRAY_WOOL", "CYAN_WOOL", "PURPLE_WOOL", "BLUE_WOOL", "BROWN_WOOL", "GREEN_WOOL", "RED_WOOL", "BLACK_WOOL");
    Tag<Material> CARPETS = new VanillaTag("blocks", "carpets", "CARPET", "WHITE_CARPET", "ORANGE_CARPET", "MAGENTA_CARPET", "LIGHT_BLUE_CARPET", "YELLOW_CARPET", "LIME_CARPET", "PINK_CARPET", "GRAY_CARPET", "LIGHT_GRAY_CARPET", "CYAN_CARPET", "PURPLE_CARPET", "BLUE_CARPET", "BROWN_CARPET", "GREEN_CARPET", "RED_CARPET", "BLACK_CARPET");
}

/**
 * A {@link Tag} backed by the server's real {@code org.bukkit.Tag} (resolved reflectively and cached),
 * with a name-based {@link Material} fallback on legacy versions without the tag system.
 */
class VanillaTag implements Tag<Material> {

    private final String registry;
    private final String key;
    private final String[] fallbackNames;
    private volatile Set<Material> cached;

    VanillaTag(String registry, String key, String... fallbackNames) {
        this.registry = registry;
        this.key = key;
        this.fallbackNames = fallbackNames;
    }

    @Override
    public Set<Material> getValues() {
        Set<Material> result = cached;

        if (result == null) {
            Set<Material> resolved = resolveVanilla();

            if (resolved == null || resolved.isEmpty()) {
                resolved = resolveFallback();
            }

            result = Collections.unmodifiableSet(resolved);
            cached = result;
        }

        return result;
    }

    @Override
    public boolean isTagged(Material value) {
        return getValues().contains(value);
    }

    private Set<Material> resolveFallback() {
        Set<Material> set = new HashSet<>();

        for (String name : fallbackNames) {
            Material material = Material.matchMaterial(name);

            if (material != null) {
                set.add(material);
            }
        }

        return set;
    }

    private Set<Material> resolveVanilla() {
        try {
            Class<?> bukkitKeyClass = Class.forName("org.bukkit.NamespacedKey");
            Object namespacedKey = BukkitKeys.toBukkit(NamespacedKey.minecraft(key));
            Method getTag = Bukkit.class.getMethod("getTag", String.class, bukkitKeyClass, Class.class);
            Object tag = getTag.invoke(null, registry, namespacedKey, Material.class);

            if (tag == null) {
                return null;
            }

            Object values = tag.getClass().getMethod("getValues").invoke(tag);

            if (values instanceof Set) {
                Set<Material> result = new HashSet<>();

                for (Object element : (Set<?>) values) {
                    if (element instanceof Material) {
                        result.add((Material) element);
                    }
                }

                return result;
            }

            return null;
        } catch (Throwable e) {
            return null;
        }
    }
}
