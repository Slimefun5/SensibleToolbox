package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class GoldDust extends BaseSTBItem {

    public GoldDust() {
        super();
    }

    public GoldDust(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GLOWSTONE_DUST);
    }

    @Override
    public String getItemName() {
        return "Gold Dust";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Smelt in a Smelter or Furnace", " to get gold ingots" };
    }

    @Override
    public Recipe getMainRecipe() {
        // Only made by the Masher
        return null;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }

    @Override
    public ItemStack getSmeltingResult() {
        return new ItemStack(MaterialCompat.safe(XMaterial.GOLD_INGOT));
    }
}

