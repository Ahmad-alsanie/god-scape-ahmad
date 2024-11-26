package com.godscape.system.enums;

import com.godscape.system.templates.EmptyPanel;
import com.godscape.system.themes.celestial.CelestialTheme;
import com.godscape.system.themes.sandbox.SandboxTheme;
import com.godscape.system.themes.volcanic.VolcanicTheme;
import com.godscape.system.themes.oceanic.OceanicTheme;
import com.godscape.system.themes.light.LightTheme;
import com.godscape.system.themes.dark.DarkTheme;
import com.godscape.system.themes.dunescar.DunescarTheme;
import com.godscape.system.themes.dawnbreak.DawnbreakTheme;
import com.godscape.system.themes.viridian.ViridianTheme;
import com.godscape.system.themes.arctic.ArcticTheme;
import com.godscape.system.themes.steampunk.SteampunkTheme;
import com.godscape.system.themes.cyberpunk.CyberpunkTheme;
import com.godscape.system.themes.aurora.AuroraTheme;
import com.godscape.system.themes.neon.NeonTheme;
import com.godscape.system.themes.shadowfest.ShadowfestTheme;
import com.godscape.system.themes.springtide.SpringtideTheme;
import com.godscape.system.themes.heartswake.HeartswakeTheme;
import com.godscape.system.themes.abyssal.AbyssalTheme;
import com.godscape.system.themes.frostfall.FrostfallTheme;
import com.godscape.system.themes.stygian.StygianTheme;


import com.godscape.system.utility.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Enum representing different themes with their associated colors, fonts, and entry points.
 */
@Getter
@RequiredArgsConstructor
public enum Themes {

    // Theme definitions with associated colors, fonts, and entry points for creating panels
    LIGHT(new ThemeColors(
            UIManager.getColor("Panel.background"),
            Color.BLACK,
            Color.LIGHT_GRAY,
            Color.GRAY,
            "Arial", Font.PLAIN, 12),
            null, EmptyPanel::new),
    /*DARK(new ThemeColors(Color.BLACK, Color.WHITE, Color.DARK_GRAY, Color.LIGHT_GRAY, "Arial", Font.PLAIN, 12),
            null, EmptyPanel::new),*/ // Direct ThemeColors instance for dark theme
    CELESTIAL(null,
            () -> new ThemeColors(new Color(10, 10, 30), Color.WHITE, new Color(70, 70, 150), Color.LIGHT_GRAY, "Georgia", Font.ITALIC, 12),
            CelestialTheme::new),
    VOLCANIC(null,
            () -> new ThemeColors(new Color(60, 0, 0), Color.ORANGE, new Color(120, 30, 30), Color.DARK_GRAY, "Impact", Font.BOLD, 14),
            VolcanicTheme::new),
    OCEANIC(null,
            () -> new ThemeColors(new Color(0, 50, 100), Color.WHITE, new Color(0, 100, 200), Color.LIGHT_GRAY, "Verdana", Font.PLAIN, 12),
            OceanicTheme::new),
    STYGIAN(null,
            () -> new ThemeColors(new Color(0, 0, 0), Color.WHITE, new Color(25, 25, 25), Color.DARK_GRAY, "Arial", Font.PLAIN, 12),
            StygianTheme::new),
    VIRIDIAN(null,
            () -> new ThemeColors(new Color(34, 139, 34), Color.BLACK, new Color(85, 107, 47), Color.LIGHT_GRAY, "Courier New", Font.PLAIN, 12),
            ViridianTheme::new),
    DUNESCAR(null,
            () -> new ThemeColors(new Color(210, 180, 140), Color.BLACK, new Color(244, 164, 96), Color.DARK_GRAY, "Times New Roman", Font.PLAIN, 12),
            DunescarTheme::new),
    ARCTIC(null,
            () -> new ThemeColors(new Color(240, 248, 255), Color.BLACK, new Color(135, 206, 235), Color.LIGHT_GRAY, "Comic Sans MS", Font.PLAIN, 12),
            ArcticTheme::new),
    DAWNBREAK(null,
            () -> new ThemeColors(new Color(255, 223, 186), Color.BLACK, new Color(255, 140, 0), Color.LIGHT_GRAY, "Segoe Script", Font.BOLD, 12),
            DawnbreakTheme::new),
    HEARTSWAKE(null,
            () -> new ThemeColors(new Color(255, 192, 203), Color.BLACK, new Color(255, 105, 180), Color.LIGHT_GRAY, "Arial", Font.PLAIN, 12),
            HeartswakeTheme::new),
    FROSTFALL(null,
            () -> new ThemeColors(new Color(135, 206, 250), Color.BLACK, new Color(70, 130, 180), Color.LIGHT_GRAY, "Arial", Font.PLAIN, 12),
            FrostfallTheme::new),
    SHADOWFEST(null,
            () -> new ThemeColors(new Color(0, 0, 0), Color.WHITE, new Color(128, 0, 0), Color.DARK_GRAY, "Arial", Font.PLAIN, 12),
            ShadowfestTheme::new),
    SPRINGTIDE(null,
            () -> new ThemeColors(new Color(0, 255, 127), Color.BLACK, new Color(0, 128, 64), Color.LIGHT_GRAY, "Arial", Font.PLAIN, 12),
            SpringtideTheme::new);


