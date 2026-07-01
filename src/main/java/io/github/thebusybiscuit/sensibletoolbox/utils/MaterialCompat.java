package io.github.thebusybiscuit.sensibletoolbox.utils;

import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

/**
 * Java-8 universal port: resolves an {@link XMaterial} to the running server's {@link Material},
 * guaranteeing a non-null result.
 * <p>
 * {@link XMaterial#parseMaterial()} may return {@code null} on legacy versions for a material with no
 * 1.8 equivalent; passing that to an {@link org.bukkit.inventory.ItemStack} constructor or
 * {@code setType(...)} would throw. {@link #safe(XMaterial)} substitutes {@link Material#STONE} in that
 * case so item creation never fails at runtime.
 */
public final class MaterialCompat {

    private MaterialCompat() {}

    public static Material safe(XMaterial material) {
        Material parsed = material.parseMaterial();
        return parsed != null ? parsed : Material.STONE;
    }
}
