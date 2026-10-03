package com.ares.headhunter.managers;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class EconomyManager {

    private Economy economy;

    public EconomyManager(JavaPlugin plugin) {
        setup(plugin);
    }

    private void setup(JavaPlugin plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> provider = plugin.getServer()
                .getServicesManager()
                .getRegistration(Economy.class);
        if (provider != null) {
            this.economy = provider.getProvider();
        }
    }

    public boolean isAvailable() {
        return economy != null;
    }

    public boolean has(OfflinePlayer player, double amount) {
        return isAvailable() && economy.has(player, amount);
    }

    public boolean withdraw(OfflinePlayer player, double amount) {
        return isAvailable() && economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    public void deposit(OfflinePlayer player, double amount) {
        if (isAvailable()) {
            economy.depositPlayer(player, amount);
        }
    }

    public String format(double amount) {
        return isAvailable() ? economy.format(amount) : String.valueOf(amount);
    }
}
