package com.ares.headhunter.utils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

public final class SkullUtil {

    private SkullUtil() {
    }

    public static ItemStack buildSkullWithSkin(OfflinePlayer owner) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta == null) {
            return skull;
        }

        PlayerProfile profile = owner.getPlayerProfile();
        meta.setOwnerProfile(profile);
        skull.setItemMeta(meta);
        return skull;
    }
}
