package io.github.thebusybiscuit.sensibletoolbox.items.itemroutermodules;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import me.desht.dhutils.Debugger;

public class ReceiverModule extends ItemRouterModule {

    public ReceiverModule() {}

    public ReceiverModule(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public String getItemName() {
        return "I.R. Mod: Receiver";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Insert into an Item Router", "Passive module; receives items", "from a facing Sender module OR", "linked Adv. Sender module" };
    }

    @Override
    public Recipe getMainRecipe() {
        BlankModule bm = new BlankModule();
        registerCustomIngredients(bm);
        ShapelessRecipe recipe = RecipeCompat.shapeless(getKey(), toItemStack());
        recipe.addIngredient(bm.getMaterial());
        recipe.addIngredient(MaterialCompat.safe(XMaterial.HOPPER));
        return recipe;
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.ORANGE_DYE);
    }

    public int receiveItem(ItemStack item, UUID senderUUID) {
        int received = getItemRouter().insertItems(item, BlockFace.SELF, false, senderUUID);
        if (received > 0) {
            Debugger.getInstance().debug(2, "receiver in " + getItemRouter() + " received " + received + " of " + item + ", now has " + getItemRouter().getBufferItem());
        }
        return received;
    }
}

