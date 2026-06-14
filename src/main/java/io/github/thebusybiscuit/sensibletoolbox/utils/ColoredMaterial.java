package io.github.thebusybiscuit.sensibletoolbox.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

/**
 * This class holds a few ordered {@link List Lists} that hold colored variants
 * of {@link Material}.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum ColoredMaterial {

    // @formatter:off (We want this to stay formatted like this)
    
    /**
     * This {@link List} contains all wool colors ordered by their appearance ingame.
     */
    WOOL(new Material[] {
            MaterialCompat.safe(XMaterial.WHITE_WOOL),
            MaterialCompat.safe(XMaterial.ORANGE_WOOL),
            MaterialCompat.safe(XMaterial.MAGENTA_WOOL),
            MaterialCompat.safe(XMaterial.LIGHT_BLUE_WOOL),
            MaterialCompat.safe(XMaterial.YELLOW_WOOL),
            MaterialCompat.safe(XMaterial.LIME_WOOL),
            MaterialCompat.safe(XMaterial.PINK_WOOL),
            MaterialCompat.safe(XMaterial.GRAY_WOOL),
            MaterialCompat.safe(XMaterial.LIGHT_GRAY_WOOL),
            MaterialCompat.safe(XMaterial.CYAN_WOOL),
            MaterialCompat.safe(XMaterial.PURPLE_WOOL),
            MaterialCompat.safe(XMaterial.BLUE_WOOL),
            MaterialCompat.safe(XMaterial.BROWN_WOOL),
            MaterialCompat.safe(XMaterial.GREEN_WOOL),
            MaterialCompat.safe(XMaterial.RED_WOOL),
            MaterialCompat.safe(XMaterial.BLACK_WOOL)
    }),

    /**
     * This {@link List} contains all carpet colors ordered by their appearance ingame.
     */
    CARPET(new Material[] {
            MaterialCompat.safe(XMaterial.WHITE_CARPET),
            MaterialCompat.safe(XMaterial.ORANGE_CARPET),
            MaterialCompat.safe(XMaterial.MAGENTA_CARPET),
            MaterialCompat.safe(XMaterial.LIGHT_BLUE_CARPET),
            MaterialCompat.safe(XMaterial.YELLOW_CARPET),
            MaterialCompat.safe(XMaterial.LIME_CARPET),
            MaterialCompat.safe(XMaterial.PINK_CARPET),
            MaterialCompat.safe(XMaterial.GRAY_CARPET),
            MaterialCompat.safe(XMaterial.LIGHT_GRAY_CARPET),
            MaterialCompat.safe(XMaterial.CYAN_CARPET),
            MaterialCompat.safe(XMaterial.PURPLE_CARPET),
            MaterialCompat.safe(XMaterial.BLUE_CARPET),
            MaterialCompat.safe(XMaterial.BROWN_CARPET),
            MaterialCompat.safe(XMaterial.GREEN_CARPET),
            MaterialCompat.safe(XMaterial.RED_CARPET),
            MaterialCompat.safe(XMaterial.BLACK_CARPET)
    }),

    /**
     * This {@link List} contains all stained glass colors ordered by their appearance ingame.
     */
    STAINED_GLASS(new Material[] {
            MaterialCompat.safe(XMaterial.WHITE_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.ORANGE_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.MAGENTA_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.LIGHT_BLUE_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.YELLOW_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.LIME_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.PINK_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.GRAY_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.LIGHT_GRAY_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.CYAN_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.PURPLE_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.BLUE_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.BROWN_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.GREEN_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.RED_STAINED_GLASS),
            MaterialCompat.safe(XMaterial.BLACK_STAINED_GLASS)
    }),

    /**
     * This {@link List} contains all stained glass pane colors ordered by their appearance ingame.
     */
    STAINED_GLASS_PANE(new Material[] {
            MaterialCompat.safe(XMaterial.WHITE_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.ORANGE_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.MAGENTA_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.YELLOW_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.LIME_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.PINK_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.GRAY_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.CYAN_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.PURPLE_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.BLUE_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.BROWN_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.GREEN_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.RED_STAINED_GLASS_PANE),
            MaterialCompat.safe(XMaterial.BLACK_STAINED_GLASS_PANE)
    }),

    /**
     * This {@link List} contains all terracotta colors ordered by their appearance ingame.
     */
    TERRACOTTA(new Material[] {
            MaterialCompat.safe(XMaterial.WHITE_TERRACOTTA),
            MaterialCompat.safe(XMaterial.ORANGE_TERRACOTTA),
            MaterialCompat.safe(XMaterial.MAGENTA_TERRACOTTA),
            MaterialCompat.safe(XMaterial.LIGHT_BLUE_TERRACOTTA),
            MaterialCompat.safe(XMaterial.YELLOW_TERRACOTTA),
            MaterialCompat.safe(XMaterial.LIME_TERRACOTTA),
            MaterialCompat.safe(XMaterial.PINK_TERRACOTTA),
            MaterialCompat.safe(XMaterial.GRAY_TERRACOTTA),
            MaterialCompat.safe(XMaterial.LIGHT_GRAY_TERRACOTTA),
            MaterialCompat.safe(XMaterial.CYAN_TERRACOTTA),
            MaterialCompat.safe(XMaterial.PURPLE_TERRACOTTA),
            MaterialCompat.safe(XMaterial.BLUE_TERRACOTTA),
            MaterialCompat.safe(XMaterial.BROWN_TERRACOTTA),
            MaterialCompat.safe(XMaterial.GREEN_TERRACOTTA),
            MaterialCompat.safe(XMaterial.RED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.BLACK_TERRACOTTA)
    }),

    /**
     * This {@link List} contains all glazed terracotta colors ordered by their appearance ingame.
     */
    GLAZED_TERRACOTTA(new Material[] {
            MaterialCompat.safe(XMaterial.WHITE_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.ORANGE_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.MAGENTA_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.LIGHT_BLUE_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.YELLOW_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.LIME_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.PINK_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.GRAY_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.LIGHT_GRAY_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.CYAN_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.PURPLE_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.BLUE_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.BROWN_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.GREEN_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.RED_GLAZED_TERRACOTTA),
            MaterialCompat.safe(XMaterial.BLACK_GLAZED_TERRACOTTA)
    }),

    /**
     * This {@link List} contains all concrete colors ordered by their appearance ingame.
     */
    CONCRETE(new Material[] {
            MaterialCompat.safe(XMaterial.WHITE_CONCRETE),
            MaterialCompat.safe(XMaterial.ORANGE_CONCRETE),
            MaterialCompat.safe(XMaterial.MAGENTA_CONCRETE),
            MaterialCompat.safe(XMaterial.LIGHT_BLUE_CONCRETE),
            MaterialCompat.safe(XMaterial.YELLOW_CONCRETE),
            MaterialCompat.safe(XMaterial.LIME_CONCRETE),
            MaterialCompat.safe(XMaterial.PINK_CONCRETE),
            MaterialCompat.safe(XMaterial.GRAY_CONCRETE),
            MaterialCompat.safe(XMaterial.LIGHT_GRAY_CONCRETE),
            MaterialCompat.safe(XMaterial.CYAN_CONCRETE),
            MaterialCompat.safe(XMaterial.PURPLE_CONCRETE),
            MaterialCompat.safe(XMaterial.BLUE_CONCRETE),
            MaterialCompat.safe(XMaterial.BROWN_CONCRETE),
            MaterialCompat.safe(XMaterial.GREEN_CONCRETE),
            MaterialCompat.safe(XMaterial.RED_CONCRETE),
            MaterialCompat.safe(XMaterial.BLACK_CONCRETE)
    });
    
    // @formatter:on

    /**
     * This is our {@link List} of {@link Material Materials}, the backbone of this enum.
     */
    private final List<Material> list;

    /**
     * This creates a new constant of {@link ColoredMaterial}.
     * The array must have a length of 16 and cannot contain null elements!
     * 
     * @param materials
     *            The {@link Material Materials} for this {@link ColoredMaterial}.
     */
    ColoredMaterial(@Nonnull Material[] materials) {
        Validate.noNullElements(materials, "The List cannot contain any null elements");
        Validate.isTrue(materials.length == 16, "Expected 16, received: " + materials.length + ". Did you miss a color?");

        list = Collections.unmodifiableList(Arrays.asList(materials));
    }

    /**
     * This returns an ordered {@link List} of {@link Material Materials}
     * that are part o this {@link ColoredMaterial}.
     * 
     * @return An ordered {@link List} of {@link Material Materials}
     */
    @Nonnull
    public List<Material> asList() {
        return list;
    }

    /**
     * This returns the {@link Material} at the given index.
     * 
     * @param index
     *            The index
     * 
     * @return The {@link Material} at that index
     */
    @Nonnull
    public Material get(int index) {
        Validate.isTrue(index >= 0 && index < 16, "The index must be between 0 and 15 (inclusive).");

        return list.get(index);
    }

    /**
     * This returns the {@link Material} with the given {@link DyeColor}.
     * 
     * @param color
     *            The {@link DyeColor}
     * 
     * @return The {@link Material} with that {@link DyeColor}
     */
    @Nonnull
    public Material get(@Nonnull DyeColor color) {
        Validate.notNull(color, "Color cannot be null!");

        return get(color.ordinal());
    }

}

