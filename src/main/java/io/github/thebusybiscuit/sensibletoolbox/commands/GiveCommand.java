package io.github.thebusybiscuit.sensibletoolbox.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.sensibletoolbox.api.SensibleToolbox;
import io.github.thebusybiscuit.sensibletoolbox.api.items.BaseSTBItem;
import io.github.thebusybiscuit.sensibletoolbox.utils.Messages;
import io.github.thebusybiscuit.sensibletoolbox.utils.STBUtil;
import me.desht.dhutils.MiscUtil;
import me.desht.dhutils.commands.AbstractCommand;

public class GiveCommand extends AbstractCommand {

    public GiveCommand() {
        super("stb give", 1);
        setPermissionNode("stb.commands.give");
        setUsage("/<command> give <item-name> [<amount>] [<player-name>]");
        setQuotedArgs(true);
    }

    @Override
    public boolean execute(Plugin plugin, CommandSender sender, String[] args) {
        Player target;
        int amount = 1;

        if (args.length >= 3) {
            target = Bukkit.getPlayer(args[2]);

            if (target == null) {
                MiscUtil.errorMessage(sender, Messages.get(sender, "command.give-invalid-player", "%name% is not a valid Player or not online!",
                        line -> line.replace("%name%", args[2])));
                return true;
            }
        } else {
            if (args.length >= 2) {
                if (STBUtil.isNumeric(args[1])) {
                    amount = Integer.parseInt(args[1]);
                } else {
                    MiscUtil.errorMessage(sender, Messages.get(sender, "command.give-invalid-amount", "%amount% is not a valid amount!",
                            line -> line.replace("%amount%", args[1])));
                    return true;
                }
            }

            if (!(sender instanceof Player)) {
                MiscUtil.errorMessage(sender, Messages.get(sender, "command.player-only",
                    "This command can't be run from the console."));
                return true;
            }

            target = (Player) sender;
        }

        String id = args[0].replace(" ", "").toLowerCase(Locale.ROOT);
        BaseSTBItem item = SensibleToolbox.getItemRegistry().getItemById(id);
        Validate.notNull(item, "Unknown SensibleToolbox item: " + args[0]);
        target.getInventory().addItem(item.toItemStack(amount));
        String given = Messages.get(target, "command.give-received", "You received %amount% x %item%.");
        MiscUtil.statusMessage(target, given.replace("%amount%", String.valueOf(amount))
                .replace("%item%", Messages.itemName(target, item.toItemStack())));
        return true;
    }

    @Override
    public List<String> onTabComplete(Plugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return getItemCompletions(plugin, sender, args[0]);
        } else if (args.length == 2) {
            // This will list all online Players
            return null;
        } else {
            return noCompletions(sender);
        }
    }

    protected List<String> getItemCompletions(Plugin plugin, CommandSender sender, String prefix) {
        List<String> res = new ArrayList<>();

        for (String item : SensibleToolbox.getItemRegistry().getItemIds()) {
            if (item.startsWith(prefix)) {
                res.add(item);
            }
        }

        return getResult(res, sender, true);
    }
}

