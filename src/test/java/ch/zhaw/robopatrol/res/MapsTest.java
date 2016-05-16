package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.store.RobopatrolStore;
import ch.zhaw.robopatrol.store.RobopatrolStoreProvider;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

public class MapsTest {

    private Maps maps;

    @Before
    public void createSchedule() {
        maps = new Maps(RobopatrolStoreProvider.inMemory());
    }

    @Test
    public void testInject(){
        RobopatrolStoreProvider provider = mock(RobopatrolStoreProvider.class);
        new Maps(provider);
        verify(provider).get(eq(Maps.class));
    }

    @Test
    public void testNotFound() {
        Response response = maps.getMap("test");
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void testAdd() {
        Response response = maps.addMap(map());
        assertThat(response.getStatus(), is(200));

        Map map = (Map)response.getEntity();
        map.setId(null); // Reset id
        assertThat(map, equalTo(map()));
    }

    @Test
    public void testGet() {
        Map map = (Map) maps.addMap(map()).getEntity();

        Response response = maps.getMap(map.getId());
        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is(map));
    }

    @Test
    public void testUpdate() {
        Map map = (Map) maps.addMap(map()).getEntity();

        // Update entity locally.
        map.setFilename("/12312/12");
        assertThat((Map) maps.getMap(map.getId()).getEntity(), is(not(map)));

        // Send update request. Entity should then be updated.
        Response response = maps.updateMap(map.getId(), map);
        assertThat(response.getStatus(), is(200));
        assertThat((Map) maps.getMap(map.getId()).getEntity(), is(map));
    }

    @Test
    public void testUpdateNoId() {
        Map map = map();

        // Send update request. Entity should then be updated.
        Response response = maps.updateMap("asdf", map);
        assertThat(response.getStatus(), is(404));
        assertThat(response.getStatusInfo(), is(Response.Status.NOT_FOUND));
    }

    @Test
    public void testGetAll() {
        maps.addMap(map());
        maps.addMap(map());
        maps.addMap(map());


        Response response = maps.getAll();
        assertThat(response.getStatus(), is(200));

        Collection<Map> maps = (Collection<Map>)response.getEntity();

        assertThat(maps, hasSize(3));
        maps.forEach(map -> {
            map.setId(null); // Reset id
            assertThat(map, is(map()));
        });
    }

    @Test
    public void testDelete() {
        Map map1 = (Map) maps.addMap(map()).getEntity();
        Map map2 = (Map) maps.addMap(map()).getEntity();

        Response response;

        response = maps.deleteMap(map1.getId());
        assertThat(response.getStatus(), is(200));

        response = maps.deleteMap(map2.getId());
        assertThat(response.getStatus(), is(200));

        response = maps.getAll();
        assertThat(response.getStatus(), is(200));
        Collection<Map> maps = (Collection<Map>)response.getEntity();
        assertThat(maps, hasSize(0));
    }

    @Test
    public void testEquals(){
        Map p1 = map();
        Map p2 = map();

        assertThat(p1.equals(p2), is(true));
    }

    @Test
    public void testEquals2(){
        Map p1 = map();
        assertThat(p1.equals(p1), is(true));
    }

    @Test
    public void testEquals3(){
        Map p1 = map();
        assertThat(p1.equals(null), is(false));
    }

    @Test
    public void testEquals4(){
        Map p1 = map();
        assertThat(p1.equals(new Object()), is(false));
    }

    @Test
    public void testEquals5(){
        Map p1 = map();
        Map p2 =  map();
        p2.setName(null);
        assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals6(){
        Map p1 = map();
        Map p2 =  map();
        p2.setFilename("asdfasdf");
        assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals7(){
        Map p1 = map();
        p1.setId("id");
        Map p2 =  map();
        p2.setId("foo");
        assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals8(){
        Map p1 = map();
        p1.setName(null);
        Map p2 =  map();
        assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals9(){
        Map p1 = map();
        p1.setName(null);
        Map p2 =  map();
        p2.setName(null);
        assertThat(p1.equals(p2), is(true));
    }

    @Test
    public void testNotEquals(){
        Map p1 = map();
        Map p2 = map();
        p2.setId("asdf");

        assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testHash(){
        Map p1 = map();
        Map p2 =  map();
        assertThat(p1.hashCode(), is(p2.hashCode()));
    }

    @Test
    public void testHash2(){
        Map p1 = map();
        p1.setId("test");
        Map p2 =  map();
        p2.setId("test");
        assertThat(p1.hashCode(), is(p2.hashCode()));
    }

    @Test
    public void testHash3(){
        Map p1 = map();
        p1.setName(null);
        Map p2 =  map();
        p2.setName(null);
        assertThat(p1.hashCode(), is(p2.hashCode()));
    }

    @Test
    public void testToString(){
        Map p1 = map();

        String result = p1.toString();
        assertThat(result, containsString("Map"));
        assertThat(result, containsString("id="));
        assertThat(result, containsString("name="));
        assertThat(result, containsString("filename="));
        assertThat(result, containsString(p1.getName()));
        assertThat(result, containsString(p1.getFilename()));
    }

    private Map map() {
        Map map = new Map();
        map.setName("Test Map #1");
        map.setFilename("test.xy");

        return map;
    }

}
