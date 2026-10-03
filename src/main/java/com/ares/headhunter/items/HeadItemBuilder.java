package com.ares.headhunter.items;

import com.ares.headhunter.model.HeadRarity;
import com.ares.headhunter.model.PlayerHeadData;
import com.ares.headhunter.utils.SkullUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class HeadItemBuilder {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    private final NamespacedKey victimKey;
    private final NamespacedKey rarityKey;
    private final NamespacedKey soulEatKey;

    public HeadItemBuilder(JavaPlugin plugin) {
        this.victimKey = new NamespacedKey(plugin, "head_victim");
        this.rarityKey = new NamespacedKey(plugin, "head_rarity");
        this.soulEatKey = new NamespacedKey(plugin, "head_soul_eat");
    }

    public ItemStack build(OfflinePlayer victim, PlayerHeadData data) {
        ItemStack skull = SkullUtil.buildSkullWithSkin(victim);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta == null) {
            return skull;
        }

        HeadRarity rarity = data.rarity();

        Component displayName = Component.text(data.victimName() + "'in Kafası")
                .color(rarity.getColor())
                .decorate(rarity.getDecorations())
                .decoration(TextDecoration.ITALIC, false);
        meta.displayName(displayName);

        meta.lore(buildLore(data));

        meta.getPersistentDataContainer().set(victimKey, PersistentDataType.STRING, data.victimId().toString());
        meta.getPersistentDataContainer().set(rarityKey, PersistentDataType.STRING, rarity.name());
        meta.getPersistentDataContainer().set(soulEatKey, PersistentDataType.BYTE, (byte) 1);

        skull.setItemMeta(meta);
        return skull;
    }

    private List<Component> buildLore(PlayerHeadData data) {
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(line("Nadirlik", data.rarity().getDisplayName(), data.rarity().getColor()));
        lore.add(line("Öldüren", data.killerName(), NamedTextColor.GREEN));
        lore.add(line("Ölen", data.victimName(), NamedTextColor.RED));
        lore.add(line("Tarih", DATE_FORMAT.format(data.deathTime()), NamedTextColor.GRAY));
        lore.add(line("Sebep", data.deathCause(), NamedTextColor.GRAY));
        lore.add(Component.empty());
        lore.add(Component.text("Sağ tıkla: Ruh Sömürme")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, true));
        return lore;
    }

    private Component line(String label, String value, TextColor valueColor) {
        return Component.text(label + ": ", NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text(value, valueColor));
    }

    public NamespacedKey getVictimKey() {
        return victimKey;
    }

    public NamespacedKey getRarityKey() {
        return rarityKey;
    }

    public NamespacedKey getSoulEatKey() {
        return soulEatKey;
    }
}
