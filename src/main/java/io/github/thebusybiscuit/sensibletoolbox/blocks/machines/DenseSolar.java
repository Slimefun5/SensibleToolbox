package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import io.github.thebusybiscuit.sensibletoolbox.items.components.IntegratedCircuit;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;

import javax.annotation.Nonnull;

public class DenseSolar extends BasicSolarCell {

    public DenseSolar() {}

    public DenseSolar(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GRAY_STAINED_GLASS);
    }

    @Override
    public String getItemName() {
        return "Dense Solar";
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        BasicSolarCell bs = new BasicSolarCell();
        IntegratedCircuit ic = new IntegratedCircuit();
        registerCustomIngredients(bs, ic);
        recipe.shape("SSS", "SIS", "SSS");
        recipe.setIngredient('S', bs.getMaterial());
        recipe.setIngredient('I', ic.getMaterial());
        return recipe;
    }

    @Override
    public int getMaxCharge() {
        return 800;
    }

    @Override
    public int getChargeRate() {
        return 12;
    }

    @Nonnull
    @Override
    protected DyeColor getCapColor() {
        return DyeColor.CYAN;
    }

    @Override
    protected double getPowerOutput() {
        return 4.0;
    }
}

