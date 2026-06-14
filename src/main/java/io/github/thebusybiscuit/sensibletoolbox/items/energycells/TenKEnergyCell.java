package io.github.thebusybiscuit.sensibletoolbox.items.energycells;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.Tag;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class TenKEnergyCell extends EnergyCell {

    public TenKEnergyCell() {
        super();
    }

    public TenKEnergyCell(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public int getMaxCharge() {
        return 10000;
    }

    @Override
    public int getChargeRate() {
        return 100;
    }

    @Override
    public Color getCellColor() {
        return Color.MAROON;
    }

    @Override
    public String getItemName() {
        return "10K Energy Cell";
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("WWW", "WSW", "GRG");
        RecipeCompat.setIngredient(recipe, 'W', Tag.PLANKS.getValues());
        recipe.setIngredient('S', MaterialCompat.safe(XMaterial.SUGAR));
        recipe.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        recipe.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        return recipe;
    }

}

