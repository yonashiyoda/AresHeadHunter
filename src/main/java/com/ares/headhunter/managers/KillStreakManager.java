package com.ares.headhunter.managers;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class KillStreakManager {

    private final ConcurrentHashMap<UUID, AtomicInteger> streaks = new ConcurrentHashMap<>();

    public int increment(UUID playerId) {
        return streaks.computeIfAbsent(playerId, id -> new AtomicInteger()).incrementAndGet();
    }

    public void reset(UUID playerId) {
        streaks.remove(playerId);
    }

    public int getStreak(UUID playerId) {
        AtomicInteger counter = streaks.get(playerId);
        return counter == null ? 0 : counter.get();
    }
}
