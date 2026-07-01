package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class EnergizedGoldDust extends BaseSTBItem {

    public EnergizedGoldDust() {}

    public EnergizedGoldDust(ConfigurationSection conf) {}

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GLOWSTONE_DUST);
    }

    @Override
    public String getItemName() {
        return "Energized Gold Dust";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Smelt to get an energized gold ingot" };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapelessRecipe recipe = RecipeCompat.shapeless(getKey(), toItemStack(1));
        InfernalDust dust1 = new InfernalDust();
        GoldDust dust2 = new GoldDust();
        registerCustomIngredients(dust1, dust2);
        recipe.addIngredient(dust1.getMaterial());
        recipe.addIngredient(dust2.getMaterial());
        return recipe;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }

    @Override
    public ItemStack getSmeltingResult() {
        return new EnergizedGoldIngot().toItemStack();
    }
}

