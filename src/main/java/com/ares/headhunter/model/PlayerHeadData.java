package com.ares.headhunter.model;

import java.time.Instant;
import java.util.UUID;

public record PlayerHeadData(UUID victimId, String victimName, String killerName,
                              Instant deathTime, String deathCause, HeadRarity rarity) {
}
