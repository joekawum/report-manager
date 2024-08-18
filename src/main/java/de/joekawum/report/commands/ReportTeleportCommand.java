package de.joekawum.report.commands;

import de.joekawum.report.Manager;
import de.joekawum.report.manager.Report;
import de.joekawum.report.manager.ReportManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.w3c.dom.Text;

import java.util.UUID;

public class ReportTeleportCommand extends Command {
    public ReportTeleportCommand() {
        super("reportteleport", "blockheaven.report.teleport", "reporttp");
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
                        ProxiedPlayer suspect = ProxyServer.getInstance().getPlayer(report.getSuspect());
                        if(suspect == null || !suspect.isConnected()) {
                            player.sendMessage("§cDer gemeldete Spieler ist nicht mehr online!");
                            //player.sendMessage("§c§oclick §ahere §c§oto delete report");
                            TextComponent textComponent = new TextComponent("here");
                            textComponent.setColor(ChatColor.GREEN);
                            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportdeny " + id));
                            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§xdelete").create()));

                            TextComponent comp1 = new TextComponent("click ");
                            comp1.setColor(ChatColor.RED);
                            comp1.setItalic(true);

                            TextComponent comp2 = new TextComponent(" to delete report");
                            comp2.setColor(ChatColor.RED);
                            comp2.setItalic(true);

                            textComponent.addExtra(comp2);
                            comp1.addExtra(textComponent);

                            player.sendMessage(comp1);

                            return;
                        }
                        if(!player.getServer().getInfo().getName().equals(suspect.getServer().getInfo().getName()))
                            player.connect(suspect.getServer().getInfo());

                        // TODO: 13.08.24 teleport mechanic paperserver via pl messaging
                        Manager.instance().getReportManager().sendTeleportData(player, suspect.getUniqueId());

                        player.sendMessage("§7Du bist nun auf §e" + player.getServer().getInfo().getName());

                        player.sendMessage("§aReport ACCEPT");
                        player.sendMessage("§cReport DENY");
                        player.sendMessage("§7§othe player will not be automatically banned if the report has been accepted");
                        return;
                    }
                }
            }
            player.sendMessage("§cInvalid report-id!");
        } else
            player.sendMessage("§cBitte benutze: §7/reportteleport <id>");
    }
}
