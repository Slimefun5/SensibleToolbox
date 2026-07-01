package io.github.thebusybiscuit.sensibletoolbox.items.upgrades;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.items.components.SimpleCircuit;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class RegulatorUpgrade extends AbstractMachineUpgrade {

    public RegulatorUpgrade() {}

    public RegulatorUpgrade(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.ENDER_EYE);
    }

    @Override
    public String getItemName() {
        return "Regulator Upgrade";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Adds intelligence to machines", "for more efficient resource", "usage.  Effect varies by machine." };
    }

    @Override
    public Recipe getMainRecipe() {
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        SimpleCircuit sc = new SimpleCircuit();
        registerCustomIngredients(sc);
        recipe.shape("ISI", "IEI", "IRI");
        recipe.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_BARS));
        recipe.setIngredient('S', sc.getMaterial());
        recipe.setIngredient('E', MaterialCompat.safe(XMaterial.ENDER_EYE));
        recipe.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        return recipe;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }
}

