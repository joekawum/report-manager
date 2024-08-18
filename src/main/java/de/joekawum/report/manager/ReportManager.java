package de.joekawum.report.manager;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class ReportManager {

    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz", CODECHAR = "!=@&#?";
    public static final HashMap<UUID, LinkedList<Report>> reportCache = new HashMap<>();
    public static final List<ProxiedPlayer> reportNotify = new ArrayList<>();
    public static int totalReportAmount = 0;

    public ReportManager(){
        // TODO: 13.08.24 init mysql

    }

    public void sendTeleportData(ProxiedPlayer player, UUID suspect) {
        Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers();
        if(players == null || players.isEmpty()) {
            System.out.println("[Report] tried sending data while network was empty [" + player + "," + suspect + "]");
            return;
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("teleportInformation");
        output.writeUTF(player.getUniqueId().toString());
        output.writeUTF(suspect.toString());

        player.getServer().getInfo().sendData("report:teleport", output.toByteArray());
    }

    public enum Reasons {

        CHEATING(1, "Hacking"),
        BUGUSING(2, "Bugusing"),
        TROLLING(3, "Trolling"),
        GRIEFING(4, "Griefing"),
        INSULT(5, "Beleidigung"),
        OTHER(6, "Sonstiges");

        Reasons(int id, String name) {
            this.id = id;
            this.name = name;
        }

        private final int id;
        private final String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}
