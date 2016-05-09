package ch.zhaw.robopatrol.store;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;


public class RobopatrolStore<T extends Entity> implements Closeable, AutoCloseable {

    private final DB db;

    private final Map<String, T> persistentMap;

    protected RobopatrolStore(DB db, String storeId) {
        this.db = db;
        persistentMap = (Map<String, T>) db.hashMap(storeId).createOrOpen();
    }

    public Collection<T> getAll() {
        return persistentMap.values();
    }

    public void put(T entity) {
        persistentMap.put(entity.getId(), entity);
        db.commit();
    }

    public T get(String id) {
        return persistentMap.get(id);
    }

    public void remove(String id) {
        persistentMap.remove(id);
        db.commit();
    }

    @Override
    public void close() {
        db.close();
    }

}
