package de.joekawum.report.commands;

import de.joekawum.report.manager.Report;
import de.joekawum.report.manager.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class ReportDenyCommand extends Command {
    public ReportDenyCommand() {
        super("reportdeny", "blockheaven.report.deny", "denyreport");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            System.out.println("You must be a player!");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if(args.length == 1) {
            String id = args[0];
            for (UUID uuid : ReportManager.reportCache.keySet()) {
                for (Report report : ReportManager.reportCache.get(uuid)) {
                    if(report.getId().equals(id)) {
                        player.sendMessage("§cReport abgelehnt!");

                        ReportManager.reportCache.get(uuid).forEach(r -> {
                            ProxiedPlayer sender = r.getSender();
                            if(sender != null && sender.isConnected())
                                sender.sendMessage("§cDein Report wurde abgelehnt und der Spieler NICHT bestraft §7(§e" + r.getId() + "§7)§a.");
                        });

                        ReportManager.reportCache.remove(uuid);

                        // TODO: 14.08.24 improve and finish
                        return;
                    }
                }
            }
            player.sendMessage("§cInvalid report-id!");
        } else
            player.sendMessage("§cBitte benutze: §7/reportdeny <id>");
    }
}
