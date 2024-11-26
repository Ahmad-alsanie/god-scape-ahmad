package com.godscape.system.observers;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.managers.BaseObservationManager;
import com.godscape.system.utility.Logger;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;

import javax.swing.*;

public class ProfileChangeListener implements EntryAddedListener<Object, Object>,
        EntryUpdatedListener<Object, Object>,
        EntryRemovedListener<Object, Object> {

    private final BaseObservationManager baseObservationManager;

    public ProfileChangeListener() {
        this.baseObservationManager = DependencyFactory.getInstance().getInjection(BaseObservationManager.class);
    }

    @Override
    public void entryAdded(EntryEvent<Object, Object> event) {
        Object profile = event.getValue();
        Logger.info("Profile added: {}", getProfileName(profile));
        SwingUtilities.invokeLater(() -> baseObservationManager.notifyProfileAdded(profile));
    }

    @Override
    public void entryUpdated(EntryEvent<Object, Object> event) {
        Object profile = event.getValue();
        Logger.info("Profile updated: {}", getProfileName(profile));
        SwingUtilities.invokeLater(() -> baseObservationManager.notifyProfileUpdated(profile));
    }

    @Override
    public void entryRemoved(EntryEvent<Object, Object> event) {
        Object profile = event.getOldValue();
        Logger.info("Profile removed: {}", getProfileName(profile));
        SwingUtilities.invokeLater(() -> baseObservationManager.notifyProfileRemoved(profile));
    }

    private String getProfileName(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            return ((OsrsProfileSchema) profile).getProfileName();
        } else if (profile instanceof Rs3ProfileSchema) {
            return ((Rs3ProfileSchema) profile).getProfileName();
        }
        return "Unknown Profile";
    }
}
