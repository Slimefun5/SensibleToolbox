package io.github.thebusybiscuit.sensibletoolbox.items;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class GoldCombineHoe extends CombineHoe {

    public GoldCombineHoe() {
        super();
    }

    public GoldCombineHoe(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GOLDEN_HOE);
    }

    @Override
    public String getItemName() {
        return "Gold Combine Hoe";
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("SSS", "HCW", "SSS");
        recipe.setIngredient('S', MaterialCompat.safe(XMaterial.STRING));
        recipe.setIngredient('H', MaterialCompat.safe(XMaterial.GOLDEN_HOE));
        recipe.setIngredient('C', MaterialCompat.safe(XMaterial.CHEST));
        recipe.setIngredient('W', MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        return recipe;
    }
}

