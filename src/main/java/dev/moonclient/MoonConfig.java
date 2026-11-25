package dev.moonclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class MoonConfig {
    private static final Path FILE = Paths.get("config", "moonclient.properties");
    public static boolean keystrokes = true, cps = true, fps = true, ping = true;
    public static boolean armor = true, coordinates = false;
    public static String skinUrl = "";

    private MoonConfig() {}

    public static void load() {
        Properties p = new Properties();
        try {
            if (Files.exists(FILE)) {
                try (InputStream in = Files.newInputStream(FILE)) { p.load(in); }
            }
            keystrokes = Boolean.parseBoolean(p.getProperty("keystrokes", "true"));
            cps = Boolean.parseBoolean(p.getProperty("cps", "true"));
            fps = Boolean.parseBoolean(p.getProperty("fps", "true"));
            ping = Boolean.parseBoolean(p.getProperty("ping", "true"));
            armor = Boolean.parseBoolean(p.getProperty("armor", "true"));
            coordinates = Boolean.parseBoolean(p.getProperty("coordinates", "false"));
            skinUrl = p.getProperty("skinUrl", "");
        } catch (IOException e) {
            System.err.println("[Moon Client] Config load failed: " + e.getMessage());
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("keystrokes", Boolean.toString(keystrokes));
        p.setProperty("cps", Boolean.toString(cps));
        p.setProperty("fps", Boolean.toString(fps));
        p.setProperty("ping", Boolean.toString(ping));
        p.setProperty("armor", Boolean.toString(armor));
        p.setProperty("coordinates", Boolean.toString(coordinates));
        p.setProperty("skinUrl", skinUrl);
        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream out = Files.newOutputStream(FILE)) { p.store(out, "Moon Client"); }
        } catch (IOException e) {
            System.err.println("[Moon Client] Config save failed: " + e.getMessage());
        }
    }
}
