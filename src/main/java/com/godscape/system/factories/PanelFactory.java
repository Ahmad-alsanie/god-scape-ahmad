package com.godscape.system.factories;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.managers.OsrsObservationManager;
import com.godscape.osrs.observations.OsrsProfileChangeObservation;
import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.rs3.managers.Rs3ObservationManager;
import com.godscape.rs3.observations.Rs3ProfileChangeObservation;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.managers.BaseObservationManager;
import com.godscape.system.observers.BotStateObservation;
import com.godscape.system.observers.ProfileChangeObserver;
import com.godscape.system.observers.ThemeChangeObservation;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PanelFactory {
    private static volatile PanelFactory instance;

    private PanelFactory() {
        Logger.info("PanelFactory initialized.");
    }

    public static PanelFactory getInstance() {
        if (instance == null) {
            synchronized (PanelFactory.class) {
                if (instance == null) {
                    instance = new PanelFactory();
                }
            }
        }
        return instance;
    }

    public JPanel createPanel(Enum<?> panelEnum, Object footerManager) {
        if (panelEnum == null || isExcludedPanel(panelEnum)) {
            Logger.info("Skipping creation of excluded panel: '{}'", panelEnum != null ? getTabTitle(panelEnum) : "null");
            return createErrorPanel("Panel Excluded or Null");
        }

        try {
            JPanel panel = createStandardPanel(panelEnum);
            registerObservers(panel);
            return panel;
        } catch (Exception e) {
            Logger.error("PanelFactory: Error creating panel '{}': {}", getTabTitle(panelEnum), e.getMessage(), e);
            return createErrorPanel(getTabTitle(panelEnum));
        }
    }

    private JPanel createStandardPanel(Enum<?> panelEnum) {
        Supplier<? extends JPanel> supplier = getSupplier(panelEnum);
        return supplier.get();
    }

    private void registerObservers(JPanel panel) {
        GameVersion currentGameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();

        if (currentGameVersion == GameVersion.OSRS && panel instanceof OsrsProfileChangeObservation) {
            DependencyFactory.getInstance().getInjection(OsrsObservationManager.class)
                    .addProfileObserver((OsrsProfileChangeObservation) panel);
            Logger.info("Registered OsrsProfileChangeObservation for OSRS panel: {}", panel.getClass().getSimpleName());
        } else if (currentGameVersion == GameVersion.RS3 && panel instanceof Rs3ProfileChangeObservation) {
            DependencyFactory.getInstance().getInjection(Rs3ObservationManager.class)
                    .addProfileObserver((Rs3ProfileChangeObservation) panel);
            Logger.info("Registered Rs3ProfileChangeObservation for RS3 panel: {}", panel.getClass().getSimpleName());
        } else {
            Logger.warn("PanelFactory: Unsupported ProfileChangeObserver type or game version.");
        }

        if (panel instanceof ThemeChangeObservation) {
            DependencyFactory.getInstance().getInjection(BaseObservationManager.class)
                    .addThemeListener((ThemeChangeObservation) panel);
            Logger.info("Registered ThemeChangeObservation for panel: {}", panel.getClass().getSimpleName());
        }

        if (panel instanceof BotStateObservation) {
            DependencyFactory.getInstance().getInjection(BaseObservationManager.class)
                    .addBotStateListener((BotStateObservation) panel);
            Logger.info("Registered BotStateObservation for panel: {}", panel.getClass().getSimpleName());
        }
    }

    private boolean isExcludedPanel(Enum<?> panelEnum) {
        Enum<?> mainPanel = panelEnum instanceof OsrsPanels ? OsrsPanels.OSRS_MAIN_PANEL : Rs3Panels.RS3_MAIN_PANEL;
        Enum<?> parent = getParentPanel(panelEnum);

        while (parent != null) {
            if (parent == mainPanel) {
                return true;
            }
            parent = getParentPanel(parent);
        }

        return false;
    }

    private JPanel createErrorPanel(String tabTitle) {
        JPanel errorPanel = new JPanel();
        JLabel errorMessage = new JLabel("Failed to load the '" + tabTitle + "' panel.");
        errorPanel.add(errorMessage);
        errorPanel.setBackground(Color.RED);
        errorMessage.setForeground(Color.WHITE);
        return errorPanel;
    }

    private Supplier<? extends JPanel> getSupplier(Enum<?> panelEnum) {
        if (panelEnum instanceof OsrsPanels) {
            return ((OsrsPanels) panelEnum).getSupplier();
        } else if (panelEnum instanceof Rs3Panels) {
            return ((Rs3Panels) panelEnum).getSupplier();
        }
        throw new IllegalArgumentException("Invalid panel enum type");
    }

    private String getTabTitle(Enum<?> panelEnum) {
        if (panelEnum instanceof OsrsPanels) {
            return ((OsrsPanels) panelEnum).getTabTitle();
        } else if (panelEnum instanceof Rs3Panels) {
            return ((Rs3Panels) panelEnum).getTabTitle();
        }
        throw new IllegalArgumentException("Invalid panel enum type");
    }

    private Enum<?> getParentPanel(Enum<?> panelEnum) {
        if (panelEnum instanceof OsrsPanels) {
            return ((OsrsPanels) panelEnum).getParentPanel();
        } else if (panelEnum instanceof Rs3Panels) {
            return ((Rs3Panels) panelEnum).getParentPanel();
        }
        throw new IllegalArgumentException("Invalid panel enum type");
    }

    private List<String> getFullTabTitlePath(Enum<?> panelEnum) {
        List<String> path = new ArrayList<>();
        Enum<?> current = panelEnum;
        while (current != null) {
            path.add(0, getTabTitle(current));
            current = getParentPanel(current);
        }
        return path;
    }
}
