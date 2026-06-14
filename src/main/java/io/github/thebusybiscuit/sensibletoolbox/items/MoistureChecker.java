package io.github.thebusybiscuit.sensibletoolbox.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.items.components.SimpleCircuit;
import io.github.thebusybiscuit.sensibletoolbox.utils.BlockDataCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.RecipeCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.MaterialCompat;
import io.github.thebusybiscuit.sensibletoolbox.utils.STBUtil;
import io.github.thebusybiscuit.sensibletoolbox.utils.SoilSaturation;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public class MoistureChecker extends BaseSTBItem {

    public MoistureChecker() {
        super();
    }

    public MoistureChecker(ConfigurationSection conf) {
        super(conf);
    }

    @Override
    public Material getMaterial() {
        return MaterialCompat.safe(XMaterial.GHAST_TEAR);
    }

    @Override
    public String getItemName() {
        return "Moisture Checker";
    }

    @Override
    public String[] getLore() {
        int r = getRadius() * 2 + 1;
        return new String[] { "Tests the saturation level", " of a " + r + "x" + r + " area of farmland.", "R-click: " + ChatColor.WHITE + "use" };
    }

    @Override
    public Recipe getMainRecipe() {
        SimpleCircuit sc = new SimpleCircuit();
        registerCustomIngredients(sc);
        ShapedRecipe recipe = RecipeCompat.shaped(getKey(), toItemStack());
        recipe.shape("S", "C", "I");
        recipe.setIngredient('S', MaterialCompat.safe(XMaterial.OAK_SIGN));
        recipe.setIngredient('C', sc.getMaterial());
        recipe.setIngredient('I', MaterialCompat.safe(XMaterial.GOLDEN_SWORD));
        return recipe;
    }

    @Override
    public boolean hasGlow() {
        return true;
    }

    protected int getRadius() {
        return 1;
    }

    @Override
    public void onInteractItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = event.getClickedBlock();

            if (STBUtil.isCrop(b.getType())) {
                b = b.getRelative(BlockFace.DOWN);
            }

            List<Location> l = new ArrayList<>();

            for (int i = -getRadius(); i <= getRadius(); i++) {
                for (int j = -getRadius(); j <= getRadius(); j++) {
                    Block b1 = b.getRelative(i, 0, j);

                    if (b1.getType() == MaterialCompat.safe(XMaterial.FARMLAND)) {
                        l.add(b1.getLocation());
                    }
                }
            }

            if (!l.isEmpty()) {
                // visual moisture overlay relies on BlockData (1.13+); no-op on legacy MC
                if (BlockDataCompat.isModern()) {
                    Bukkit.getScheduler().runTask(getProviderPlugin(), () -> {
                        for (Location loc : l) {
                            BlockDataCompat.sendBlockChange(player, loc, getWoolFromSaturationlevel(loc.getBlock()));
                        }
                    });

                    Bukkit.getScheduler().runTaskLater(getProviderPlugin(), () -> {
                        for (Location loc : l) {
                            BlockDataCompat.restoreBlockChange(player, loc.getBlock());
                        }
                    }, 30L);
                }

                event.setCancelled(true);
            }
        }
    }

    private Material getWoolFromSaturationlevel(Block b) {
        long now = System.currentTimeMillis();
        long delta = (now - SoilSaturation.getLastWatered(b)) / 1000;
        int saturation = SoilSaturation.getSaturationLevel(b);
        saturation = Math.max(0, saturation - (int) delta);

        if (saturation < 10) {
            return MaterialCompat.safe(XMaterial.YELLOW_WOOL);
        } else if (saturation < 30) {
            return MaterialCompat.safe(XMaterial.BROWN_WOOL);
        } else if (saturation < 50) {
            return MaterialCompat.safe(XMaterial.GREEN_WOOL);
        } else if (saturation < 70) {
            return MaterialCompat.safe(XMaterial.LIGHT_BLUE_WOOL);
        } else if (saturation < 90) {
            return MaterialCompat.safe(XMaterial.CYAN_WOOL);
        } else {
            return MaterialCompat.safe(XMaterial.BLUE_WOOL);
        }
    }
}

