package com.ares.headhunter.managers;

import com.ares.headhunter.model.HeadRarity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public boolean isHeadDropEnabled() {
        return config.getBoolean("head-drop.enabled", true);
    }

    public double getHeadDropChance() {
        return config.getDouble("head-drop.chance", 1.0);
    }

    public boolean requirePvp() {
        return config.getBoolean("head-drop.require-pvp", true);
    }

    public HeadRarity.RarityThresholds getRarityThresholds() {
        return new HeadRarity.RarityThresholds(
                config.getInt("rarity.thresholds.rare-streak", 3),
                config.getInt("rarity.thresholds.epic-streak", 7),
                config.getInt("rarity.thresholds.legendary-streak", 12),
                config.getBoolean("rarity.value-bonus.enabled", true),
                config.getInt("rarity.value-bonus.epic-item-value", 25)
        );
    }

    public boolean isSoulEatEnabled() {
        return config.getBoolean("soul-eat.enabled", true);
    }

    public int getSoulEatDurationSeconds() {
        return config.getInt("soul-eat.duration-seconds", 10);
    }

    public int getSoulEatSpeedAmplifier() {
        return config.getInt("soul-eat.speed-amplifier", 1);
    }

    public int getSoulEatRegenAmplifier() {
        return config.getInt("soul-eat.regeneration-amplifier", 1);
    }

    public int getSoulEatCooldownSeconds() {
        return config.getInt("soul-eat.cooldown-seconds", 30);
    }

    public String getCurrencySymbol() {
        return config.getString("bounty.currency-symbol", "₺");
    }

    public double getMinBountyAmount() {
        return config.getDouble("bounty.min-amount", 100);
    }

    public double getBigBountyThreshold() {
        return config.getDouble("bounty.big-threshold", 10000);
    }

    public String getStorageFileName() {
        return config.getString("bounty.storage-file", "bounties.yml");
    }

    public String getMessage(String path) {
        return config.getString("messages." + path, "");
    }

    public String getPrefix() {
        return getMessage("prefix");
    }
}
