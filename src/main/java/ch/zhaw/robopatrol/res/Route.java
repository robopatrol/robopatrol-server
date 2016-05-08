package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.store.Entity;
import ch.zhaw.robopatrol.store.RobopatrolStore;
import ch.zhaw.robopatrol.store.RobopatrolStoreProvider;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/** Collection of {@link Waypoint} resources. */
@Path("route")
public class Route {

    private final RobopatrolStore<Waypoint> STORE;

    @Inject
    public Route(RobopatrolStoreProvider storeProvider) {
        this(storeProvider.get(Route.class));
    }

    Route(RobopatrolStore<Waypoint> store) {
        STORE = store;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        // Sadly, GenericEnity is required for generic types, such as Collection<T>...
        return Response.ok(new GenericEntity<Collection<Waypoint>>(STORE.getAll()) {}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPoint(Waypoint point) {
        point.generateId();
        STORE.put(point);
        return Response.ok(point).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoint(@PathParam("id") String id) {
        Waypoint point = STORE.get(id);
        if (point == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(point).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePoint(@PathParam("id") String id, Waypoint updatedPoint) {
        Waypoint point = STORE.get(id);
        if (point == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        STORE.put(updatedPoint);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePoint(@PathParam("id") String id) {
        STORE.remove(id);
        return Response.ok().build();
    }

}

/**
 * Json example:
 * <pre><code>
 *  {
 *      "name": "Point #1",
 *      "x": "234",
 *      "y": "25"
 *  }
 * </code></pre>
 */
final class Waypoint implements Entity {

    private String id;

    private String name;

    private int x;

    private int y;

    public Waypoint() { }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) { this.x = x; }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /** Generated. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Waypoint waypoint = (Waypoint) o;

        if (x != waypoint.x) return false;
        if (y != waypoint.y) return false;
        if (id != null ? !id.equals(waypoint.id) : waypoint.id != null) return false;
        return name != null ? name.equals(waypoint.name) : waypoint.name == null;

    }

    /** Generated. */
    @Override
    public String toString() {
        return "Point{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
