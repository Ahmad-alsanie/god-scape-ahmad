package com.godscape.system.interfaces.fuse;

public interface ObservationFuse<T> {
    void addObserver(T observer);
    void removeObserver(T observer);
    void notifyObservers(Object profile);
}
