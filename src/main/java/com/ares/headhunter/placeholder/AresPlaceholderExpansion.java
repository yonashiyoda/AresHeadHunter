package com.ares.headhunter.placeholder;

import com.ares.headhunter.managers.BountyManager;
import com.ares.headhunter.managers.KillStreakManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class AresPlaceholderExpansion extends PlaceholderExpansion {

    private final KillStreakManager killStreakManager;
    private final BountyManager bountyManager;

    public AresPlaceholderExpansion(KillStreakManager killStreakManager, BountyManager bountyManager) {
        this.killStreakManager = killStreakManager;
        this.bountyManager = bountyManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "aresheadhunter";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Ares";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        return switch (params.toLowerCase()) {
            case "killstreak" -> String.valueOf(killStreakManager.getStreak(player.getUniqueId()));
            case "bounty" -> String.valueOf((long) bountyManager.getBounty(player.getUniqueId()));
            default -> null;
        };
    }
}
