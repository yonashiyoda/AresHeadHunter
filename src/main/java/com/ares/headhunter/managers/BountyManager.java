package com.ares.headhunter.managers;

import com.ares.headhunter.storage.BountyStorage;
import com.ares.headhunter.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public final class BountyManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final EconomyManager economyManager;
    private final BountyStorage storage;

    public BountyManager(JavaPlugin plugin, ConfigManager configManager, EconomyManager economyManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.economyManager = economyManager;
        this.storage = new BountyStorage(plugin, configManager.getStorageFileName());
        this.storage.loadAsync();
    }

    public BountyResult placeBounty(Player source, OfflinePlayer target, double amount) {
        if (!economyManager.isAvailable()) {
            return BountyResult.NO_ECONOMY;
        }
        if (amount < configManager.getMinBountyAmount()) {
            return BountyResult.TOO_SMALL;
        }
        if (!economyManager.withdraw(source, amount)) {
            return BountyResult.INSUFFICIENT_FUNDS;
        }

        storage.addBounty(target.getUniqueId(), amount);
        storage.saveAsync();
        announce(target, amount);
        return BountyResult.SUCCESS;
    }

    public double getBounty(UUID playerId) {
        return storage.getBounty(playerId);
    }

    public double claimBounty(Player killer, OfflinePlayer victim) {
        double amount = storage.clearBounty(victim.getUniqueId());
        if (amount <= 0) {
            return 0;
        }
        economyManager.deposit(killer, amount);
        storage.saveAsync();

        String message = configManager.getMessage("bounty-claimed")
                .replace("{killer}", killer.getName())
                .replace("{target}", String.valueOf(victim.getName()))
                .replace("{amount}", String.valueOf((long) amount))
                .replace("{symbol}", configManager.getCurrencySymbol());
        Bukkit.broadcast(MessageUtil.parse(configManager.getPrefix() + message));
        return amount;
    }

    public Map<UUID, Double> topBounties() {
        return storage.snapshot();
    }

    private void announce(OfflinePlayer target, double amount) {
        String symbol = configManager.getCurrencySymbol();
        String targetName = String.valueOf(target.getName());
        String amountText = String.valueOf((long) amount);

        if (amount >= configManager.getBigBountyThreshold()) {
            String titleText = configManager.getMessage("bounty-big-title");
            String subtitleText = configManager.getMessage("bounty-big-subtitle")
                    .replace("{target}", targetName)
                    .replace("{amount}", amountText)
                    .replace("{symbol}", symbol);

            Title title = Title.title(
                    MessageUtil.parse(titleText),
                    MessageUtil.parse(subtitleText),
                    Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(4), Duration.ofMillis(500))
            );
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.showTitle(title);
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 0.6f, 1.2f);
            });
        }

        String chatMessage = configManager.getMessage("bounty-chat-announce")
                .replace("{target}", targetName)
                .replace("{amount}", amountText)
                .replace("{symbol}", symbol);
        Bukkit.broadcast(MessageUtil.parse(configManager.getPrefix() + chatMessage));
    }

    public enum BountyResult {
        SUCCESS, TOO_SMALL, INSUFFICIENT_FUNDS, NO_ECONOMY
    }
}
