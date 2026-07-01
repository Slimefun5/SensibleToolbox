package io.github.thebusybiscuit.sensibletoolbox.items.upgrades;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.items.components.SimpleCircuit;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class SpeedUpgrade extends AbstractMachineUpgrade {

    public SpeedUpgrade() {}

    public SpeedUpgrade(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.SUGAR);
    }

    @Override
    public boolean hasGlow() {
        return true;
    }

    @Override
    public String getItemName() {
        return "Speed Upgrade";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Place in a machine block", "Speed: x1.4", "Power Usage: x1.6" };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        SimpleCircuit sc = new SimpleCircuit();
        registerCustomIngredients(sc);
        recipe.shape("ISI", "IBI", "IGI");
        recipe.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_BARS));
        recipe.setIngredient('S', sc.getMaterial());
        recipe.setIngredient('B', MaterialCompat.safe(XMaterial.BLAZE_ROD));
        recipe.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        return recipe;
    }
}

