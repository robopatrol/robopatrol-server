package ch.zhaw.robopatrol;


import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.jetbrains.annotations.NotNull;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;
import static jetbrains.exodus.env.StoreConfig.WITHOUT_DUPLICATES;

/**
 * This example shows low level key/value store access.
 */
public class XodusPlayground {

    public static void main(String[] args) {

        //Create environment or open existing one
        final Environment env = Environments.newInstance("data");

        //Create or open existing store in environment
        final Store store = env.computeInTransaction(new TransactionalComputable<Store>() {
            @Override
            public Store compute(@NotNull final Transaction txn) {
                return env.openStore("MyStore", WITHOUT_DUPLICATES, txn);
            }
        });

        @NotNull final ByteIterable key = stringToEntry("myKey");
        @NotNull final ByteIterable value = stringToEntry("myValue");

        // Put "myValue" string under the key "myKey"
        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull final Transaction txn) {
                store.put(txn, key, value);
            }
        });

        // Read value by key "myKey"
        env.executeInTransaction(new TransactionalExecutable() {
            @Override
            public void execute(@NotNull final Transaction txn) {
                final ByteIterable entry = store.get(txn, key);
                assert entry == value;
                System.out.println(entryToString(entry));
            }
        });

        // Close environment when we are done
        env.close();
    }
}