package com.ares.headhunter.commands;

import com.ares.headhunter.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class AdminCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public AdminCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("aresheadhunter.admin")) {
            sender.sendMessage("Bu işlem için yetkin yok.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            configManager.reload();
            sender.sendMessage("AresHeadHunter yapılandırması yeniden yüklendi.");
            return true;
        }

        sender.sendMessage("Kullanım: /aheadhunter reload");
        return true;
    }
}
