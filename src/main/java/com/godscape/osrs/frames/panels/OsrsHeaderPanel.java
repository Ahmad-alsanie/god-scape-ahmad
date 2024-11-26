package com.godscape.osrs.frames.panels;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.managers.panels.OsrsHeaderManager;
import com.godscape.osrs.observations.OsrsProfileChangeObservation;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
public class OsrsHeaderPanel extends JPanel implements OsrsProfileChangeObservation {

    private JComboBox<String> profileDropdown;
    private JButton createProfileButton;
    private JButton deleteProfileButton;
    private JButton updateProfileButton;
    private JButton renameProfileButton;

    private OsrsHeaderManager headerPanelManager;
    private boolean isProgrammaticallySettingProfile = false;

    public OsrsHeaderPanel() {
        setOpaque(false);
        initializeComponents();
        SwingUtilities.invokeLater(this::initializeUI);
        initialize(); // Call initialize here to set up dependencies and observers
    }

    /**
     * Initializes dependencies and registers listeners.
     * This method should be called after instantiation.
     */
    private void initialize() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.headerPanelManager = dependencyFactory.getInjection(OsrsHeaderManager.class);

        addActionListeners();
        loadProfiles();
        headerPanelManager.addObserver(this); // Register this panel as an observer
        Logger.info("OsrsHeaderPanel: Initialized with dependencies and observers.");
    }

    private void initializeComponents() {
        profileDropdown = new JComboBox<>();
        updateProfileButton = new JButton("Update");
        renameProfileButton = new JButton("Rename");
        deleteProfileButton = new JButton("Delete");
        createProfileButton = new JButton("Create");
    }

    private void initializeUI() {
        Logger.debug("Initializing OsrsHeaderPanel UI.");
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());

        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        profilePanel.setOpaque(false);

        profileDropdown.setPreferredSize(new Dimension(325, 50));
        profileDropdown.setMaximumRowCount(20);
        profileDropdown.setRenderer(new CenteredComboBoxRenderer());

        profilePanel.add(profileDropdown);

        Dimension buttonSize = new Dimension(120, 50);
        updateProfileButton.setPreferredSize(buttonSize);
        renameProfileButton.setPreferredSize(buttonSize);
        deleteProfileButton.setPreferredSize(buttonSize);
        createProfileButton.setPreferredSize(buttonSize);

        profilePanel.add(updateProfileButton);
        profilePanel.add(renameProfileButton);
        profilePanel.add(deleteProfileButton);
        profilePanel.add(createProfileButton);

        add(profilePanel, BorderLayout.NORTH);
        Logger.debug("OsrsHeaderPanel UI initialized.");
    }

    private static class CenteredComboBoxRenderer extends DefaultListCellRenderer {
        public CenteredComboBoxRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }

    private void addActionListeners() {
        createProfileButton.addActionListener(e -> createProfile());
        renameProfileButton.addActionListener(e -> renameProfile());
        deleteProfileButton.addActionListener(e -> deleteProfile());
        updateProfileButton.addActionListener(e -> updateProfile());

        profileDropdown.addActionListener(e -> {
            if (!isProgrammaticallySettingProfile) {
                onProfileSelected();
            }
        });

        Logger.debug("Action listeners added to OsrsHeaderPanel buttons and dropdown.");
    }

    private void loadProfiles() {
        Logger.debug("Loading profiles from header panel manager.");
        CompletableFuture.runAsync(() -> {
            List<String> profileNames = headerPanelManager.getAllProfileNames();
            Logger.info("Loaded {} profiles.", profileNames.size());
            SwingUtilities.invokeLater(() -> updateProfileDropdown(profileNames, null));
        }).exceptionally(ex -> {
            Logger.error("Failed to load profiles: {}", ex.getMessage());
            return null;
        });
    }

    private void createProfile() {
        SwingUtilities.invokeLater(() -> {
            String newProfileName = JOptionPane.showInputDialog(this, "Enter new profile name:");
            if (newProfileName != null && !newProfileName.trim().isEmpty()) {
                Logger.info("Creating new profile with name: {}", newProfileName.trim());
                boolean created = headerPanelManager.createProfile(newProfileName.trim());
                if (created) {
                    loadProfilesAndSelectProfile(newProfileName.trim());
                } else {
                    Logger.warn("Profile '{}' could not be created.", newProfileName.trim());
                    JOptionPane.showMessageDialog(this, "Profile could not be created. It may already exist.", "Creation Failed", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                Logger.warn("Attempted to create a profile with an empty or null name.");
            }
        });
    }

    private void renameProfile() {
        SwingUtilities.invokeLater(() -> {
            String selectedProfile = (String) profileDropdown.getSelectedItem();
            if (selectedProfile != null) {
                String newProfileName = JOptionPane.showInputDialog(this, "Enter new profile name:", selectedProfile);
                if (newProfileName != null && !newProfileName.trim().isEmpty() && !newProfileName.equals(selectedProfile)) {
                    Logger.info("Renaming profile from '{}' to '{}'", selectedProfile, newProfileName.trim());
                    boolean renamed = headerPanelManager.renameProfile(selectedProfile, newProfileName.trim());
                    if (renamed) {
                        loadProfilesAndSelectProfile(newProfileName.trim());
                    } else {
                        Logger.warn("Profile '{}' could not be renamed to '{}'.", selectedProfile, newProfileName.trim());
                        JOptionPane.showMessageDialog(this, "Profile could not be renamed. The new name may already exist.", "Renaming Failed", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    Logger.warn("Invalid new profile name provided for renaming.");
                }
            } else {
                Logger.warn("No profile selected for renaming.");
            }
        });
    }

    private void deleteProfile() {
        SwingUtilities.invokeLater(() -> {
            String selectedProfile = (String) profileDropdown.getSelectedItem();
            if (selectedProfile != null) {
                int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the profile: " + selectedProfile + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    Logger.info("Deleting profile: {}", selectedProfile);
                    boolean deleted = headerPanelManager.deleteProfile(selectedProfile);
                    if (deleted) {
                        loadProfilesAndSelectProfile(null);
                    } else {
                        Logger.warn("Profile '{}' could not be deleted.", selectedProfile);
                        JOptionPane.showMessageDialog(this, "Profile could not be deleted. It may not exist.", "Deletion Failed", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    Logger.info("Profile deletion canceled for: {}", selectedProfile);
                }
            } else {
                Logger.warn("No profile selected for deletion.");
            }
        });
    }

    private void updateProfile() {
        SwingUtilities.invokeLater(() -> {
            String selectedProfile = (String) profileDropdown.getSelectedItem();
            if (selectedProfile != null) {
                Logger.info("Updating profile: {}", selectedProfile);
                boolean updated = headerPanelManager.updateProfile(selectedProfile);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Profile '" + selectedProfile + "' has been updated successfully.", "Profile Updated", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Logger.warn("Profile '{}' could not be updated.", selectedProfile);
                    JOptionPane.showMessageDialog(this, "Profile could not be updated. It may not exist.", "Update Failed", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                Logger.warn("No profile selected for update.");
            }
        });
    }

    private void onProfileSelected() {
        String selectedProfile = (String) profileDropdown.getSelectedItem();
        Logger.info("Profile selected: {}", selectedProfile);
        if (selectedProfile != null) {
            headerPanelManager.loadProfileSettings(selectedProfile);
        }
    }

    private void updateProfileDropdown(List<String> profiles, String profileToSelect) {
        Logger.debug("Updating profile dropdown with profiles: {}", profiles);
        isProgrammaticallySettingProfile = true;
        try {
            profileDropdown.removeAllItems();
            profiles.forEach(profileDropdown::addItem);
            if (profileToSelect != null && profiles.contains(profileToSelect)) {
                profileDropdown.setSelectedItem(profileToSelect);
                Logger.debug("Selected profile: {}", profileToSelect);
            } else if (profileDropdown.getItemCount() > 0) {
                profileDropdown.setSelectedIndex(0);
                Logger.debug("Selected first available profile: {}", profileDropdown.getItemAt(0));
            } else {
                Logger.debug("No profiles available to select.");
            }
        } finally {
            isProgrammaticallySettingProfile = false;
        }
        onProfileSelected();
    }

    private void loadProfilesAndSelectProfile(String profileName) {
        CompletableFuture.runAsync(() -> {
            List<String> profileNames = headerPanelManager.getAllProfileNames();
            Logger.info("Loaded {} profiles.", profileNames.size());
            SwingUtilities.invokeLater(() -> updateProfileDropdown(profileNames, profileName));
        }).exceptionally(ex -> {
            Logger.error("Failed to load profiles and select profile: {}", ex.getMessage());
            return null;
        });
    }

    public void selectProfile(String profileName) {
        SwingUtilities.invokeLater(() -> {
            if (profileName != null && ((DefaultComboBoxModel<String>) profileDropdown.getModel()).getIndexOf(profileName) != -1) {
                isProgrammaticallySettingProfile = true;
                try {
                    profileDropdown.setSelectedItem(profileName);
                    Logger.debug("Programmatically selected profile: {}", profileName);
                } finally {
                    isProgrammaticallySettingProfile = false;
                }
            } else if (profileDropdown.getItemCount() > 0) {
                isProgrammaticallySettingProfile = true;
                try {
                    profileDropdown.setSelectedIndex(0);
                    Logger.debug("Programmatically selected first available profile: {}", profileDropdown.getItemAt(0));
                } finally {
                    isProgrammaticallySettingProfile = false;
                }
            }
            onProfileSelected();
        });
    }

    @Override
    public void onProfileAdded(OsrsProfileSchema profile) {
        Logger.info("Profile added: {}", profile.getProfileName());
        loadProfilesAndSelectProfile(profile.getProfileName());
    }

    @Override
    public void onProfileUpdated(OsrsProfileSchema profile) {
        Logger.info("Profile updated: {}", profile.getProfileName());
        loadProfilesAndSelectProfile(profile.getProfileName());
    }

    @Override
    public void onProfileRemoved(OsrsProfileSchema profile) {
        Logger.info("Profile removed: {}", profile.getProfileName());
        loadProfilesAndSelectProfile(null);
    }

    public void cleanup() {
        if (headerPanelManager != null) {
            headerPanelManager.removeObserver(this);
        }
        Logger.debug("OsrsHeaderPanel cleanup completed.");
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        cleanup();
    }
}
