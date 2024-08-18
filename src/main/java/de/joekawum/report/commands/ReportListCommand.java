package de.joekawum.report.commands;

import de.joekawum.report.manager.Report;
import de.joekawum.report.manager.ReportManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.UUID;

public class ReportListCommand extends Command {
    public ReportListCommand() {
        super("reportlist", "blockheaven.report.list", "reports");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof ProxiedPlayer)) {
            System.out.println("You must be a player");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if(ReportManager.reportCache.isEmpty()) {
            player.sendMessage("§aEs sind momentan keine Reports offen!");
            return;
        }

        player.sendMessage(" ");
        player.sendMessage("§7-----[offene Reports]-----");
        player.sendMessage(" ");
        for (UUID uuid : ReportManager.reportCache.keySet()) {
            LinkedList<Report> reports = ReportManager.reportCache.get(uuid);
            ProxiedPlayer suspect = ProxyServer.getInstance().getPlayer(uuid);
            if(suspect == null || !suspect.isConnected())
                player.sendMessage("§7Gemeldeter Spieler: §c" + suspect.getName());
            else
                player.sendMessage("§7Gemeldeter Spieler: §a" + suspect.getName());

            String reason = reports.getFirst().getReason().getName();
            for (Report report : reports) {
                if(!reason.contains(report.getReason().getName()))
                    reason += "," + report.getReason().getName();
            }
            if(reason.split(",").length > 1)
                player.sendMessage("§7Gründe: §e" + String.join(", ", reason.split(",")));
            else
                player.sendMessage("§7Grund: §e" + reason);

            ProxiedPlayer sender = reports.getFirst().getSender();
            if(sender == null || !sender.isConnected())
                player.sendMessage("§7Gemeldet von: §c" + sender.getName() + (reports.size() > 1 ? " §7§o(" + reports.size() + ")" : ""));
            else
                player.sendMessage("§7Gemeldet von: §a" + sender.getName() + (reports.size() > 1 ? " §7§o(" + reports.size() + ")" : ""));

            player.sendMessage("§7Gemeldet am: §e" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(reports.getFirst().getTimestamp()) + " Uhr");

            player.sendMessage("§7ID: §c" + reports.getFirst().getId());

            TextComponent textComponent = new TextComponent("[TELEPORT]");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportteleport " + reports.getFirst().getId()));
            textComponent.setColor(ChatColor.GREEN);

            TextComponent baseComponent = new TextComponent("PREFIX | ");
            baseComponent.addExtra(textComponent);

            player.sendMessage(baseComponent);

            player.sendMessage(" ");
            player.sendMessage("§7-----[x]-----");
            player.sendMessage(" ");
        }
    }
}
