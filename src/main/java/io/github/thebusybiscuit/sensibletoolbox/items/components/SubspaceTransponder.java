package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class SubspaceTransponder extends BaseSTBItem {

    public SubspaceTransponder() {
        super();
    }

    public SubspaceTransponder(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.BREWING_STAND);
    }

    @Override
    public String getItemName() {
        return "Subspace Transponder";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Used by some advanced", "items for cross-world", "communication" };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        IntegratedCircuit ic = new IntegratedCircuit();
        EnergizedGoldIngot eg = new EnergizedGoldIngot();
        registerCustomIngredients(ic, eg);
        recipe.shape("DGE", " G ", " C ");
        recipe.setIngredient('D', MaterialCompat.safe(XMaterial.DIAMOND));
        recipe.setIngredient('E', MaterialCompat.safe(XMaterial.ENDER_EYE));
        recipe.setIngredient('G', eg.getMaterial());
        recipe.setIngredient('C', ic.getMaterial());
        return recipe;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }
}

