package com.ares.headhunter.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.logging.Level;

public final class BountyStorage {

    private final JavaPlugin plugin;
    private final File file;
    private final Executor asyncExecutor;
    private final Map<UUID, Double> cache = new ConcurrentHashMap<>();

    public BountyStorage(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.asyncExecutor = command -> plugin.getServer().getScheduler().runTaskAsynchronously(plugin, command);
    }

    public CompletableFuture<Void> loadAsync() {
        return CompletableFuture.runAsync(() -> {
            if (!file.exists()) {
                return;
            }
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            for (String key : yaml.getKeys(false)) {
                try {
                    UUID id = UUID.fromString(key);
                    cache.put(id, yaml.getDouble(key));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }, asyncExecutor);
    }

    public CompletableFuture<Void> saveAsync() {
        Map<UUID, Double> snapshot = Map.copyOf(cache);
        return CompletableFuture.runAsync(() -> {
            YamlConfiguration yaml = new YamlConfiguration();
            snapshot.forEach((id, amount) -> yaml.set(id.toString(), amount));
            try {
                yaml.save(file);
            } catch (IOException exception) {
                plugin.getLogger().log(Level.WARNING, "Ödül verisi kaydedilemedi", exception);
            }
        }, asyncExecutor);
    }

    public double getBounty(UUID playerId) {
        return cache.getOrDefault(playerId, 0.0);
    }

    public void addBounty(UUID playerId, double amount) {
        cache.merge(playerId, amount, Double::sum);
    }

    public double clearBounty(UUID playerId) {
        Double removed = cache.remove(playerId);
        return removed == null ? 0.0 : removed;
    }

    public Map<UUID, Double> snapshot() {
        return Map.copyOf(cache);
    }
}
