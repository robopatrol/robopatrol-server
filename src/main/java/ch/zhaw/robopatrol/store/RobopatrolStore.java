package ch.zhaw.robopatrol.store;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;


public class RobopatrolStore<T extends Entity> implements AutoCloseable {

    private static final String STORE_DIR = "store";

    private static final String FILE_EXTENTION = "mapdb";

    private final DB db;

    private final Map<String, T> persistentMap;

    public static <T extends Entity> RobopatrolStore<T> forClass(Class<?> id) {
        File baseDir = new File(STORE_DIR);
        baseDir.mkdirs();

        String storeId = id.getName();
        File dbFile = new File(baseDir, storeId + "." + FILE_EXTENTION);
        DB db = DBMaker.fileDB(dbFile).closeOnJvmShutdown().make();

        return new RobopatrolStore<T>(db, storeId);
    }

    public static <T extends Entity> RobopatrolStore<T> inMemory() {
        DB db = DBMaker.memoryDB().make();
        return new RobopatrolStore<T>(db, UUID.randomUUID().toString());
    }

    private RobopatrolStore(DB db, String storeId) {
        this.db = db;
        persistentMap = (Map<String, T>) db.hashMap(storeId).createOrOpen();
    }

    public Collection<T> getAll() {
        return persistentMap.values();
    }

    public void put(T entity) {
        persistentMap.put(entity.getId(), entity);
    }

    public T get(String id) {
        return persistentMap.get(id);
    }

    public void remove(String id) {
        persistentMap.remove(id);
    }

    @Override
    public void close() {
        db.close();
    }

}
