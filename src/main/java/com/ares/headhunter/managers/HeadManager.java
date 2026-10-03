package com.ares.headhunter.managers;

import com.ares.headhunter.items.HeadItemBuilder;
import com.ares.headhunter.model.HeadRarity;
import com.ares.headhunter.model.PlayerHeadData;
import com.ares.headhunter.utils.ItemValueCalculator;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;

public final class HeadManager {

    private final ConfigManager configManager;
    private final KillStreakManager killStreakManager;
    private final HeadItemBuilder itemBuilder;

    public HeadManager(JavaPlugin plugin, ConfigManager configManager, KillStreakManager killStreakManager) {
        this.configManager = configManager;
        this.killStreakManager = killStreakManager;
        this.itemBuilder = new HeadItemBuilder(plugin);
    }

    /**
     * Kafa, olum olayinin icinden (senkron) cagrilmalidir; drop listesine
     * zamaninda eklenebilmesi icin ag/disk icermeyen hafif bir islemdir.
     * Cevrimici kurban icin profil dokusu zaten istemci tarafindan
     * dogrulanmis oldugundan ek bir Mojang API cagrisi gerekmez.
     */
    public ItemStack createHead(Player killer, Player victim, EntityDamageEvent.DamageCause cause) {
        int streak = killStreakManager.increment(killer.getUniqueId());
        killStreakManager.reset(victim.getUniqueId());

        int itemScore = ItemValueCalculator.scoreEquipment(victim);
        HeadRarity rarity = HeadRarity.fromScore(streak, itemScore, configManager.getRarityThresholds());

        PlayerHeadData data = new PlayerHeadData(
                victim.getUniqueId(),
                victim.getName(),
                killer.getName(),
                Instant.now(),
                describeCause(cause),
                rarity
        );

        return itemBuilder.build(victim, data);
    }

    public boolean shouldDropHead() {
        if (!configManager.isHeadDropEnabled()) {
            return false;
        }
        return Math.random() <= configManager.getHeadDropChance();
    }

    public HeadItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    private String describeCause(EntityDamageEvent.DamageCause cause) {
        return switch (cause) {
            case ENTITY_ATTACK -> "Yakin dovus";
            case PROJECTILE -> "Ok/mermi";
            case FIRE, FIRE_TICK -> "Yanarak";
            case FALL -> "Duserek";
            case LAVA -> "Lavda";
            case VOID -> "Boslukta";
            case MAGIC -> "Buyu";
            case EXPLOSION -> "Patlama";
            default -> "Bilinmeyen";
        };
    }
}
