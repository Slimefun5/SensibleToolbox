package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class MachineFrame extends BaseSTBItem {

    public MachineFrame() {}

    public MachineFrame(ConfigurationSection conf) {}

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.IRON_BLOCK);
    }

    @Override
    public String getItemName() {
        return "Machine Frame";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Used in fabrication of", "various machines." };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("IBI", "B B", "IBI");
        recipe.setIngredient('B', MaterialCompat.safe(XMaterial.IRON_BARS));
        recipe.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_INGOT));
        return recipe;
    }

}

