package com.ares.headhunter.listeners;

import com.ares.headhunter.managers.BountyManager;
import com.ares.headhunter.managers.HeadManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class PlayerDeathListener implements Listener {

    private final HeadManager headManager;
    private final BountyManager bountyManager;

    public PlayerDeathListener(HeadManager headManager, BountyManager bountyManager) {
        this.headManager = headManager;
        this.bountyManager = bountyManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null) {
            return;
        }

        if (bountyManager.getBounty(victim.getUniqueId()) > 0) {
            bountyManager.claimBounty(killer, victim);
        }

        if (!headManager.shouldDropHead()) {
            return;
        }

        EntityDamageEvent.DamageCause cause = victim.getLastDamageCause() != null
                ? victim.getLastDamageCause().getCause()
                : EntityDamageEvent.DamageCause.CUSTOM;

        event.getDrops().add(headManager.createHead(killer, victim, cause));
    }
}
