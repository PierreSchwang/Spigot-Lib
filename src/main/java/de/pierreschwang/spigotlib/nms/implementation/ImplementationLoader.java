package de.pierreschwang.spigotlib.nms.implementation;

import com.google.common.reflect.ClassPath;
import de.pierreschwang.spigotlib.AbstractJavaPlugin;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

/**
 * Loads the correct NMS implementations based of the version the server is running
 */
public class ImplementationLoader {

    private static final String basePackage = ImplementationLoader.class.getPackage().getName() + ".implementations";

    private final AbstractJavaPlugin<?> plugin;
    private final String version;
    private Map<Class<? extends Implementation>, Implementation> loadedImplementations;

    public ImplementationLoader(AbstractJavaPlugin<?> plugin) {
        this.plugin = plugin;
        this.version = getRunningVersion();
        this.loadImplementations();
    }

    public <T extends Implementation> T getImplementation(Class<T> baseClass) {
        return (T) loadedImplementations.get(baseClass);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void loadImplementations() {
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(Thread.currentThread().getContextClassLoader())
                    .getTopLevelClasses(basePackage + "." + this.version)) {
                Class<?> clazz = classInfo.load();
                Class<? extends Implementation> baseImplementation = getBaseImplementation(clazz).orElse(null);
                if (baseImplementation == null) // Helper classes etc don't implement the Implementation interface.
                    continue;
                Object implementation = clazz.getConstructor().newInstance();
                loadedImplementations.put(baseImplementation, (Implementation) implementation);
            }
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        this.plugin.getLogger().info(String.format("Loaded %s implementations for version %s", this.loadedImplementations.size(), version));
    }

    private Optional<Class<? extends Implementation>> getBaseImplementation(Class<?> clazz) {
        for (Class<?> anInterface : clazz.getInterfaces()) {
            if (Implementation.class.isAssignableFrom(anInterface)) {
                return Optional.of((Class<? extends Implementation>) anInterface);
            }
        }
        return Optional.empty();
    }

    private String getRunningVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}
