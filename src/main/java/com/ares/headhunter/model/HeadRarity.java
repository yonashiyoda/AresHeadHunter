package com.ares.headhunter.model;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum HeadRarity {

    COMMON("Yaygın Kafa", TextColor.color(170, 170, 170), false),
    RARE("Nadir Kafa", TextColor.color(85, 170, 255), false),
    EPIC("Epik Kafa", TextColor.color(170, 0, 255), false),
    LEGENDARY("Efsanevi Kafa", TextColor.color(255, 170, 0), true);

    private final String displayName;
    private final TextColor color;
    private final boolean goldTrim;

    HeadRarity(String displayName, TextColor color, boolean goldTrim) {
        this.displayName = displayName;
        this.color = color;
        this.goldTrim = goldTrim;
    }

    public String getDisplayName() {
        return goldTrim ? "Altın Kaplama " + displayName : displayName;
    }

    public TextColor getColor() {
        return color;
    }

    public TextDecoration[] getDecorations() {
        return this == LEGENDARY
                ? new TextDecoration[]{TextDecoration.BOLD}
                : new TextDecoration[0];
    }

    public static HeadRarity fromScore(int killStreak, int itemValueScore, RarityThresholds thresholds) {
        HeadRarity base;
        if (killStreak >= thresholds.legendaryStreak()) {
            base = LEGENDARY;
        } else if (killStreak >= thresholds.epicStreak()) {
            base = EPIC;
        } else if (killStreak >= thresholds.rareStreak()) {
            base = RARE;
        } else {
            base = COMMON;
        }

        if (thresholds.valueBonusEnabled() && itemValueScore >= thresholds.epicItemValue() && base.ordinal() < LEGENDARY.ordinal()) {
            return values()[base.ordinal() + 1];
        }
        return base;
    }

    public record RarityThresholds(int rareStreak, int epicStreak, int legendaryStreak,
                                    boolean valueBonusEnabled, int epicItemValue) {
    }
}
