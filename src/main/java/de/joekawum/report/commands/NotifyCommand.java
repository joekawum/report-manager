package de.joekawum.report.commands;

import de.joekawum.report.manager.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NotifyCommand extends Command { // TODO: 14.08.24 add bungeesystem
    public NotifyCommand() {
        super("notify", "blockheaven.report.notify");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            System.out.println("You must be a player!");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("report")) {
                if (ReportManager.reportNotify.contains(player)) {
                    ReportManager.reportNotify.remove(player);
                    player.sendMessage("§7Du bekommst nun §cKEINE §7reports mehr gemeldet!");
                } else {
                    ReportManager.reportNotify.add(player);
                    player.sendMessage("§7Du bekommst nun §awieder §7reports gemeldet!");
                }
            } else
                player.sendMessage("§cBitte benutze: §7/notify <report§7§o/ban/mute§7>");
        } else
            player.sendMessage("§cBitte benutze: §7/notify <report§7§o/ban/mute§7>");
    }
}
