package com.godscape.system.enums;

import com.godscape.system.cache.backup.*;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Utilities {

    LOGGER(Logger.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return new Logger();
        }
    }),
    HAZELCAST_CONFIG(HazelcastConfig.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return new HazelcastConfig();
        }
    }),
    BOT_INITIALIZE(BotInitialize.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return DependencyFactory.getInstance().getInjection(BotInitialize.class);
        }
    }),
    BOT_MAIN_LOOP(BotMainLoop.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return DependencyFactory.getInstance().getInjection(BotMainLoop.class);
        }
    }),
    BOT_TERMINATE(BotTerminate.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return DependencyFactory.getInstance().getInjection(BotTerminate.class);
        }
    }),
    BACKUP_TO_XML(BackupToXml.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return new BackupToXml();
        }
    }),
    BACKUP_TO_JSON(BackupToJson.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return new BackupToJson();
        }
    }),
    RESTORE_FROM_XML(RestoreFromXml.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return new RestoreFromXml();
        }
    }),
    RESTORE_FROM_JSON(RestoreFromJson.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return new RestoreFromJson();
        }
    }),
    PERFORM_BACKUP(PerformBackup.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return DependencyFactory.getInstance().getInjection(PerformBackup.class);
        }
    }),
    PERFORM_RESTORE(PerformRestore.class, new Supplier<Object>() {
        @Override
        public Object get() {
            return DependencyFactory.getInstance().getInjection(PerformRestore.class);
        }
    });

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @SuppressWarnings("unchecked")
    public <T> T createUtility() {
        return (T) supplier.get();
    }
}
