package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import io.github.thebusybiscuit.sensibletoolbox.items.energycells.TenKEnergyCell;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;

public class TenKBatteryBox extends BatteryBox {

    public TenKBatteryBox() {}

    public TenKBatteryBox(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.RED_STAINED_GLASS);
    }

    @Override
    public String getItemName() {
        return "10K Battery Box";
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("GGG", "GCG", "RIR");
        TenKEnergyCell cell = new TenKEnergyCell();
        cell.setCharge(0.0);
        registerCustomIngredients(cell);
        recipe.setIngredient('G', MaterialCompat.safe(XMaterial.GLASS));
        recipe.setIngredient('C', cell.getMaterial());
        recipe.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        recipe.setIngredient('I', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        return recipe;
    }

    @Override
    public int getMaxCharge() {
        return 10000;
    }

    @Override
    public int getChargeRate() {
        return isRedstoneActive() ? 100 : 0;
    }
}

