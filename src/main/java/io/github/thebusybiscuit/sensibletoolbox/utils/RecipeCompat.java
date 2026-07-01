package io.github.thebusybiscuit.sensibletoolbox.utils;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.BukkitKeys;

/**
 * Java-8 universal port: builds keyed recipes ({@link ShapedRecipe}, {@link ShapelessRecipe},
 * {@link FurnaceRecipe}) version-safely.
 * <p>
 * The {@code (NamespacedKey, ...)} recipe constructors and {@code setIngredient(char, RecipeChoice)}
 * are 1.12+/1.13+ only. On those versions we reflectively invoke them, converting Slimefun's own
 * {@link NamespacedKey} to the real {@code org.bukkit.NamespacedKey} via {@link BukkitKeys}. On 1.8
 * we fall back to the legacy result-only constructors and {@code setIngredient(char, Material)}.
 */
public final class RecipeCompat {

    private RecipeCompat() {}

    private static final boolean KEYED_RECIPES = BukkitKeys.toBukkit(NamespacedKey.minecraft("probe")) != null;

    public static ShapedRecipe shaped(NamespacedKey key, ItemStack result) {
        if (KEYED_RECIPES) {
            ShapedRecipe recipe = newKeyed(ShapedRecipe.class, key, result);
            if (recipe != null) {
                return recipe;
            }
        }

        return new ShapedRecipe(result);
    }

    public static ShapelessRecipe shapeless(NamespacedKey key, ItemStack result) {
        if (KEYED_RECIPES) {
            ShapelessRecipe recipe = newKeyed(ShapelessRecipe.class, key, result);
            if (recipe != null) {
                return recipe;
            }
        }

        return new ShapelessRecipe(result);
    }

    public static FurnaceRecipe furnace(NamespacedKey key, ItemStack result, Material source, float experience, int cookingTime) {
        if (KEYED_RECIPES) {
            try {
                Object bukkitKey = BukkitKeys.toBukkit(key);
                Constructor<FurnaceRecipe> ctor = FurnaceRecipe.class.getConstructor(Class.forName("org.bukkit.NamespacedKey"), ItemStack.class, Material.class, float.class, int.class);
                return ctor.newInstance(bukkitKey, result, source, experience, cookingTime);
            } catch (Throwable ignored) {
                // fall through to legacy constructor
            }
        }

        return new FurnaceRecipe(result, source);
    }

    private static <T> T newKeyed(Class<T> recipeType, NamespacedKey key, ItemStack result) {
        try {
            Object bukkitKey = BukkitKeys.toBukkit(key);
            Constructor<T> ctor = recipeType.getConstructor(Class.forName("org.bukkit.NamespacedKey"), ItemStack.class);
            return ctor.newInstance(bukkitKey, result);
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * Sets a shaped-recipe ingredient to any of several allowed materials. On 1.13+ this uses a
     * {@code RecipeChoice.MaterialChoice}; on 1.8 it falls back to the first material via the legacy
     * {@code setIngredient(char, Material)}.
     */
    public static void setIngredient(ShapedRecipe recipe, char key, Material... choices) {
        if (choices.length == 0) {
            return;
        }

        if (!setMaterialChoice(recipe, "setIngredient", key, choices)) {
            recipe.setIngredient(key, choices[0]);
        }
    }

    public static void setIngredient(ShapedRecipe recipe, char key, Collection<Material> choices) {
        setIngredient(recipe, key, choices.toArray(new Material[0]));
    }

    private static boolean setMaterialChoice(Object recipe, String method, char key, Material... choices) {
        try {
            Class<?> recipeChoice = Class.forName("org.bukkit.inventory.RecipeChoice");
            Class<?> materialChoice = Class.forName("org.bukkit.inventory.RecipeChoice$MaterialChoice");
            Object choice = materialChoice.getConstructor(Material[].class).newInstance((Object) choices);
            recipe.getClass().getMethod(method, char.class, recipeChoice).invoke(recipe, key, choice);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
