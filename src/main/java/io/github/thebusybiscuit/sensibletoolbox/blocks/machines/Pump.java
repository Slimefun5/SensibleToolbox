package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.slimefun5.libraries.dough.blocks.Vein;
import io.github.thebusybiscuit.sensibletoolbox.api.items.AbstractProcessingMachine;
import io.github.thebusybiscuit.sensibletoolbox.items.components.MachineFrame;
import io.github.thebusybiscuit.sensibletoolbox.items.components.SimpleCircuit;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.STBUtil;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class Pump extends AbstractProcessingMachine {

    // 40 ticks to fill a bucket
    private static final int PUMP_FILL_TIME = 40;
    // will be configurable later
    private BlockFace pumpFace = BlockFace.DOWN;

    public Pump() {
        super();
    }

    public Pump(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public int getTickRate() {
        return 5;
    }

    @Override
    public int getInventoryGUISize() {
        return 45;
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
        return new ItemStack(MaterialCompat.safe(XMaterial.DIAMOND_BOOTS));
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { 10 };
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] { 14 };
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
    protected void playActiveParticleEffect() {
        getLocation().getWorld().playEffect(getLocation(), Effect.STEP_SOUND, getRelativeLocation(pumpFace).getBlock().getType());
    }

    @Override
    public boolean acceptsEnergy(BlockFace face) {
        return true;
    }

    @Override
    public boolean suppliesEnergy(BlockFace face) {
        return false;
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
    public int getMaxCharge() {
        return 1000;
    }

    @Override
    public int getChargeRate() {
        return 20;
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.CYAN_TERRACOTTA);
    }

    @Override
    public String getItemName() {
        return "Pump";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Pumps liquids into a bucket" };
    }

    @Override
    public Recipe getMainRecipe() {
        SimpleCircuit sc = new SimpleCircuit();
        MachineFrame mf = new MachineFrame();
        registerCustomIngredients(sc, mf);
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("PB ", "SIS", "RGR");
        recipe.setIngredient('P', MaterialCompat.safe(XMaterial.PISTON));
        recipe.setIngredient('B', MaterialCompat.safe(XMaterial.BUCKET));
        recipe.setIngredient('S', sc.getMaterial());
        recipe.setIngredient('I', mf.getMaterial());
        recipe.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        recipe.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        return recipe;
    }

    @Override
    public double getScuPerTick() {
        // 0.1 SCU to fill a bucket
        return 0.1 / PUMP_FILL_TIME;
    }

    @Override
    public void onServerTick() {
        int inputSlot = getInputSlots()[0];
        ItemStack stackIn = getInventoryItem(inputSlot);

        Block toPump = findNextBlockToPump();

        if (getProcessing() == null && stackIn != null && isRedstoneActive()) {
            // pull a bucket from the input stack into processing
            ItemStack toProcess = makeProcessingItem(toPump, stackIn.getType());
            setProcessing(toProcess);

            if (toProcess != null) {
                getProgressMeter().setMaxProgress(PUMP_FILL_TIME);
                setProgress(PUMP_FILL_TIME);
                stackIn.setAmount(stackIn.getAmount() - 1);
                setInventoryItem(inputSlot, stackIn);
            }
        }

        if (getProgress() > 0 && getCharge() > 0 && STBUtil.isLiquidSourceBlock(toPump)) {
            // currently processing....
            setProgress(getProgress() - getSpeedMultiplier() * getTickRate());
            setCharge(getCharge() - getPowerMultiplier() * getScuPerTick() * getTickRate());
            playActiveParticleEffect();
        }

        if (getProcessing() != null && getProgress() <= 0 && !isJammed()) {
            // done processing - try to move filled container into output
            ItemStack result = getProcessing();
            int slot = findOutputSlot(result);
            if (slot >= 0) {
                setInventoryItem(slot, result);
                setProcessing(null);
                update(false);
                replacePumpedBlock(toPump);
            } else {
                setJammed(true);
            }
        }

        handleAutoEjection();

        super.onServerTick();
    }

    @Nonnull
    private Block findNextBlockToPump() {
        Block target = getRelativeLocation(pumpFace).getBlock();

        if (target.getType() == MaterialCompat.safe(XMaterial.LAVA)) {
            List<Block> list = Vein.find(target, 64, block -> block.getType() == MaterialCompat.safe(XMaterial.LAVA));
            return list.get(list.size() - 1);
        } else {
            return target;
        }
    }

    private void replacePumpedBlock(@Nonnull Block block) {
        if (STBUtil.isInfiniteWaterSource(block)) {
            return;
        }

        switch (block.getType()) {
            case WATER:
                block.setType(MaterialCompat.safe(XMaterial.AIR));
                break;
            case LAVA:
                block.setType(MaterialCompat.safe(XMaterial.STONE));
                break;
            default:
                break;
        }
    }

    @Nullable
    private ItemStack makeProcessingItem(@Nonnull Block fluid, @Nonnull Material container) {
        if (!STBUtil.isLiquidSourceBlock(fluid)) {
            return null;
        }

        Material type = fluid.getType();

        if (container == MaterialCompat.safe(XMaterial.BUCKET)) {
            switch (type) {
                case LAVA:
                    return new ItemStack(MaterialCompat.safe(XMaterial.LAVA_BUCKET));
                case BUBBLE_COLUMN:
                case WATER:
                    return new ItemStack(MaterialCompat.safe(XMaterial.WATER_BUCKET));
                default:
                    return null;
            }
        } else if (container == MaterialCompat.safe(XMaterial.GLASS_BOTTLE)) {
            switch (type) {
                case BUBBLE_COLUMN:
                case WATER:
                    return new ItemStack(MaterialCompat.safe(XMaterial.POTION));
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean acceptsItemType(ItemStack stack) {
        return stack.getType() == MaterialCompat.safe(XMaterial.BUCKET) || stack.getType() == MaterialCompat.safe(XMaterial.GLASS_BOTTLE);
    }
}


