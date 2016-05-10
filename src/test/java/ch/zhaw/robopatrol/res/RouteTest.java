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

public class RouteTest {

    private Route route;

    @Before
    public void createSchedule() {
        route = new Route(RobopatrolStoreProvider.inMemory());
    }

    @Test
    public void testInject(){
      RobopatrolStoreProvider provider = mock(RobopatrolStoreProvider.class);
      new Route(provider);
      verify(provider).get(eq(Route.class));
    }

    @Test
    public void testNotFound() {
        Response response = route.getPoint("test");
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void testAdd() {
        Response response = route.addPoint(point());
        assertThat(response.getStatus(), is(200));

        Waypoint point = (Waypoint)response.getEntity();
        point.setId(null); // Reset id
        assertThat(point, equalTo(point()));
    }

    @Test
    public void testGet() {
        Waypoint point = (Waypoint) route.addPoint(point()).getEntity();

        Response response = route.getPoint(point.getId());
        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is(point));
    }

    @Test
    public void testUpdate() {
        Waypoint point = (Waypoint) route.addPoint(point()).getEntity();

        // Update entity locally.
        point.setX(123);
        assertThat((Waypoint) route.getPoint(point.getId()).getEntity(), is(not(point)));

        // Send update request. Entity should then be updated.
        Response response = route.updatePoint(point.getId(), point);
        assertThat(response.getStatus(), is(200));
        assertThat((Waypoint) route.getPoint(point.getId()).getEntity(), is(point));
    }

    @Test
    public void testUpdateNoId() {
        Waypoint point = point();

        // Send update request. Entity should then be updated.
        Response response = route.updatePoint("asdf", point);
        assertThat(response.getStatus(), is(404));
        assertThat(response.getStatusInfo(), is(Response.Status.NOT_FOUND));
    }

    @Test
    public void testGetAll() {
        route.addPoint(point());
        route.addPoint(point());
        route.addPoint(point());


        Response response = route.getAll();
        assertThat(response.getStatus(), is(200));

        Collection<Waypoint> points = (Collection<Waypoint>)response.getEntity();

        assertThat(points, hasSize(3));
        points.forEach(point -> {
            point.setId(null); // Reset id
            assertThat(point, is(point()));
        });
    }

    @Test
    public void testDelete() {
        Waypoint point1 = (Waypoint) route.addPoint(point()).getEntity();
        Waypoint point2 = (Waypoint) route.addPoint(point()).getEntity();

        Response response;

        response = route.deletePoint(point1.getId());
        assertThat(response.getStatus(), is(200));

        response = route.deletePoint(point2.getId());
        assertThat(response.getStatus(), is(200));

        response = route.getAll();
        assertThat(response.getStatus(), is(200));
        Collection<Waypoint> points = (Collection<Waypoint>)response.getEntity();
        assertThat(points, hasSize(0));
    }

    @Test
    public void testEquals(){
      Waypoint p1 = point();
      Waypoint p2 = point();

      assertThat(p1.equals(p2), is(true));
    }

    @Test
    public void testEquals2(){
      Waypoint p1 = point();
      assertThat(p1.equals(p1), is(true));
    }

    @Test
    public void testEquals3(){
      Waypoint p1 = point();
      assertThat(p1.equals(null), is(false));
    }

    @Test
    public void testEquals4(){
      Waypoint p1 = point();
      assertThat(p1.equals(new Object()), is(false));
    }

    @Test
    public void testEquals5(){
      Waypoint p1 = point();
      Waypoint p2 =  point();
      p2.setName(null);
      assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals6(){
      Waypoint p1 = point();
      Waypoint p2 =  point();
      p2.setY(5);
      assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals7(){
      Waypoint p1 = point();
      p1.setId("id");
      Waypoint p2 =  point();
      p2.setId("foo");
      assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals8(){
      Waypoint p1 = point();
      p1.setName(null);
      Waypoint p2 =  point();
      assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testEquals9(){
      Waypoint p1 = point();
      p1.setName(null);
      Waypoint p2 =  point();
      p2.setName(null);
      assertThat(p1.equals(p2), is(true));
    }

    @Test
    public void testNotEquals(){
      Waypoint p1 = point();
      Waypoint p2 = point();
      p2.setId("asdf");

      assertThat(p1.equals(p2), is(false));
    }

    @Test
    public void testHash(){
      Waypoint p1 = point();
      Waypoint p2 =  point();
      assertThat(p1.hashCode(), is(p2.hashCode()));
    }

    @Test
    public void testHash2(){
      Waypoint p1 = point();
      p1.setId("test");
      Waypoint p2 =  point();
      p2.setId("test");
      assertThat(p1.hashCode(), is(p2.hashCode()));
    }

    @Test
    public void testHash3(){
      Waypoint p1 = point();
      p1.setName(null);
      Waypoint p2 =  point();
      p2.setName(null);
      assertThat(p1.hashCode(), is(p2.hashCode()));
    }

    @Test
    public void testToString(){
      Waypoint p1 = point();

      String result = p1.toString();
      assertThat(result, containsString("Point"));
      assertThat(result, containsString("id="));
      assertThat(result, containsString("x="));
      assertThat(result, containsString("y="));
      assertThat(result, containsString("name="));
      assertThat(result, containsString(p1.getName()));
      assertThat(result, containsString(Integer.toString(p1.getX())));
      assertThat(result, containsString(Integer.toString(p1.getY())));
    }

    private Waypoint point() {
        Waypoint point = new Waypoint();
        point.setName("Test Waypoint #1");
        point.setX(22);
        point.setY(33);

        return point;
    }

}
