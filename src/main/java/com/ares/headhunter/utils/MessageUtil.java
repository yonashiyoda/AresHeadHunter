package com.ares.headhunter.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Map;

public final class MessageUtil {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private MessageUtil() {
    }

    public static Component parse(String raw, Map<String, String> placeholders) {
        String replaced = raw;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            replaced = replaced.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return MINI.deserialize(replaced);
    }

    public static Component parse(String raw) {
        return MINI.deserialize(raw);
    }

    public static String stripToPlain(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
