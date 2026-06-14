package io.github.thebusybiscuit.sensibletoolbox.items.itemroutermodules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import me.desht.dhutils.Debugger;

public class DropperModule extends DirectionalItemRouterModule {

    public DropperModule() {}

    public DropperModule(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public String getItemName() {
        return "I.R. Mod: Dropper";
    }

    @Override
    public String[] getLore() {
        return makeDirectionalLore("Insert into an Item Router", "Drops items onto the ground", "in the configured direction");
    }

    @Override
    public Recipe getMainRecipe() {
        BlankModule bm = new BlankModule();
        registerCustomIngredients(bm);
        ShapelessRecipe recipe = RecipeCompat.shapeless(getKey(), toItemStack());
        recipe.addIngredient(bm.getMaterial());
        recipe.addIngredient(MaterialCompat.safe(XMaterial.DROPPER));
        return recipe;
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GRAY_DYE);
    }

    @Override
    public boolean execute(Location loc) {
        if (getItemRouter() != null && getItemRouter().getBufferItem() != null) {
            if (getFilter() != null && !getFilter().shouldPass(getItemRouter().getBufferItem())) {
                return false;
            }

            int toDrop = getItemRouter().getStackSize();
            ItemStack stack = getItemRouter().extractItems(BlockFace.SELF, null, toDrop, null);

            if (stack != null) {
                Location targetLoc = getTargetLocation(loc).add(0.5, 0.5, 0.5);
                Item item = targetLoc.getWorld().dropItem(targetLoc, stack);
                item.setVelocity(new Vector(0, 0, 0));
                Debugger.getInstance().debug(2, "dropper dropped " + stack + " from " + getItemRouter());
            }

            return true;
        }
        return false;
    }
}

