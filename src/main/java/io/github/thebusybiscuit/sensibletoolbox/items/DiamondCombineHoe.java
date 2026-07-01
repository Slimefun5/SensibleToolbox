package io.github.thebusybiscuit.sensibletoolbox.items;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class DiamondCombineHoe extends CombineHoe {

    public DiamondCombineHoe() {
        super();
    }

    public DiamondCombineHoe(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.DIAMOND_HOE);
    }

    @Override
    public String getItemName() {
        return "Diamond Combine Hoe";
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("SSS", "HCW", "SSS");
        recipe.setIngredient('S', MaterialCompat.safe(XMaterial.STRING));
        recipe.setIngredient('H', MaterialCompat.safe(XMaterial.DIAMOND_HOE));
        recipe.setIngredient('C', MaterialCompat.safe(XMaterial.CHEST));
        recipe.setIngredient('W', MaterialCompat.safe(XMaterial.DIAMOND_SWORD));
        return recipe;
    }

    @Override
    public int getWorkRadius() {
        return 2;
    }
}

