package de.joekawum.report.commands;

import de.joekawum.report.manager.Report;
import de.joekawum.report.manager.ReportManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class ReportCommand extends Command {
    public ReportCommand() {
        super("report");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            System.out.println("You have to be a player!");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if(args.length == 2) {
            ProxiedPlayer suspect = ProxyServer.getInstance().getPlayer(args[0]);
            if(suspect == null || !suspect.isConnected()) {
                player.sendMessage("§cSuspect isn't online!");
                return;
            }
            
            if(ReportManager.reportCache.containsKey(suspect.getUniqueId())) {
                LinkedList<Report> reports = ReportManager.reportCache.get(suspect.getUniqueId());
                for (Report report : reports) {
                    if(report.getSender().getName().equals(player.getName())) {
                        player.sendMessage("§7Du hast den Spieler bereits gemeldet!");
                        return;
                    }
                }
            }
            
            for (ReportManager.Reasons value : ReportManager.Reasons.values()) {
                if(args[1].equalsIgnoreCase(value.getName()) | args[1].equalsIgnoreCase("" + value.getId())) {
                    Report report = new Report(player, suspect.getUniqueId(), value, player.getServer().getInfo().getName(), System.currentTimeMillis());
                    player.sendMessage("§aReport gesendet!");

                    for (ProxiedPlayer proxiedPlayer : ReportManager.reportNotify) {
                        // TODO: 14.08.24 design
                        proxiedPlayer.sendMessage("§4§lEINGEHENDER REPORT");
                        proxiedPlayer.sendMessage("§7UUID: " + report.getSuspect());
                        proxiedPlayer.sendMessage("§7Reason: " + report.getReason().getName());
                        proxiedPlayer.sendMessage("§7Sender: " + report.getSender().getName());
                        proxiedPlayer.sendMessage("§7ID: " + report.getId());
                        proxiedPlayer.sendMessage("§7When: " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(report.getTimestamp()));
                    }
                    return;
                }
            }
            player.sendMessage("§cPlease use a valid reason!");
        } else {
            player.sendMessage("§cReport reasons:");
            for (ReportManager.Reasons value : ReportManager.Reasons.values()) {
                player.sendMessage("§7- §e" + value.getId() + " §7| §e" + value.getName());
            }
            player.sendMessage("§cBitte benutze: §7/report <Spieler> <Id>");
        }
    }
}
