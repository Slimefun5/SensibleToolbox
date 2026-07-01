package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class QuartzDust extends BaseSTBItem {

    public QuartzDust() {}

    public QuartzDust(ConfigurationSection conf) {}

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.SUGAR);
    }

    @Override
    public String getItemName() {
        return "Quartz Dust";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Cook in a Smelter to", "make silicon" };
    }

    @Override
    public Recipe getMainRecipe() {
        // no vanilla recipe - made in a masher
        return null;
    }

    @Override
    public ItemStack getSmeltingResult() {
        return new SiliconWafer().toItemStack();
    }
}

