package com.ares.headhunter.managers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class BuffManager {

    private final ConfigManager configManager;
    private final ConcurrentHashMap<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    public BuffManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean isOnCooldown(UUID playerId) {
        Long expiry = cooldowns.get(playerId);
        return expiry != null && expiry > System.currentTimeMillis();
    }

    public long getRemainingSeconds(UUID playerId) {
        Long expiry = cooldowns.get(playerId);
        if (expiry == null) {
            return 0;
        }
        return Math.max(0, TimeUnit.MILLISECONDS.toSeconds(expiry - System.currentTimeMillis()));
    }

    public void applySoulEatBuff(Player player) {
        int durationTicks = configManager.getSoulEatDurationSeconds() * 20;
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, durationTicks, configManager.getSoulEatSpeedAmplifier(), false, true, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, durationTicks, configManager.getSoulEatRegenAmplifier(), false, true, true));

        long cooldownMillis = configManager.getSoulEatCooldownSeconds() * 1000L;
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + cooldownMillis);
    }
}
