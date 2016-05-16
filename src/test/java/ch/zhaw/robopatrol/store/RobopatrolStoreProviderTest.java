package ch.zhaw.robopatrol.store;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class RobopatrolStoreProviderTest {

    @Test
    public void testStoreIsShared() throws Exception {
        RobopatrolStore<Entity> store1, store2, store3;

        try (RobopatrolStoreProvider provider = new RobopatrolStoreProvider()) {
            store1 = provider.get(String.class);
            store2 = provider.get(String.class);
            store3 = provider.get(Integer.class);

            assertThat(store1, not(nullValue()));
            assertThat(store1, is(store2));
            assertThat(store3, is(not(store2)));
        } finally {
            assertThat(RobopatrolStoreProvider.getStoreFile(String.class).delete(), is(true));
            assertThat(RobopatrolStoreProvider.getStoreFile(Integer.class).delete(), is(true));
        }
    }

}