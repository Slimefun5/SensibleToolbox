package io.github.thebusybiscuit.sensibletoolbox.blocks.machines;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.items.GoldCombineHoe;
import io.github.thebusybiscuit.sensibletoolbox.items.components.MachineFrame;
import io.github.thebusybiscuit.sensibletoolbox.utils.BlockDataCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class InfernalFarm extends AutoFarm {

    private static final int RADIUS = 5;

    private Set<Block> blocks;
    private Material buffer;

    public InfernalFarm() {
        blocks = new HashSet<>();
    }

    public InfernalFarm(ConfigurationSection conf) {
        super(conf);
        blocks = new HashSet<>();
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.NETHER_BRICKS);
    }

    @Override
    public String getItemName() {
        return "Infernal Farm";
    }

    @Override
    public String[] getLore() {
        return new String[] { "Automatically harvests and replants", "Nether Warts", "in a " + RADIUS + "x" + RADIUS + " Radius 2 Blocks above the Machine" };
    }

    @Override
    public Recipe getMainRecipe() {
        MachineFrame frame = new MachineFrame();
        GoldCombineHoe hoe = new GoldCombineHoe();
        registerCustomIngredients(frame, hoe);
        ShapedRecipe res = RecipeCompat.shaped(getKey(), toItemStack());
        res.shape("NHN", "IFI", "RGR");
        res.setIngredient('R', MaterialCompat.safe(XMaterial.REDSTONE));
        res.setIngredient('G', MaterialCompat.safe(XMaterial.GOLD_INGOT));
        res.setIngredient('I', MaterialCompat.safe(XMaterial.IRON_INGOT));
        res.setIngredient('H', hoe.getMaterial());
        res.setIngredient('F', frame.getMaterial());
        res.setIngredient('N', MaterialCompat.safe(XMaterial.NETHER_BRICK));
        return res;
    }

    @Override
    public void onServerTick() {
        // crop-maturity checks require BlockData (1.13+); this machine is inert on legacy MC
        if (MODERN_BLOCK_DATA && !isJammed()) {
            if (getCharge() >= getScuPerCycle()) {
                for (Block crop : blocks) {
                    if (crop.getType() == MaterialCompat.safe(XMaterial.NETHER_WART)) {
                        if (BlockDataCompat.isFullyGrown(crop)) {
                            setCharge(getCharge() - getScuPerCycle());

                            BlockDataCompat.resetAge(crop);
                            crop.getWorld().playEffect(crop.getLocation(), Effect.STEP_SOUND, crop.getType());
                            setJammed(!output(MaterialCompat.safe(XMaterial.NETHER_WART)));
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
        return 50.0;
    }
}

