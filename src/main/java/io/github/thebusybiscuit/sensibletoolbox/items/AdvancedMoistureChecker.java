package io.github.thebusybiscuit.sensibletoolbox.items;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class AdvancedMoistureChecker extends MoistureChecker {

    public AdvancedMoistureChecker() {}

    public AdvancedMoistureChecker(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public String getItemName() {
        return "Advanced Moisture Checker";
    }

    @Override
    public Recipe getMainRecipe() {
        MoistureChecker mc = new MoistureChecker();
        registerCustomIngredients(mc);
        ShapelessRecipe recipe = RecipeCompat.shapeless(getKey(), toItemStack());
        recipe.addIngredient(mc.getMaterial());
        recipe.addIngredient(MaterialCompat.safe(XMaterial.DIAMOND));
        return recipe;
    }

    @Override
    protected int getRadius() {
        return 2;
    }
}

