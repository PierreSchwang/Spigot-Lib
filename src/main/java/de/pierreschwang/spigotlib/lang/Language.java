package de.pierreschwang.spigotlib.lang;

import java.text.MessageFormat;
import java.util.Map;

public class Language {

    private final String name;
    private final Map<String, String> translations;

    public Language(String name, Map<String, String> translations) {
        this.name = name;
        this.translations = translations;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public String translate(String key, Object... params) {
        String value = translations.getOrDefault(key, key);
        value = value.replaceAll("%prefix%", translations.get("prefix"));
        return MessageFormat.format(value, params);
    }

}