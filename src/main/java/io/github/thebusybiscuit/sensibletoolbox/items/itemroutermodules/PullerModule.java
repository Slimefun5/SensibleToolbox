package io.github.thebusybiscuit.sensibletoolbox.items.itemroutermodules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class PullerModule extends DirectionalItemRouterModule {

    public PullerModule() {}

    public PullerModule(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public String getItemName() {
        return "I.R. Mod: Puller";
    }

    @Override
    public String[] getLore() {
        return makeDirectionalLore("Insert into an Item Router", "Pulls items from an adjacent inventory");
    }

    @Override
    public Recipe getMainRecipe() {
        BlankModule bm = new BlankModule();
        registerCustomIngredients(bm);
        ShapelessRecipe recipe = RecipeCompat.shapeless(getKey(), toItemStack());
        recipe.addIngredient(bm.getMaterial());
        recipe.addIngredient(MaterialCompat.safe(XMaterial.STICKY_PISTON));
        return recipe;
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.LIME_DYE);
    }

    @Override
    public boolean execute(Location loc) {
        return getItemRouter() != null && doPull(getFacing(), loc);
    }
}

