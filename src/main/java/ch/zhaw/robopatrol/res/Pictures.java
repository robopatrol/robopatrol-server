package ch.zhaw.robopatrol.res;

import java.util.Collection;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.zhaw.robopatrol.store.Entity;
import ch.zhaw.robopatrol.store.RobopatrolStore;
import ch.zhaw.robopatrol.store.RobopatrolStoreProvider;

/** Collection of {@link Picture} resources. */
@Path("pictures")
public class Pictures {

    private final RobopatrolStore<Picture> STORE;

    @Inject
    public Pictures(RobopatrolStoreProvider storeProvider) {
        this(storeProvider.get(Pictures.class));
    }

    Pictures(RobopatrolStore<Picture> store) {
        STORE = store;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        // Sadly, GenericEnity is required for generic types, such as Collection<T>...
        return Response.ok(new GenericEntity<Collection<Picture>>(STORE.getAll()) {}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPicture(Picture picture) {
    	picture.generateId();
    	System.out.println(picture);
        STORE.put(picture);
        return Response.ok(picture).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPicture(@PathParam("id") String id) {
        Picture picture = STORE.get(id);
        if (picture == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(picture).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePicture(@PathParam("id") String id, Picture updatedPicture) {
        Picture picture = STORE.get(id);
        if (picture == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        STORE.put(updatedPicture);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deletePicture(@PathParam("id") String id) {
        STORE.remove(id);
        return Response.ok().build();
    }

}

/**
 * Json example:
 * <pre><code>
 *  {
 *      "name": "Grumpy cat",
 *      "image": "..............................."
 *  }
 * </code></pre>
 */
final class Picture implements Entity {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

    private String image;

    /** Generated. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(id, picture.id) &&
                Objects.equals(name, picture.name) &&
                Objects.equals(image, picture.image);
    }

    /** Generated. */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, image);
    }

    /** Generated. */
    @Override
    public String toString() {
        return "Picture{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
    
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
