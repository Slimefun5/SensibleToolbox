package io.github.thebusybiscuit.sensibletoolbox.utils;

import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.sensibletoolbox.SensibleToolboxPlugin;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.ItemUtils;
import me.desht.dhutils.MiscUtil;

/**
 * Resolves this addon's chat text for one viewer, from {@code languages/<lang>/messages.yml}.
 *
 * @implNote No send methods here: the colour belongs to {@link MiscUtil}'s own
 *           {@code errorMessage}/{@code statusMessage}/{@code alertMessage}, which already paint the
 *           whole line, so a call site resolves the text and hands it to the wrapper it always used.
 *           A mid-sentence highlight stays at the call site as {@code &6...&-}, keeping dhutils'
 *           "back to the previous colour" convention that a translator never has to know about.
 */
public final class Messages {

    private Messages() {}

    /**
     * @return the {@link Player} a message is for, or {@code null} for the console, which gets the
     *         English baseline.
     */
    @Nullable
    public static Player viewer(@Nullable CommandSender sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

    @Nonnull
    public static String get(@Nullable CommandSender sender, @Nonnull String key, @Nonnull String english) {
        return Slimefun.getAddonMessageService().getMessage(viewer(sender),
                SensibleToolboxPlugin.getInstance(), key, english);
    }

    @Nonnull
    public static String get(@Nullable CommandSender sender, @Nonnull String key, @Nonnull String english,
                             @Nonnull UnaryOperator<String> placeholders) {
        return placeholders.apply(get(sender, key, english));
    }

    /**
     * An item's display name in the viewer's own language, highlighted the way this addon
     * highlights an item mid-sentence.
     */
    @Nonnull
    public static String itemName(@Nullable CommandSender sender, @Nullable ItemStack stack) {
        return "&6" + plainItemName(sender, stack) + "&-";
    }

    /**
     * The same name without the highlight, for a line that colours it differently.
     */
    @Nonnull
    public static String plainItemName(@Nullable CommandSender sender, @Nullable ItemStack stack) {
        if (stack == null) {
            return "";
        }
        SlimefunItem item = SlimefunItem.getByItem(stack);
        if (item == null) {
            return ItemUtils.getItemName(stack);
        }
        Player viewer = viewer(sender);
        return viewer != null ? Slimefun.getItemTranslationService().getName(viewer, item) : item.getItemName();
    }
}
