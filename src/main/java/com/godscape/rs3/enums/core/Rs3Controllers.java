package com.godscape.rs3.enums.core;

import com.godscape.rs3.controllers.Rs3CharacterController;
import com.godscape.rs3.controllers.Rs3ProfileController;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum Rs3Controllers {

    RS3_PROFILE_CONTROLLER(Rs3ProfileController::new),
    RS3_CHARACTER_CONTROLLER(Rs3CharacterController::new);

    private final Supplier<?> supplier;

    Rs3Controllers(Supplier<?> supplier) {
        this.supplier = supplier;
    }

    public <T> T getController() {
        return (T) supplier.get();
    }
}
