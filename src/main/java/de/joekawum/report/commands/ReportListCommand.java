package de.joekawum.report.commands;

import de.joekawum.report.manager.Report;
import de.joekawum.report.manager.ReportManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

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

        for (UUID uuid : ReportManager.reportCache.keySet()) {
            LinkedList<Report> reports = ReportManager.reportCache.get(uuid);
            Report first = reports.getFirst();
            ProxiedPlayer suspect = ProxyServer.getInstance().getPlayer(uuid);
            if(suspect == null) return;
            player.sendMessage(" ");
            player.sendMessage("§4§lREPORT §8>> §e" + first.getReason().getName() + " §8>> §e" + first.getServer() + " §8>> " + (suspect.isConnected() ? "§a" : "§c") + suspect.getName() + " §8[§7" + first.getId() + "§8]");
            TextComponent teleportButton = new TextComponent("[TELEPORT]");
            teleportButton.setColor(ChatColor.GREEN);
            teleportButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportteleport " + first.getId()));
            teleportButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("click here to teleport").create()));

            TextComponent detailsButton = new TextComponent("[DETAILS]");
            detailsButton.setColor(ChatColor.AQUA);
            detailsButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportinfo " + first.getId()));
            detailsButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("click here for more details").create()));
            detailsButton.addExtra(new TextComponent(" "));
            detailsButton.addExtra(teleportButton);

            TextComponent prefixSymbol = new TextComponent(" >> ");
            prefixSymbol.setColor(ChatColor.DARK_GRAY);
            prefixSymbol.setBold(false);
            prefixSymbol.addExtra(detailsButton);

            TextComponent prefix = new TextComponent("REPORT");
            prefix.setColor(ChatColor.DARK_RED);
            prefix.setBold(true);
            prefix.addExtra(prefixSymbol);

            player.sendMessage(prefix);
        }
    }
}
