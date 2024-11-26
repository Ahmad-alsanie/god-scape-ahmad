package com.godscape.rs3.observations;

import com.godscape.rs3.schemas.Rs3ProfileSchema;

public interface Rs3ProfileChangeObservation {
    void onProfileAdded(Rs3ProfileSchema profile);
    void onProfileUpdated(Rs3ProfileSchema profile);
    void onProfileRemoved(Rs3ProfileSchema profile);
}
