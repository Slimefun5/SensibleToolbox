package io.github.thebusybiscuit.sensibletoolbox.items.itemroutermodules;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class BlankModule extends BaseSTBItem {

    public BlankModule() {}

    public BlankModule(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.PAPER);
    }

    @Override
    public String getItemName() {
        return "Blank Item Router Module";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Used for crafting active", " Item Router Modules " };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack(8));
        recipe.shape("PPP", "PRP", "PBP");
        recipe.setIngredient('P', MaterialCompat.safe(XMaterial.PAPER));
        recipe.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        recipe.setIngredient('B', MaterialCompat.safe(XMaterial.LAPIS_LAZULI));
        return recipe;
    }
}

