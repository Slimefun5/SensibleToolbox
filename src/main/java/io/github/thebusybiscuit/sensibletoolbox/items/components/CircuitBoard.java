package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class CircuitBoard extends BaseSTBItem {

    public CircuitBoard() {}

    public CircuitBoard(ConfigurationSection conf) {}

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GREEN_CARPET);
    }

    @Override
    public String getItemName() {
        return "Circuit Board";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Used in the construction", "of electronic circuits" };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapelessRecipe recipe = RecipeCompat.shapeless(getKey(), toItemStack(2));
        recipe.addIngredient(MaterialCompat.safe(XMaterial.STONE_PRESSURE_PLATE));
        recipe.addIngredient(MaterialCompat.safe(XMaterial.GREEN_DYE));
        return recipe;
    }
}

