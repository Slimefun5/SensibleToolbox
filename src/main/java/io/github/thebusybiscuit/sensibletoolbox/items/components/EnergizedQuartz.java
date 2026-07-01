package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class EnergizedQuartz extends BaseSTBItem {

    public EnergizedQuartz() {}

    public EnergizedQuartz(ConfigurationSection conf) {}

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.QUARTZ);
    }

    @Override
    public String getItemName() {
        return "Energized Quartz";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Strangely glowing..." };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapelessRecipe recipe = RecipeCompat.shapeless(getKey(), toItemStack(1));
        InfernalDust dust = new InfernalDust();
        registerCustomIngredients(dust);
        recipe.addIngredient(dust.getMaterial());
        recipe.addIngredient(MaterialCompat.safe(XMaterial.QUARTZ));
        return recipe;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }
}

