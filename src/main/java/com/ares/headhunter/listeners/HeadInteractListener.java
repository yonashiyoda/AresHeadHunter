package com.ares.headhunter.listeners;

import com.ares.headhunter.items.HeadItemBuilder;
import com.ares.headhunter.managers.BuffManager;
import com.ares.headhunter.managers.ConfigManager;
import com.ares.headhunter.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public final class HeadInteractListener implements Listener {

    private final HeadItemBuilder itemBuilder;
    private final BuffManager buffManager;
    private final ConfigManager configManager;

    public HeadInteractListener(HeadItemBuilder itemBuilder, BuffManager buffManager, ConfigManager configManager) {
        this.itemBuilder = itemBuilder;
        this.buffManager = buffManager;
        this.configManager = configManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (!configManager.isSoulEatEnabled()) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.PLAYER_HEAD || !(item.getItemMeta() instanceof SkullMeta meta)) {
            return;
        }

        Byte soulEatFlag = meta.getPersistentDataContainer().get(itemBuilder.getSoulEatKey(), PersistentDataType.BYTE);
        if (soulEatFlag == null || soulEatFlag != 1) {
            return;
        }

        event.setCancelled(true);

        if (buffManager.isOnCooldown(player.getUniqueId())) {
            long remaining = buffManager.getRemainingSeconds(player.getUniqueId());
            player.sendMessage(MessageUtil.parse(configManager.getPrefix()
                    + configManager.getMessage("soul-eat-cooldown").replace("{seconds}", String.valueOf(remaining))));
            return;
        }

        String victimName = meta.displayName() != null
                ? MessageUtil.stripToPlain(meta.displayName())
                : "Bilinmeyen";

        buffManager.applySoulEatBuff(player);
        item.subtract(1);

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1f, 0.8f);
        player.sendMessage(MessageUtil.parse(configManager.getPrefix()
                + configManager.getMessage("soul-eat-success").replace("{target}", victimName)));
    }
}
