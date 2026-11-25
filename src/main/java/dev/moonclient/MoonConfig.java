package dev.moonclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class MoonConfig {
    private static final Path FILE = Path.of("config", "moonclient.properties");
    public static boolean hudEnabled = true;
    public static boolean showFps = true;
    public static boolean showCps = true;
    public static boolean showKeystrokes = true;
    public static boolean showCoordinates = false;
    public static boolean showPing = true;
    public static boolean showArmor = true;
    public static boolean showPotionEffects = true;
    public static boolean customCrosshair = false;
    public static boolean toggleSprint = false;
    public static boolean compactHud = true;
    public static boolean minimalAnimations = true;
    public static boolean reducedParticles = false;

    private MoonConfig() {}

    public static void load() {
        Properties p = new Properties();
        if (Files.exists(FILE)) {
            try (InputStream in = Files.newInputStream(FILE)) { p.load(in); }
            catch (IOException e) { System.err.println("[Moon Client] Config load failed: " + e.getMessage()); }
        }
        hudEnabled = get(p, "hudEnabled", hudEnabled);
        showFps = get(p, "showFps", showFps);
        showCps = get(p, "showCps", showCps);
        showKeystrokes = get(p, "showKeystrokes", showKeystrokes);
        showCoordinates = get(p, "showCoordinates", showCoordinates);
        showPing = get(p, "showPing", showPing);
        showArmor = get(p, "showArmor", showArmor);
        showPotionEffects = get(p, "showPotionEffects", showPotionEffects);
        customCrosshair = get(p, "customCrosshair", customCrosshair);
        toggleSprint = get(p, "toggleSprint", toggleSprint);
        compactHud = get(p, "compactHud", compactHud);
        minimalAnimations = get(p, "minimalAnimations", minimalAnimations);
        reducedParticles = get(p, "reducedParticles", reducedParticles);
    }

    private static boolean get(Properties p, String key, boolean fallback) {
        return Boolean.parseBoolean(p.getProperty(key, Boolean.toString(fallback)));
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("hudEnabled", Boolean.toString(hudEnabled));
        p.setProperty("showFps", Boolean.toString(showFps));
        p.setProperty("showCps", Boolean.toString(showCps));
        p.setProperty("showKeystrokes", Boolean.toString(showKeystrokes));
        p.setProperty("showCoordinates", Boolean.toString(showCoordinates));
        p.setProperty("showPing", Boolean.toString(showPing));
        p.setProperty("showArmor", Boolean.toString(showArmor));
        p.setProperty("showPotionEffects", Boolean.toString(showPotionEffects));
        p.setProperty("customCrosshair", Boolean.toString(customCrosshair));
        p.setProperty("toggleSprint", Boolean.toString(toggleSprint));
        p.setProperty("compactHud", Boolean.toString(compactHud));
        p.setProperty("minimalAnimations", Boolean.toString(minimalAnimations));
        p.setProperty("reducedParticles", Boolean.toString(reducedParticles));
        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream out = Files.newOutputStream(FILE)) { p.store(out, "Moon Client settings"); }
        } catch (IOException e) { System.err.println("[Moon Client] Config save failed: " + e.getMessage()); }
    }
}
