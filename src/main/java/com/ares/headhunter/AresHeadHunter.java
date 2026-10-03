package com.ares.headhunter;

import com.ares.headhunter.commands.AdminCommand;
import com.ares.headhunter.commands.BountyCommand;
import com.ares.headhunter.listeners.HeadInteractListener;
import com.ares.headhunter.listeners.PlayerDeathListener;
import com.ares.headhunter.managers.BountyManager;
import com.ares.headhunter.managers.BuffManager;
import com.ares.headhunter.managers.ConfigManager;
import com.ares.headhunter.managers.EconomyManager;
import com.ares.headhunter.managers.HeadManager;
import com.ares.headhunter.managers.KillStreakManager;
import com.ares.headhunter.placeholder.AresPlaceholderExpansion;
import org.bukkit.plugin.java.JavaPlugin;

public final class AresHeadHunter extends JavaPlugin {

    private ConfigManager configManager;
    private EconomyManager economyManager;
    private KillStreakManager killStreakManager;
    private HeadManager headManager;
    private BuffManager buffManager;
    private BountyManager bountyManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.economyManager = new EconomyManager(this);
        this.killStreakManager = new KillStreakManager();
        this.headManager = new HeadManager(this, configManager, killStreakManager);
        this.buffManager = new BuffManager(configManager);
        this.bountyManager = new BountyManager(this, configManager, economyManager);

        registerListeners();
        registerCommands();
        registerPlaceholders();

        if (!economyManager.isAvailable()) {
            getLogger().warning("Vault bulunamadı; /bounty komutu ekonomi olmadan çalışmayacak.");
        }

        getLogger().info("AresHeadHunter etkinleştirildi.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AresHeadHunter devre dışı bırakıldı.");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(
                new PlayerDeathListener(headManager, bountyManager), this);
        getServer().getPluginManager().registerEvents(
                new HeadInteractListener(headManager.getItemBuilder(), buffManager, configManager), this);
    }

    private void registerCommands() {
        var bountyExecutor = new BountyCommand(bountyManager, configManager);
        var adminExecutor = new AdminCommand(configManager);

        var bountyCommand = getCommand("bounty");
        if (bountyCommand != null) {
            bountyCommand.setExecutor(bountyExecutor);
        }

        var adminCommand = getCommand("aheadhunter");
        if (adminCommand != null) {
            adminCommand.setExecutor(adminExecutor);
        }
    }

    private void registerPlaceholders() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return;
        }
        new AresPlaceholderExpansion(killStreakManager, bountyManager).register();
    }
}
