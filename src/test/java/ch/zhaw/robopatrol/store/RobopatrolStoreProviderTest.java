package ch.zhaw.robopatrol.store;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class RobopatrolStoreProviderTest {

    @Test
    public void testStoreIsShared() throws Exception {
        try (
            RobopatrolStoreProvider provider = new RobopatrolStoreProvider();
            RobopatrolStore<Entity> store1 = provider.get(String.class);
            RobopatrolStore<Entity> store2 = provider.get(String.class);
            RobopatrolStore<Entity> store3 = provider.get(Integer.class);
        ) {
            assertThat(store1, not(nullValue()));
            assertThat(store1, is(store2));
            assertThat(store3, is(not(store2)));
        }
    }

}