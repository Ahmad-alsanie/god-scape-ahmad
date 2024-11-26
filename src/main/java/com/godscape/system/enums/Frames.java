package com.godscape.system.enums;

import com.godscape.osrs.frames.OsrsMainFrame;
import com.godscape.system.factories.DependencyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

/**
 * Enum representing different Frames in the system.
 */
@Getter
@RequiredArgsConstructor
public enum Frames {

    /**
     * Represents the main OSRS frame.
     */
    OSRS_MAIN_FRAME(OsrsMainFrame.class, () -> DependencyFactory.getInstance().getInjection(OsrsMainFrame.class));

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    /**
     * Retrieves the instance of the frame.
     *
     * @param <T> the type of the frame
     * @return the instance of the frame
     */
    @SuppressWarnings("unchecked")
    public <T> T getFrame() {
        return (T) supplier.get();
    }
}
