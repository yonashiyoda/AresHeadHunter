package com.ares.headhunter.utils;

import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public final class ItemValueCalculator {

    private ItemValueCalculator() {
    }

    public static int scoreEquipment(Player player) {
        EntityEquipment equipment = player.getEquipment();
        if (equipment == null) {
            return 0;
        }

        int score = 0;
        for (ItemStack piece : equipment.getArmorContents()) {
            score += scoreItem(piece);
        }
        score += scoreItem(equipment.getItemInMainHand());
        score += scoreItem(equipment.getItemInOffHand());
        return score;
    }

    private static int scoreItem(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return 0;
        }
        int score = 2;
        for (var entry : item.getEnchantments().entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            score += (enchantment.isTreasure() ? 8 : 4) * level;
        }
        return score;
    }
}
