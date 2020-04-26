package de.pierreschwang.spigotlib.config;

import java.io.File;
import java.io.IOException;

public class Config {

    private final File file;

    public Config(File file) {
        this.file = file;
        file.getParentFile().mkdirs();
        try {
            if(this.file.exists() && !file.createNewFile()) {
                System.err.println("An error occurred while creating the config file!");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