    private final ThemeColors directThemeColors; // Direct instance for ThemeColors
    private final Supplier<ThemeColors> themeColorsSupplier; // Supplier for lazy instantiation of ThemeColors
    private final Supplier<JPanel> entryPoint; // Supplier for creating the theme's panel

    private static Themes currentTheme = LIGHT; // Default theme

    /**
     * Get the ThemeColors for the theme.
     *
     * @return The ThemeColors associated with this theme.
     */
    public ThemeColors getThemeColors() {
        if (directThemeColors != null) {
            return directThemeColors;
        } else if (themeColorsSupplier != null) {
            try {
                ThemeColors colors = themeColorsSupplier.get();
                if (colors == null) {
                    Logger.warn("Themes: Supplier returned null for theme '{}'. Using fallback colors.", this.name());
                    colors = createFallbackColors();
                }
                return colors;
            } catch (Exception e) {
                Logger.warn("Themes: Failed to retrieve theme colors for '{}'. Using fallback colors.", this.name());
                return createFallbackColors();
            }
        }
        return createFallbackColors(); // Fallback if no match
    }

    /**
     * Creates a fallback ThemeColors for the current theme.
     *
     * @return The fallback ThemeColors associated with the current theme.
     */
    public ThemeColors createFallbackColors() {
        switch (this) {
             case CELESTIAL:
                return new ThemeColors(new Color(10, 10, 30), Color.WHITE, new Color(70, 70, 150), Color.LIGHT_GRAY, "Georgia", Font.ITALIC, 12);
            default:
                Logger.warn("Themes: No specific fallback defined for '{}'. Using LIGHT theme as fallback.", this.name());
                return LIGHT.getThemeColors(); // Fallback to LIGHT
        }
    }

    public JPanel createPanel() {
        try {
            JPanel panel = entryPoint.get();
            if (panel == null) {
                Logger.warn("Themes: Entry point returned null for theme '{}'. Applying default colors.", this.name());
                panel = new JPanel();
            }
            return applyThemeColorsToPanel(panel, getThemeColors());
        } catch (Exception e) {
            Logger.warn("Themes: Failed to create panel for theme '{}'. Using fallback colors.", this.name());
            return applyThemeColorsToPanel(new JPanel(), getThemeColors());
        }
    }

    /**
     * Applies the theme colors to a JPanel.
     *
     * @param panel  The JPanel to apply the theme colors to.
     * @param colors The ThemeColors to apply.
     * @return The JPanel with applied colors.
     */
    private JPanel applyThemeColorsToPanel(JPanel panel, ThemeColors colors) {
        panel.setBackground(colors.getBackgroundColor());
        panel.setForeground(colors.getForegroundColor());
        panel.setBorder(BorderFactory.createLineBorder(colors.getBorderColor()));
        panel.setFont(colors.getFont());
        return panel;
    }

    /**
     * Gets the currently active theme.
     *
     * @return The currently active Themes enum.
     */
    public static Themes getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Sets the current theme.
     *
     * @param theme The theme to set as the current theme.
     */
    public static void setCurrentTheme(Themes theme) {
        if (theme != null) {
            currentTheme = theme;
            Logger.info("Themes: Current theme set to '{}'.", theme.name());
        }
    }

    /**
     * Utility method to safely parse a theme from a string.
     *
     * @param themeName The name of the theme.
     * @return The corresponding Themes enum, or LIGHT if not found.
     */
    public static Themes fromString(String themeName) {
        if (themeName == null || themeName.isEmpty()) {
            Logger.warn("Themes: Received null or empty theme name. Defaulting to LIGHT.");
            return LIGHT;
        }
        try {
            return Themes.valueOf(themeName.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            Logger.warn("Themes: Invalid theme name '{}'. Defaulting to LIGHT.", themeName);
            return LIGHT;
        }
    }

    @Getter
    public static class ThemeColors {
        private final Color backgroundColor;
        private final Color foregroundColor;
        private final Color accentColor;
        private final Color borderColor;
        private final String fontName;
        private final int fontStyle;
        private final int fontSize;

        private Font font;

        public ThemeColors(Color backgroundColor, Color foregroundColor, Color accentColor, Color borderColor, String fontName, int fontStyle, int fontSize) {
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
            this.accentColor = accentColor;
            this.borderColor = borderColor;
            this.fontName = fontName;
            this.fontStyle = fontStyle;
            this.fontSize = fontSize;
        }

        public Font getFont() {
            if (font == null) {
                font = createFont(fontName, fontStyle, fontSize);
            }
            return font;
        }

        private Font createFont(String fontName, int fontStyle, int fontSize) {
            String[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            boolean fontAvailable = Arrays.asList(availableFonts).contains(fontName);

            if (fontAvailable) {
                return new Font(fontName, fontStyle, fontSize);
            } else {
                Logger.warn("Font '{}' is not available. Using default font.", fontName);
                return new Font("SansSerif", fontStyle, fontSize);
            }
        }
    }
}
