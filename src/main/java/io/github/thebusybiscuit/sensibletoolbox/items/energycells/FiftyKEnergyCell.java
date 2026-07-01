package io.github.thebusybiscuit.sensibletoolbox.items.energycells;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.items.components.EnergizedIronIngot;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class FiftyKEnergyCell extends EnergyCell {

    public FiftyKEnergyCell() {
        super();
    }

    public FiftyKEnergyCell(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public int getMaxCharge() {
        return 50000;
    }

    @Override
    public int getChargeRate() {
        return 500;
    }

    @Override
    public Color getCellColor() {
        return Color.PURPLE;
    }

    @Override
    public String getItemName() {
        return "50K Energy Cell";
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        TenKEnergyCell cell = new TenKEnergyCell();
        cell.setCharge(0.0);
        EnergizedIronIngot ei = new EnergizedIronIngot();
        registerCustomIngredients(cell, ei);
        recipe.shape("III", "CCC", "GRG");
        recipe.setIngredient('I', ei.getMaterial());
        recipe.setIngredient('C', cell.getMaterial());
        recipe.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        recipe.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        return recipe;
    }
}

