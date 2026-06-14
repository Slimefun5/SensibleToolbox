package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class SimpleCircuit extends BaseSTBItem {

    public SimpleCircuit() {}

    public SimpleCircuit(ConfigurationSection conf) {}

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.REPEATER);
    }

    @Override
    public String getItemName() {
        return "Simple Electronic Circuit";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Used as a component in", "various machinery " };
    }

    @Override
    public Recipe getMainRecipe() {
        CircuitBoard cb = new CircuitBoard();
        registerCustomIngredients(cb);
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack(2));
        recipe.shape("CDC", "GTG", "CGC");
        recipe.setIngredient('C', cb.getMaterial());
        recipe.setIngredient('D', MaterialCompat.safe(XMaterial.REPEATER));
        recipe.setIngredient('T', MaterialCompat.safe(XMaterial.REDSTONE_TORCH));
        recipe.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_NUGGET));
        return recipe;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }
}

