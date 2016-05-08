package ch.zhaw.robopatrol.store;

import java.util.HashMap;
import java.util.Map;

/** Provides shared instances of {@link RobopatrolStore}. */
public class RobopatrolStoreProvider implements AutoCloseable {

    private final Map<Class<?>, RobopatrolStore<?>> sharedStores = new HashMap<>();

    public <T extends Entity> RobopatrolStore<T> get(Class<?> storeId) {
        return (RobopatrolStore<T>) sharedStores.computeIfAbsent(storeId, RobopatrolStore::forClass);
    }

    public RobopatrolStoreProvider() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        System.out.println("Closing all RobopatrolStores...");
        sharedStores.values().forEach(RobopatrolStore::close);
        sharedStores.clear();
    }

}