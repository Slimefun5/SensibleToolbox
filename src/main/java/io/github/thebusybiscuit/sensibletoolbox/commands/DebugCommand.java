package io.github.thebusybiscuit.sensibletoolbox.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.sensibletoolbox.utils.Messages;

import me.desht.dhutils.Debugger;
import me.desht.dhutils.MiscUtil;
import me.desht.dhutils.commands.AbstractCommand;

public class DebugCommand extends AbstractCommand {

    public DebugCommand() {
        super("stb debug", 0, 1);
        setPermissionNode("stb.commands.debug");
        setUsage("/<command> debug [<level>]");
    }

    @Override
    public boolean execute(Plugin plugin, CommandSender sender, String[] args) {
        if (args.length >= 1) {
            int level = Integer.parseInt(args[0]);

            if (level >= 0) {
                setDebugLevel(sender, level);
            } else {
                MiscUtil.errorMessage(sender, Messages.get(sender, "command.debug-level-positive", "Debug level must be >= 0"));
            }
        } else {
            if (Debugger.getInstance().getLevel() > 0) {
                setDebugLevel(sender, 0);
            } else {
                setDebugLevel(sender, 1);
            }
        }
        return true;
    }

    private void setDebugLevel(CommandSender sender, int level) {
        Debugger.getInstance().setLevel(level);
        Debugger.getInstance().setTarget(level == 0 ? null : sender);

        if (level > 0) {
            MiscUtil.statusMessage(sender, Messages.get(sender, "command.debug-enabled", "Debugger enabled (level %level%)",
                    line -> line.replace("%level%", String.valueOf(level))));
        } else {
            MiscUtil.statusMessage(sender, Messages.get(sender, "command.debug-disabled", "Debugger disabled"));
        }
    }
}

