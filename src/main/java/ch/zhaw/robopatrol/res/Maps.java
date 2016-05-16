package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.store.Entity;
import ch.zhaw.robopatrol.store.RobopatrolStore;
import ch.zhaw.robopatrol.store.RobopatrolStoreProvider;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/** Collection of {@link Map} resources. */
@Path("maps")
public class Maps {

    private final RobopatrolStore<Map> STORE;

    @Inject
    public Maps(RobopatrolStoreProvider storeProvider) {
        this(storeProvider.get(Maps.class));
    }

    Maps(RobopatrolStore<Map> store) {
        STORE = store;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        // Sadly, GenericEnity is required for generic types, such as Collection<T>...
        return Response.ok(new GenericEntity<Collection<Map>>(STORE.getAll()) {}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMap(Map map) {
        map.generateId();
        STORE.put(map);
        return Response.ok(map).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMap (@PathParam("id") String id) {
        Map map = STORE.get(id);
        if (map == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(map).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMap(@PathParam("id") String id, Map updatedMap) {
        Map map = STORE.get(id);
        if (map == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        STORE.put(updatedMap);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteMap(@PathParam("id") String id) {
        STORE.remove(id);
        return Response.ok().build();
    }

}

/**
 * Json example:
 * <pre><code>
 *  {
 *      "name": "Livingroom",
 *      "filename": "xy/map.xy"
 *  }
 * </code></pre>
 */
final class Map implements Entity {

    private String id;

    private String name;

    private String filename;

    public Map() { }

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) { this.filename = filename; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Map map = (Map) o;

        if (id != null ? !id.equals(map.id) : map.id != null) return false;
        if (name != null ? !name.equals(map.name) : map.name != null) return false;
        return filename != null ? filename.equals(map.filename) : map.filename == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Map{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}

