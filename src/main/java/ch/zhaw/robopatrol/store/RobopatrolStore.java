package ch.zhaw.robopatrol.store;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;


public class RobopatrolStore<T extends Entity> {

    private final DB db;

    private final Map<String, T> persistentMap;

    public static <T extends Entity> RobopatrolStore<T> forClass(Class<?> storeId) {
        return new RobopatrolStore<T>(storeId.getName());
    }

    public static <T extends Entity> RobopatrolStore<T> inMemory() {
        return new RobopatrolStore<T>(UUID.randomUUID().toString(), DBMaker.memoryDB().make());
    }

    private RobopatrolStore(String storeId) {
        this(storeId, DBMaker.fileDB(storeId + ".mapdb").closeOnJvmShutdown().make());
    }

    RobopatrolStore(String storeId, DB db) {
        this.db = db;
        persistentMap = (Map<String, T>) db.hashMap(storeId).createOrOpen();
    }

    public Collection<T> getAll() {
        return persistentMap.values();
    }

    public void put(String id, T entity) {
        persistentMap.put(id, entity);
    }

    public T get(String id) {
        return persistentMap.get(id);
    }

    public void remove(String id) {
        persistentMap.remove(id);
    }

    public void close() {
        db.close();
    }

}
