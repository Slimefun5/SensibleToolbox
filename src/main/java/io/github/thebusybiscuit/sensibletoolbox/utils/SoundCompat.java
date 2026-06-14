package io.github.thebusybiscuit.sensibletoolbox.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Java-8 universal port: plays sounds by their modern (1.9+) enum name, resolving version-safely.
 * <p>
 * The {@link Sound} enum was completely renamed in 1.9 (e.g. {@code CHEST_OPEN} became
 * {@code BLOCK_CHEST_OPEN}). Referencing a modern constant directly throws {@code NoSuchFieldError} on
 * 1.8. Each constant is resolved via {@link Sound#valueOf(String)}; if absent, a hand-mapped legacy
 * name is tried, and if that also fails the sound is silently skipped (sounds are cosmetic).
 */
public final class SoundCompat {

    private SoundCompat() {}

    private static final Map<String, String> LEGACY_NAMES = new HashMap<>();

    static {
        LEGACY_NAMES.put("BLOCK_CHEST_OPEN", "CHEST_OPEN");
        LEGACY_NAMES.put("BLOCK_FIRE_AMBIENT", "FIRE");
        LEGACY_NAMES.put("BLOCK_GRASS_BREAK", "DIG_GRASS");
        LEGACY_NAMES.put("BLOCK_STONE_BREAK", "DIG_STONE");
        LEGACY_NAMES.put("BLOCK_WATER_AMBIENT", "WATER");
        LEGACY_NAMES.put("BLOCK_PISTON_EXTEND", "PISTON_EXTEND");
        LEGACY_NAMES.put("BLOCK_PISTON_CONTRACT", "PISTON_RETRACT");
        LEGACY_NAMES.put("BLOCK_NOTE_BLOCK_BASS", "NOTE_BASS");
        LEGACY_NAMES.put("BLOCK_NOTE_BLOCK_PLING", "NOTE_PLING");
        LEGACY_NAMES.put("UI_BUTTON_CLICK", "CLICK");
        LEGACY_NAMES.put("ENTITY_CHICKEN_EGG", "CHICKEN_EGG_POP");
        LEGACY_NAMES.put("ENTITY_ENDER_DRAGON_HURT", "ENDERDRAGON_HIT");
        LEGACY_NAMES.put("ENTITY_EXPERIENCE_ORB_PICKUP", "ORB_PICKUP");
        LEGACY_NAMES.put("ENTITY_GENERIC_EAT", "EAT");
        LEGACY_NAMES.put("ENTITY_GENERIC_SPLASH", "SPLASH");
        LEGACY_NAMES.put("ENTITY_PLAYER_SPLASH", "SPLASH");
        LEGACY_NAMES.put("ENTITY_ITEM_PICKUP", "ITEM_PICKUP");
        LEGACY_NAMES.put("ENTITY_PLAYER_LEVELUP", "LEVEL_UP");
        LEGACY_NAMES.put("ENTITY_HORSE_STEP_WOOD", "HORSE_WOOD");
        LEGACY_NAMES.put("ENTITY_SKELETON_HORSE_AMBIENT", "HORSE_SKELETON_IDLE");
        LEGACY_NAMES.put("AMBIENT_UNDERWATER_EXIT", "SPLASH");
    }

    private static Sound resolve(String name) {
        try {
            return Sound.valueOf(name);
        } catch (IllegalArgumentException ignored) {
            String legacy = LEGACY_NAMES.get(name);

            if (legacy != null) {
                try {
                    return Sound.valueOf(legacy);
                } catch (IllegalArgumentException ignored2) {
                    // fall through
                }
            }

            return null;
        }
    }

    public static void play(Player player, Location location, String soundName, float volume, float pitch) {
        Sound sound = resolve(soundName);

        if (sound != null) {
            player.playSound(location, sound, volume, pitch);
        }
    }

    public static void play(org.bukkit.World world, Location location, String soundName, float volume, float pitch) {
        Sound sound = resolve(soundName);

        if (sound != null) {
            world.playSound(location, sound, volume, pitch);
        }
    }
}
