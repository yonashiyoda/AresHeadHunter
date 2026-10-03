package com.ares.headhunter.commands;

import com.ares.headhunter.managers.BountyManager;
import com.ares.headhunter.managers.ConfigManager;
import com.ares.headhunter.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class BountyCommand implements CommandExecutor {

    private final BountyManager bountyManager;
    private final ConfigManager configManager;

    public BountyCommand(BountyManager bountyManager, ConfigManager configManager) {
        this.bountyManager = bountyManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Bu komut yalnızca oyuncular tarafından kullanılabilir.");
            return true;
        }

        if (!player.hasPermission("aresheadhunter.bounty")) {
            player.sendMessage(MessageUtil.parse(configManager.getPrefix() + configManager.getMessage("no-permission")));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(MessageUtil.parse(configManager.getPrefix() + "<gray>Kullanım: /bounty <oyuncu> <miktar>"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage(MessageUtil.parse(configManager.getPrefix() + "<red>Oyuncu bulunamadı."));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(MessageUtil.parse(configManager.getPrefix() + "<red>Geçersiz miktar."));
            return true;
        }

        BountyManager.BountyResult result = bountyManager.placeBounty(player, target, amount);

        switch (result) {
            case SUCCESS -> {
                String message = configManager.getMessage("bounty-set")
                        .replace("{target}", String.valueOf(target.getName()))
                        .replace("{amount}", String.valueOf((long) amount))
                        .replace("{symbol}", configManager.getCurrencySymbol());
                player.sendMessage(MessageUtil.parse(configManager.getPrefix() + message));
            }
            case TOO_SMALL -> player.sendMessage(MessageUtil.parse(configManager.getPrefix()
                    + "<red>Minimum ödül miktarı " + (long) configManager.getMinBountyAmount() + configManager.getCurrencySymbol()));
            case INSUFFICIENT_FUNDS -> player.sendMessage(MessageUtil.parse(configManager.getPrefix()
                    + configManager.getMessage("insufficient-funds")));
            case NO_ECONOMY -> player.sendMessage(MessageUtil.parse(configManager.getPrefix()
                    + configManager.getMessage("economy-missing")));
        }

        return true;
    }
}
