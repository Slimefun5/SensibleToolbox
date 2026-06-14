package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class EnergizedGoldIngot extends BaseSTBItem {

    public EnergizedGoldIngot() {}

    public EnergizedGoldIngot(ConfigurationSection conf) {

    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GOLD_INGOT);
    }

    @Override
    public String getItemName() {
        return "Energized Gold Ingot";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Strangely glowing..." };
    }

    @Override
    public Recipe getMainRecipe() {
        return null;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }
}

