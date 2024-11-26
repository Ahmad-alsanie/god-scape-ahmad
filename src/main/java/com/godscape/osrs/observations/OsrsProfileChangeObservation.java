package com.godscape.osrs.observations;

import com.godscape.osrs.schemas.OsrsProfileSchema;

public interface OsrsProfileChangeObservation {
    void onProfileAdded(OsrsProfileSchema profile);
    void onProfileUpdated(OsrsProfileSchema profile);
    void onProfileRemoved(OsrsProfileSchema profile);
}
