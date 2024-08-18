package de.joekawum.report;

import de.joekawum.report.commands.*;
import de.joekawum.report.manager.ReportManager;
import net.md_5.bungee.api.plugin.Plugin;

public final class Manager extends Plugin { // TODO: 14.08.24 add mysql + add to bungeesystem in velocity + add report core for teleport

    private static Manager instance; // TODO: 14.08.24 add join listener for notification if theres any reports or if youre loggend in
    private ReportManager reportManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        this.reportManager = new ReportManager();

        getProxy().getPluginManager().registerCommand(this, new ReportCommand());
        getProxy().getPluginManager().registerCommand(this, new ReportListCommand());
        getProxy().getPluginManager().registerCommand(this, new ReportTeleportCommand());
        getProxy().getPluginManager().registerCommand(this, new ReportAcceptCommand());
        getProxy().getPluginManager().registerCommand(this, new ReportDenyCommand());
        getProxy().getPluginManager().registerCommand(this, new NotifyCommand());

        getProxy().registerChannel("report:teleport");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Manager instance() {
        return instance;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }
}
