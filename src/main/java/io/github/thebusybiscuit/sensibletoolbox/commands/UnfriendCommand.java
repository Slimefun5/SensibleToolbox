package io.github.thebusybiscuit.sensibletoolbox.commands;

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.sensibletoolbox.SensibleToolboxPlugin;
import io.github.thebusybiscuit.sensibletoolbox.api.FriendManager;
import io.github.thebusybiscuit.sensibletoolbox.utils.Messages;
import me.desht.dhutils.MiscUtil;

public class UnfriendCommand extends STBAbstractCommand {

    public UnfriendCommand() {
        super("stb unfriend", 1);
        setPermissionNode("stb.commands.unfriend");
        setUsage("/<command> unfriend <player-name-or-id>");
    }

    @Override
    public boolean execute(Plugin plugin, CommandSender sender, String[] args) {
        FriendManager fm = ((SensibleToolboxPlugin) plugin).getFriendManager();

        Player target = getTargetPlayer(sender, getStringOption("p"));

        UUID id = getID(args[0]);
        Validate.notNull(id, "Unknown player: " + args[0]);
        fm.removeFriend(target.getUniqueId(), id);
        MiscUtil.statusMessage(sender, Messages.get(sender, "command.friend-removed", "%player% is no longer friends with %friend%",
                line -> line.replace("%player%", target.getName()).replace("%friend%", args[0])));

        return true;
    }
}

