package de.pierreschwang.spigotlib.lang;

import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import org.bukkit.ChatColor;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LanguageHandler {

    private final Map<String, Language> languages = new HashMap<>();
    private final AbstractJavaPlugin<?> plugin;

    public LanguageHandler(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
        final File languageFolder = new File(plugin.getDataFolder() + "/lang");
        languageFolder.mkdirs();
        copyFromResources(languageFolder.toPath());
        loadLanguages(languageFolder);
    }

    private void loadLanguages(File languageFolder) {
        File[] files = languageFolder.listFiles((dir, name) -> name.endsWith(".properties"));
        if(files == null)
            return;
        for (File file : files) {
            try(final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Properties properties = new Properties();
                properties.load(reader);
                final Map<String, String> translations = new HashMap<>();
                for (String key : properties.stringPropertyNames()) {
                    translations.put(key, ChatColor.translateAlternateColorCodes('&', properties.getProperty(key)));
                }
                String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                languages.put(fileName, new Language(fileName, translations));
                plugin.getLogger().info("[+] Loaded language " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyFromResources(Path target) {
        try {
            final Path path = FileSystems.newFileSystem(getClass().getResource("").toURI(), Collections.emptyMap()).getPath("/lang");
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!file.toString().endsWith(".properties"))
                        return FileVisitResult.CONTINUE;
                    Path dest = target.resolve(path.relativize(file).toString());
                    if (!dest.toFile().exists()) {
                        Files.copy(file, dest);
                    }
                    try (final BufferedReader reader = Files.newBufferedReader(file)) {
                        synchronize(reader, dest);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void synchronize(BufferedReader template, Path current) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(current.toFile(), true));
             final BufferedReader reader = new BufferedReader(new FileReader(current.toFile()))) {
            final Properties properties = new Properties();
            properties.load(template);
            final Properties curr = new Properties();
            curr.load(reader);
            // Update current file (e.g. new locales)
            for (String key : properties.stringPropertyNames()) {
                if (curr.containsKey(key))
                    continue;
                writer.newLine();
                writer.write(key + " = " + properties.getProperty(key));
                writer.newLine();
                curr.setProperty(key, properties.getProperty(key));
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String translate(String language, String key, Object... params) {
        return languages.getOrDefault(language, languages.get("en_US")).translate(key, params);
    }

}