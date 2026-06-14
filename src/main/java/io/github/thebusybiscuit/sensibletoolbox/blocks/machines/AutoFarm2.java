package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.items.GoldCombineHoe;
import io.github.thebusybiscuit.sensibletoolbox.items.components.MachineFrame;
import io.github.thebusybiscuit.sensibletoolbox.utils.BlockDataCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.Tag;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class AutoFarm2 extends AutoFarm {

    private static final Map<Material, Material> crops = new EnumMap<>(Material.class);
    private static final int RADIUS = 5;

    static {
        crops.put(MaterialCompat.safe(XMaterial.COCOA), MaterialCompat.safe(XMaterial.COCOA_BEANS));
        crops.put(MaterialCompat.safe(XMaterial.SWEET_BERRY_BUSH), MaterialCompat.safe(XMaterial.SWEET_BERRIES));
        crops.put(MaterialCompat.safe(XMaterial.SUGAR_CANE), MaterialCompat.safe(XMaterial.SUGAR_CANE));
        crops.put(MaterialCompat.safe(XMaterial.CACTUS), MaterialCompat.safe(XMaterial.CACTUS));
    }

    private Set<Block> blocks;
    private Material buffer;

    public AutoFarm2() {
        blocks = new HashSet<>();
    }

    public AutoFarm2(ConfigurationSection conf) {
        super(conf);
        blocks = new HashSet<>();
    }

    @Override
    public String getItemName() {
        return "Auto Farm MkII";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Automatically harvests and replants", "Cocoa Beans/Sugar Cane/Cactus", "in a " + RADIUS + "x" + RADIUS + " Radius 2 Blocks above the Machine" };
    }

    @Override
    public Recipe getMainRecipe() {
        MachineFrame frame = new MachineFrame();
        GoldCombineHoe hoe = new GoldCombineHoe();
        registerCustomIngredients(frame, hoe);
        ShapedRecipe res = RecipeCompat.shaped(getKey(), toItemStack());
        res.shape("LHL", "IFI", "RGR");
        res.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        res.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        res.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_INGOT));
        RecipeCompat.setIngredient(res, 'L', Tag.LOGS.getValues());
        res.setIngredient('H', hoe.getMaterial());
        res.setIngredient('F', frame.getMaterial());
        return res;
    }

    @Override
    public void onServerTick() {
        // crop-maturity checks require BlockData (1.13+); this machine is inert on legacy MC
        if (MODERN_BLOCK_DATA && !isJammed()) {
            for (Block crop : blocks) {
                if (crops.containsKey(crop.getType())) {
                    if (BlockDataCompat.isAgeable(crop)) {
                        if (BlockDataCompat.isFullyGrown(crop)) {
                            if (getCharge() >= getScuPerCycle()) {
                                setCharge(getCharge() - getScuPerCycle());
                            } else {
                                break;
                            }

                            BlockDataCompat.resetAge(crop);
                            crop.getWorld().playEffect(crop.getLocation(), Effect.STEP_SOUND, crop.getType());
                            setJammed(!output(crops.get(crop.getType())));
                            break;
                        }
                    } else {
                        Block block = crop.getRelative(BlockFace.UP);

                        if (crops.containsKey(block.getType()) && block.getType() != MaterialCompat.safe(XMaterial.COCOA)) {
                            if (getCharge() >= getScuPerCycle()) {
                                setCharge(getCharge() - getScuPerCycle());
                            } else {
                                break;
                            }

                            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                            setJammed(!output(crops.get(block.getType())));
                            block.setType(MaterialCompat.safe(XMaterial.AIR));
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

    @Override
    public double getScuPerCycle() {
        return 30.0;
    }
}

