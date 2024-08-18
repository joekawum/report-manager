package de.joekawum.report.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Report {

    private final ProxiedPlayer sender;
    private final UUID suspect;
    private final ReportManager.Reasons reason;
    private final long timestamp;
    private final int number;
    private final String id, server;

    public Report(ProxiedPlayer sender, UUID suspect, ReportManager.Reasons reason, String server, long timestamp) {
        this.sender = sender;
        this.suspect = suspect;
        this.reason = reason;
        this.server = server;
        this.timestamp = timestamp;

        /*
        if(ReportManager.reportCache.isEmpty())
            this.number = 1;
        else {
            int current = 0;
            for (Report value : ReportManager.reportCache.values()) {
                if(value.getNumber() > current)
                    current = value.getNumber();
            }
            this.number = current+1;
        }
         */

        ReportManager.totalReportAmount++;
        this.number = ReportManager.totalReportAmount;

        StringBuilder current = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int random = ThreadLocalRandom.current().nextInt(2);
            char c = ReportManager.ALPHABET.charAt(ThreadLocalRandom.current().nextInt(ReportManager.ALPHABET.length()));
            String s = String.valueOf(c);
            if(random > 0)
                current.append(s);
            else
                current.append(s.toUpperCase());
        }
        this.id = String.valueOf(ReportManager.CODECHAR.charAt(reason.getId()-1)) + current + "-" + (this.number < 10 ? "000" + this.number : (this.number < 100 ? "00" + this.number : (this.number < 1000 ? "0" + this.number : this.number)));

        if(ReportManager.reportCache.containsKey(suspect))
            ReportManager.reportCache.get(suspect).add(this);
        else
            ReportManager.reportCache.put(suspect, new LinkedList<>(Collections.singletonList(this)));
    }

    public ProxiedPlayer getSender() {
        return sender;
    }

    public UUID getSuspect() {
        return suspect;
    }

    public ReportManager.Reasons getReason() {
        return reason;
    }

    public String getServer() {
        return server;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }
}
