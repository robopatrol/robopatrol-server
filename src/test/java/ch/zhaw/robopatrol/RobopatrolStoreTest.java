package ch.zhaw.robopatrol;

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
import static org.hamcrest.Matchers.is;

public class RobopatrolStoreTest {

    /** For the test we don't need to be strict about the generics. */
    private RobopatrolStore<Entity> store;

    @Before
    public void makeTestStore() {
        store = RobopatrolStore.inMemory();
    }

    @Test
    public void testEntity() {
        TestEntity entity = new TestEntity("42", 42);
        store.put("key", entity);
        assertThat(store.get("key"), is(entity));
    }

    /** Entities must be {@link Serializable} and must implement {@link Object#equals(Object)}. */
    private static class TestEntity implements Entity {

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

        @Override
        public boolean equals(Object obj) {
            if (!obj.getClass().equals(getClass())) {
                return false;
            }
            TestEntity that = (TestEntity) obj;
            return (Objects.equals(this.id, that.id) &&
                    Objects.equals(this.property, that.property));
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, property);
        }
    }

}
