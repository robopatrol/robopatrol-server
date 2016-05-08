package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.store.RobopatrolStore;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNot.not;

public class RouteTest {

    private Route route;

    @Before
    public void createSchedule() {
        route = new Route(RobopatrolStore.inMemory());
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

    private Waypoint point() {
        Waypoint point = new Waypoint();
        point.setName("Test Waypoint #1");
        point.setX(22);
        point.setY(33);

        return point;
    }

}