package io.github.thebusybiscuit.sensibletoolbox.items;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class PaintRoller extends PaintBrush {

    public PaintRoller() {
        super();
    }

    public PaintRoller(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.IRON_SHOVEL);
    }

    @Override
    public String getItemName() {
        return "Paint Roller";
    }

    @Override
    public int getMaxPaintLevel() {
        return 100;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("WWW", "III", " S ");
        recipe.setIngredient('W', MaterialCompat.safe(XMaterial.WHITE_WOOL));
        recipe.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_INGOT));
        recipe.setIngredient('S', MaterialCompat.safe(XMaterial.STICK));
        return recipe;
    }

    @Override
    protected int getMaxBlocksAffected() {
        return 25;
    }
}

