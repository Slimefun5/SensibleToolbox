package io.github.thebusybiscuit.sensibletoolbox.items.components;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.blocks.machines.FiftyKBatteryBox;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class UnlinkedSCURelay extends BaseSTBItem {

    public UnlinkedSCURelay() {
        super();
    }

    public UnlinkedSCURelay(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.CYAN_STAINED_GLASS);
    }

    @Override
    public String getItemName() {
        return "Unlinked SCU Relay";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Craft a pair of these", "together to link them" };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        FiftyKBatteryBox bb = new FiftyKBatteryBox();
        IntegratedCircuit ic = new IntegratedCircuit();
        EnergizedGoldIngot eg = new EnergizedGoldIngot();
        registerCustomIngredients(bb, ic, eg);
        recipe.shape("GCG", " E ", " B ");
        recipe.setIngredient('B', bb.getMaterial());
        recipe.setIngredient('C', ic.getMaterial());
        recipe.setIngredient('E', MaterialCompat.safe(XMaterial.ENDER_CHEST));
        recipe.setIngredient('G', eg.getMaterial());
        return recipe;
    }
}

