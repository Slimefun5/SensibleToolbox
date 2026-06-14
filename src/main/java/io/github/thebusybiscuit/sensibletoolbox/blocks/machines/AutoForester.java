package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.slimefun5.libraries.dough.blocks.Vein;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.sensibletoolbox.api.items.AutoFarmingMachine;
import io.github.thebusybiscuit.sensibletoolbox.items.components.MachineFrame;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialConverter;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.Tag;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class AutoForester extends AutoFarmingMachine {

    private static final int RADIUS = 5;
    private static final int MAX_REACH = 256;

    private final Set<Block> blocks = new HashSet<>();
    private Material buffer;

    public AutoForester() {

    }

    public AutoForester(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.BROWN_TERRACOTTA);
    }

    @Override
    public String getItemName() {
        return "Auto Forester";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Automatically harvests and replants", "Trees", "in a " + RADIUS + "x" + RADIUS + " Radius 2 Blocks above the Machine" };
    }

    @Override
    public Recipe getMainRecipe() {
        MachineFrame frame = new MachineFrame();
        registerCustomIngredients(frame);
        ShapedRecipe res = RecipeCompat.shaped(getKey(), toItemStack());
        res.shape("A A", "IFI", "RGR");
        res.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        res.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        res.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_INGOT));
        res.setIngredient('A', MaterialCompat.safe(XMaterial.IRON_AXE));
        res.setIngredient('F', frame.getMaterial());
        return res;
    }

    @Override
    public void onBlockRegistered(Location location, boolean isPlacing) {
        int i = RADIUS / 2;

        for (int x = -i; x <= i; x++) {
            for (int z = -i; z <= i; z++) {
                blocks.add(new Location(location.getWorld(), location.getBlockX() + (double) x, location.getBlockY() + 2.0, location.getBlockZ() + (double) z).getBlock());
            }
        }

        super.onBlockRegistered(location, isPlacing);
    }

    @Override
    public void onServerTick() {
        if (!isJammed()) {
            for (Block log : blocks) {
                if (Tag.LOGS.isTagged(log.getType())) {
                    if (getCharge() >= getScuPerCycle()) {
                        setCharge(getCharge() - getScuPerCycle());
                    } else {
                        break;
                    }

                    List<Block> list = Vein.find(log, MAX_REACH, block -> Tag.LOGS.isTagged(block.getType()));

                    for (Block b : list) {
                        buffer = b.getType();
                        setJammed(!output(buffer));

                        if (isJammed()) {
                            return;
                        }

                        log.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

                        if (blocks.contains(b)) {
                            Optional<Material> sapling = MaterialConverter.getSaplingFromLog(b.getType());

                            if (sapling.isPresent()) {
                                b.setType(sapling.get());
                            } else {
                                b.setType(MaterialCompat.safe(XMaterial.AIR));
                            }
                        } else {
                            b.setType(MaterialCompat.safe(XMaterial.AIR));
                        }
                    }

                    break;
                }
            }
        } else if (buffer != null) {
            setJammed(!output(buffer));
        }

        super.onServerTick();
    }

    private boolean output(@Nonnull Material m) {
        for (int slot : getOutputSlots()) {
            ItemStack stack = getInventoryItem(slot);
            if (stack == null || (stack.getType() == m && stack.getAmount() < stack.getMaxStackSize())) {
                if (stack == null) {
                    stack = new ItemStack(m);
                }

                setInventoryItem(slot, CustomItemStack.create(stack, stack.getAmount() + 1));
                buffer = null;
                return true;
            }
        }

        return false;
    }

    @Override
    public double getScuPerCycle() {
        return 250.0;
    }
}


