package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.SensibleToolbox;
import io.github.thebusybiscuit.sensibletoolbox.api.items.AbstractIOMachine;
import io.github.thebusybiscuit.sensibletoolbox.api.recipes.CustomRecipeManager;
import io.github.thebusybiscuit.sensibletoolbox.api.recipes.SimpleCustomRecipe;
import io.github.thebusybiscuit.sensibletoolbox.items.components.GoldDust;
import io.github.thebusybiscuit.sensibletoolbox.items.components.IronDust;
import io.github.thebusybiscuit.sensibletoolbox.items.components.MachineFrame;
import io.github.thebusybiscuit.sensibletoolbox.items.components.QuartzDust;
import io.github.thebusybiscuit.sensibletoolbox.items.components.SimpleCircuit;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.SoundCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.Tag;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class Masher extends AbstractIOMachine {

    public Masher() {}

    public Masher(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public void addCustomRecipes(CustomRecipeManager crm) {
        QuartzDust qd = new QuartzDust();

        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.COBBLESTONE)), new ItemStack(MaterialCompat.safe(XMaterial.SAND)), 120));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.GRAVEL)), new ItemStack(MaterialCompat.safe(XMaterial.SAND)), 80));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.BONE)), new ItemStack(MaterialCompat.safe(XMaterial.BONE_MEAL), 5), 40));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.BLAZE_ROD)), new ItemStack(MaterialCompat.safe(XMaterial.BLAZE_POWDER), 4), 80));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.COAL_ORE)), new ItemStack(MaterialCompat.safe(XMaterial.COAL), 2), 100));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE_ORE)), new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE), 6), 100));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.DIAMOND_ORE)), new ItemStack(MaterialCompat.safe(XMaterial.DIAMOND), 2), 160));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.IRON_ORE)), new IronDust().toItemStack(2), 120));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT)), new IronDust().toItemStack(), 120));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.GOLD_ORE)), new GoldDust().toItemStack(2), 80));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.GOLD_INGOT)), new GoldDust().toItemStack(), 80));

        for (Material wool : Tag.WOOL.getValues()) {
            crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(wool), new ItemStack(MaterialCompat.safe(XMaterial.STRING), 4), 60));
        }

        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.GLOWSTONE)), new ItemStack(MaterialCompat.safe(XMaterial.GLOWSTONE_DUST), 4), 60));
        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.LAPIS_ORE)), new ItemStack(MaterialCompat.safe(XMaterial.LAPIS_LAZULI), 8), 80));
        //
        for (Material leaves : Tag.LEAVES.getValues()) {
            crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(leaves), new ItemStack(MaterialCompat.safe(XMaterial.GREEN_DYE)), 40), true);
        }

        crm.addCustomRecipe(new SimpleCustomRecipe(this, new ItemStack(MaterialCompat.safe(XMaterial.QUARTZ)), qd.toItemStack(), 120));
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GREEN_TERRACOTTA);
    }

    @Override
    public String getItemName() {
        return "Masher";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Grinds ores and other ", "resources into dusts" };
    }

    @Override
    public Recipe getMainRecipe() {
        SimpleCircuit sc = new SimpleCircuit();
        MachineFrame mf = new MachineFrame();
        registerCustomIngredients(sc, mf);
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("FFF", "SIS", "RGR");
        recipe.setIngredient('F', MaterialCompat.safe(XMaterial.FLINT));
        recipe.setIngredient('S', sc.getMaterial());
        recipe.setIngredient('I', mf.getMaterial());
        recipe.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        recipe.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        return recipe;
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { 10 };
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] { 14, 15 };
    }

    @Override
    public int[] getUpgradeSlots() {
        return new int[] { 41, 42, 43, 44 };
    }

    @Override
    public int getUpgradeLabelSlot() {
        return 40;
    }

    @Override
    public int getEnergyCellSlot() {
        return 36;
    }

    @Override
    public int getChargeDirectionSlot() {
        return 37;
    }

    @Override
    public int getInventoryGUISize() {
        return 45;
    }

    @Override
    public int getMaxCharge() {
        return 1000;
    }

    @Override
    public int getChargeRate() {
        return 20;
    }

    @Override
    public int getProgressItemSlot() {
        return 12;
    }

    @Override
    public int getProgressCounterSlot() {
        return 3;
    }

    @Override
    public ItemStack getProgressIcon() {
        return new ItemStack(MaterialCompat.safe(XMaterial.GOLDEN_PICKAXE));
    }

    @Override
    protected void onMachineStartup() {
        if (SensibleToolbox.getPluginInstance().getConfigCache().isNoisyMachines()) {
            SoundCompat.play(getLocation().getWorld(), getLocation(), "ENTITY_SKELETON_HORSE_AMBIENT", 1.0F, 0.5F);
        }
    }

    // TODO: Fix particles
    // @Override
    // protected void playActiveParticleEffect() {
    // if (((SensibleToolboxPlugin) getProviderPlugin()).isProtocolLibEnabled() && getTicksLived() % 20 == 0) {
    // ParticleEffect.LARGE_SMOKE.play(getLocation().add(0.5, 1.0, 0.5), 0.2f, 1.0f, 0.2f, 0.001f, 5);
    // }
    // }
}

