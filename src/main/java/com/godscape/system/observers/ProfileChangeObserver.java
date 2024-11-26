package com.godscape.system.observers;

import com.godscape.system.interfaces.mark.Observable;

public interface ProfileChangeObserver extends Observable {
    void updateSettings(String eventType, Object data);
}
