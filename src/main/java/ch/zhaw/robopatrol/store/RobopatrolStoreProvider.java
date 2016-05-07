package ch.zhaw.robopatrol.store;

import java.util.HashMap;
import java.util.Map;

/** Provides shared instances of {@link RobopatrolStore}. */
public class RobopatrolStoreProvider implements AutoCloseable {

    private final Map<Class<?>, RobopatrolStore<?>> sharedStores = new HashMap<>();

    public <T extends Entity> RobopatrolStore<T> get(Class<?> storeId) {
        return (RobopatrolStore<T>) sharedStores.computeIfAbsent(storeId, RobopatrolStore::forClass);
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    @Override
    public void close() throws Exception {
        sharedStores.values().forEach(RobopatrolStore::close);
        sharedStores.clear();
    }
}