package ch.zhaw.robopatrol.store;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.Closeable;
import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Provides shared instances of {@link RobopatrolStore}. */
public class RobopatrolStoreProvider implements Closeable, AutoCloseable {

    private static final String STORE_DIR = "store";

    private static final String FILE_EXTENTION = "mapdb";

    private final Map<Class<?>, RobopatrolStore<?>> sharedStores = new ConcurrentHashMap<>();

    public <T extends Entity> RobopatrolStore<T> get(Class<?> storeId) {
        return (RobopatrolStore<T>) sharedStores.computeIfAbsent(storeId, RobopatrolStoreProvider::forClass);
    }

    public static <T extends Entity> RobopatrolStore<T> inMemory() {
        DB db = DBMaker.memoryDB().transactionEnable().make();
        return new RobopatrolStore<T>(db, UUID.randomUUID().toString());
    }

    private static <T extends Entity> RobopatrolStore<T> forClass(Class<?> id) {
        DB db = DBMaker
            .fileDB(getStoreFile(id))
            .transactionEnable()
            .closeOnJvmShutdown()
            .make();

        return new RobopatrolStore<>(db, idForClass(id));
    }

    static File getStoreFile(Class<?> id) {
        File baseDir = new File(STORE_DIR);
        if (!baseDir.isDirectory() && !baseDir.mkdirs()) {
            throw new IllegalStateException("Can't create database directory: " + baseDir);
        }

        return new File(baseDir, idForClass(id) + "." + FILE_EXTENTION);
    }

    private static String idForClass(Class<?> id) {
        return id.getName();
    }

    @Override
    public void close() {
        System.out.println("Closing all RobopatrolStores...");
        sharedStores.values().forEach(RobopatrolStore::close);
        sharedStores.clear();
    }

}