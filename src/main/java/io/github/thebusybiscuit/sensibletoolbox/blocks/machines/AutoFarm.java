package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.sensibletoolbox.api.items.AutoFarmingMachine;
import io.github.thebusybiscuit.sensibletoolbox.items.IronCombineHoe;
import io.github.thebusybiscuit.sensibletoolbox.items.components.MachineFrame;
import io.github.thebusybiscuit.sensibletoolbox.utils.BlockDataCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class AutoFarm extends AutoFarmingMachine {

    // BlockData crop API is only available on MC 1.13+
    protected static final boolean MODERN_BLOCK_DATA = BlockDataCompat.isModern();

    private static final Map<Material, Material> crops = new EnumMap<>(Material.class);
    private static final int RADIUS = 3;

    static {
        crops.put(MaterialCompat.safe(XMaterial.WHEAT), MaterialCompat.safe(XMaterial.WHEAT));
        crops.put(MaterialCompat.safe(XMaterial.POTATOES), MaterialCompat.safe(XMaterial.POTATO));
        crops.put(MaterialCompat.safe(XMaterial.CARROTS), MaterialCompat.safe(XMaterial.CARROT));
        crops.put(MaterialCompat.safe(XMaterial.BEETROOTS), MaterialCompat.safe(XMaterial.BEETROOT));
    }

    private Set<Block> blocks;
    private Material buffer;

    public AutoFarm() {
        super();
        blocks = new HashSet<>();
    }

    public AutoFarm(ConfigurationSection conf) {
        super(conf);
        blocks = new HashSet<>();
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.BROWN_TERRACOTTA);
    }

    @Override
    public String getItemName() {
        return "Auto Farm";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Automatically harvests and replants", "Wheat/Potato/Carrot Crops", "in a " + RADIUS + "x" + RADIUS + " Radius 2 Blocks above the Machine" };
    }

    @Override
    public Recipe getMainRecipe() {
        MachineFrame frame = new MachineFrame();
        IronCombineHoe hoe = new IronCombineHoe();
        registerCustomIngredients(frame, hoe);
        ShapedRecipe res = RecipeCompat.shaped(getKey(), toItemStack());
        res.shape(" H ", "IFI", "RGR");
        res.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        res.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        res.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_INGOT));
        res.setIngredient('H', hoe.getMaterial());
        res.setIngredient('F', frame.getMaterial());
        return res;
    }

    @Override
    public void onBlockRegistered(Location location, boolean isPlacing) {
        int range = RADIUS / 2;
        Block block = location.getBlock();

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                blocks.add(block.getRelative(x, 0, z));
            }
        }

        super.onBlockRegistered(location, isPlacing);
    }

    @Override
    public void onServerTick() {
        // crop-maturity checks require BlockData (1.13+); this machine is inert on legacy MC
        if (MODERN_BLOCK_DATA && !isJammed()) {
            if (getCharge() >= getScuPerCycle()) {
                for (Block crop : blocks) {
                    if (crops.containsKey(crop.getType())) {
                        if (BlockDataCompat.isFullyGrown(crop)) {
                            setCharge(getCharge() - getScuPerCycle());

                            BlockDataCompat.resetAge(crop);
                            crop.getWorld().playEffect(crop.getLocation(), Effect.STEP_SOUND, crop.getType());
                            setJammed(!output(crops.get(crop.getType())));
                            break;
                        }
                    }
                }
            }
        } else if (buffer != null) {
            setJammed(!output(buffer));
        }

        super.onServerTick();
    }

    protected boolean output(@Nonnull Material m) {
        for (int slot : getOutputSlots()) {
            ItemStack stack = getInventoryItem(slot);

            if (stack == null || (stack.getType() == m && stack.getAmount() < stack.getMaxStackSize())) {
                if (stack == null) {
                    stack = new ItemStack(m);
                }

                int amount = 1;

                if (!m.isBlock()) {
                    amount = (stack.getMaxStackSize() - stack.getAmount()) > 3 ? (ThreadLocalRandom.current().nextInt(2) + 1) : (stack.getMaxStackSize() - stack.getAmount());
                }

                setInventoryItem(slot, CustomItemStack.create(stack, stack.getAmount() + amount));
                buffer = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public double getScuPerCycle() {
        return 25.0;
    }
}


