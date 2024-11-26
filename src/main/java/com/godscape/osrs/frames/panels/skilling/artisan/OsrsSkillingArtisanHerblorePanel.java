package com.godscape.osrs.frames.panels.skilling.artisan;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class OsrsSkillingArtisanHerblorePanel extends JPanel {

    private static volatile OsrsSkillingArtisanHerblorePanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingArtisanHerblorePanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_SKILLING_HERBLORE_PANEL);

        try {
            Logger.info("Attempting to initialize PanelGrid within OsrsSkillingArtisanHerblorePanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            // Define dropdown options for configurations
            List<String> potionTypes = Arrays.asList("Auto", "Attack Potion", "Strength Potion", "Magic Potion", "Super Attack", "Super Strength", "Saradomin Brew", "Overload");
            List<String> herbs = Arrays.asList("Auto", "Guam", "Marrentill", "Tarromin", "Harralander", "Ranarr", "Toadflax", "Irit", "Avantoe", "Kwuarm", "Snapdragon", "Cadantine", "Dwarf Weed", "Torstol");
            List<String> secondaryIngredients = Arrays.asList("Auto", "Eye of Newt", "Red Spider's Eggs", "Unicorn Horn Dust", "Snape Grass", "Limpwurt Root", "Wine of Zamorak", "Dragon Scale Dust");
            List<String> potionEfficiency = Arrays.asList("Normal", "Efficient");

            osrsGridBuilder.addSeparator("Herblore Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Potion Type:");
            osrsGridBuilder.addDropdown(2, "herbloreSettings", "potionType", potionTypes, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Herb Selection:");
            osrsGridBuilder.addDropdown(4, "herbloreSettings", "herbSelection", herbs, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Secondary Ingredient:");
            osrsGridBuilder.addDropdown(2, "herbloreSettings", "secondaryIngredient", secondaryIngredients, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Potion Efficiency:");
            osrsGridBuilder.addDropdown(4, "herbloreSettings", "potionEfficiency", potionEfficiency, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Auto Make Overloads:");
            osrsGridBuilder.addCheckbox(2, "herbloreSettings", "autoMakeOverloads", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Cleaning Herbs:");
            osrsGridBuilder.addCheckbox(4, "herbloreSettings", "cleaningHerbs", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Use Portable Wells:");
            osrsGridBuilder.addCheckbox(2, "herbloreSettings", "usePortableWells", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Auto Buy Ingredients:");
            osrsGridBuilder.addCheckbox(4, "herbloreSettings", "autoBuyIngredients", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Herblore Boosting:");
            osrsGridBuilder.addCheckbox(2, "herbloreSettings", "herbloreBoosting", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Obtain Botanical Outfit:");
            osrsGridBuilder.addObtainbox(4, "herbloreSettings", "obtainBotanicalOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsSkillingArtisanHerblorePanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsSkillingArtisanHerblorePanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }

    public OsrsGridBuilder getPanelGrid() {
        return osrsGridBuilder;
    }
}
