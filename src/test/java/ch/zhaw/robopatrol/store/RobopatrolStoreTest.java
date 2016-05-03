package ch.zhaw.robopatrol.store;

import ch.zhaw.robopatrol.store.Entity;
import ch.zhaw.robopatrol.store.RobopatrolStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mapdb.DBMaker;

import java.io.Serializable;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RobopatrolStoreTest {

    /** For the test we don't need to be strict about the generics. */
    private RobopatrolStore<Entity> store;

    @Before
    public void makeTestStore() {
        store = RobopatrolStore.inMemory();
    }

    @Test
    public void testPut() {
        TestEntity entity = new TestEntity("42", 42);
        store.put(entity);
        assertThat(store.get("42"), is(entity));
    }

    @Test
    public void testGetAll() {
        TestEntity entity = new TestEntity("42", 42);
        store.put(entity);
        TestEntity entity2 = new TestEntity("1337", 1337);
        store.put(entity2);

        assertThat(store.getAll(), hasSize(2));
        assertThat(store.getAll(), containsInAnyOrder(entity, entity2));
    }

    @Test
    public void testRemoveEntity() {
        TestEntity entity = new TestEntity("42", 42);
        store.put(entity);
        store.remove(entity.getId());

        assertThat(store.getAll(), hasSize(0));
        assertThat(store.get(entity.getId()), is(nullValue()));
    }

    /** Entities must be {@link Serializable} and must implement {@link Object#equals(Object)}. */
    private static final class TestEntity implements Entity {

        private String id;
        private Integer property;

        private TestEntity(String id, Integer property) {
            this.id = id;
            this.property = property;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

        /** Generated */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestEntity that = (TestEntity) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(property, that.property);
        }

        /** Generated */
        @Override
        public int hashCode() {
            return Objects.hash(id, property);
        }
    }

}
