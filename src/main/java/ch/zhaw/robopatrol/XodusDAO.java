package ch.zhaw.robopatrol;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.jetbrains.annotations.NotNull;
import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;
import static jetbrains.exodus.env.StoreConfig.WITHOUT_DUPLICATES;

/**
 * Created by simmljoe on 17.04.2016.
 * Ignore 'Failed to load class "org.slf4j.impl.StaticLoggerBinder' fault by gradle!
 */


public class XodusDAO {
    public XodusDAO(String name){
        instanceName = name;
    }
    private Environment environment;
    private Store store;
    private String instanceName;


    /**
     *Create or open existing store in environment
     */
    private void openDBStore() {
        store = environment.computeInTransaction(new TransactionalComputable<Store>() {
            @Override
            public Store compute(@NotNull final Transaction txn) {
                return environment.openStore("Store", WITHOUT_DUPLICATES, txn);
            }
        });
    }

    /**
     *
     * @param key
     * @param value
     * Put key/value pair to Database. Existing Data will be overwriten
     */
    public void putToDatabase(String key, String value){
        environment = Environments.newInstance(instanceName);
        openDBStore();
        ByteIterable byteKey, byteValue;
        byteKey = stringToEntry(key);
        byteValue = stringToEntry(value);

        environment.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull final Transaction txn) {
                store.put(txn, byteKey, byteValue);
            }
        });
        environment.close();
    }


    /**
     *
     * @param key as String
     * @return value to key as String
     * @trhows NullpointerException if nothing is found in DB
     * Read value by key "myKey"
     */
    public String getByKey(String key) {
        environment = Environments.newInstance(instanceName);
        openDBStore();
        @NotNull final ByteIterable byteKey = stringToEntry(key);
        ByteIterable entry = environment.computeInReadonlyTransaction(new TransactionalComputable<ByteIterable>() {
            @Override
            public ByteIterable compute(@NotNull final Transaction txn) {
                return store.get(txn, byteKey);
            }
        });

        environment.close();
        return entryToString(entry);
    }

    /**
     *
     * @param key
     * Delete key/value Pair by Key without success / fail message
     */
    public void deleteByKey(String key) {
        ByteIterable byteKey = stringToEntry(key);
        environment = Environments.newInstance(instanceName);
        openDBStore();

        environment.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull final Transaction txn) {
                store.delete(txn, byteKey);
            }
        });
        environment.close();
    }
}
