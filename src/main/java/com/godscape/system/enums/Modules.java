package com.godscape.system.enums;

import com.godscape.osrs.enums.core.OsrsControllers;
import com.godscape.rs3.enums.core.Rs3Controllers;
import com.godscape.system.controllers.BotController;
import com.godscape.system.modules.DatabaseModule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Modules {

    DATABASE_MODULE(DatabaseModule::new);

    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    public <T> T getController() {
        return (T) supplier.get();
    }
}
