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

/** Collection of {@link Task} resources. */
@Path("schedule")
public class Schedule {

    private final RobopatrolStore<Task> STORE;

    @Inject
    public Schedule(RobopatrolStoreProvider storeProvider) {
        this(storeProvider.get(Schedule.class));
    }

    Schedule(RobopatrolStore<Task> store) {
        STORE = store;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        // Sadly, GenericEnity is required for generic types, such as Collection<T>...
        return Response.ok(new GenericEntity<Collection<Task>>(STORE.getAll()) {}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTask(Task task) {
        task.generateId();
        STORE.put(task);
        return Response.ok(task).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("id") String id) {
        Task task = STORE.get(id);
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(task).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("id") String id, Task updatedTask) {
        Task task = STORE.get(id);
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        STORE.put(updatedTask);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTask(@PathParam("id") String id) {
        STORE.remove(id);
        return Response.ok().build();
    }

}

/**
 * Json example:
 * <pre><code>
 *  {
 *      "name": "Hourly Patrol",
 *      "description": "This should keep the cats at bay!",
 *      "cron": "0 12 * * *"
 *  }
 * </code></pre>
 */
final class Task implements Entity {

    private String id;

    /** Has to be an existing method in schedule.py */
    private String name  = "do_Patrol";

    private String description;

    /** Standard cron-style string:
     * <code><pre>
     * ┌───────────── min (0 - 59)
     * │ ┌────────────── hour (0 - 23)
     * │ │ ┌─────────────── day of month (1 - 31)
     * │ │ │ ┌──────────────── month (1 - 12)
     * │ │ │ │ ┌───────────────── day of week (0 - 6) (0 to 6 are Sunday to Saturday, or use names; 7 is Sunday, the same as 0)
     * │ │ │ │ │
     * * * * * *
     * </pre></code>
     */
    private String cron;

    private String displayName;

    private boolean takePhoto;
    
    private int takePhotoIntervalSec;
    
    private boolean active;
    
    public Task() { }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setDisplayName(String displayName){
      this.displayName = displayName;
    }

    public String getDisplayName(){
      return displayName;
    }

    /** Generated. */
    public boolean isTakePhoto() {
		return takePhoto;
	}

	public void setTakePhoto(boolean takePhoto) {
		this.takePhoto = takePhoto;
	}

	public int getTakePhotoIntervalSec() {
		return takePhotoIntervalSec;
	}

	public void setTakePhotoIntervalSec(int takePhotoIntervalSec) {
		this.takePhotoIntervalSec = takePhotoIntervalSec;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/** Generated. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(displayName, task.displayName) &&
                Objects.equals(cron, task.cron) &&
                Objects.equals(takePhoto,  task.takePhoto) &&
                Objects.equals(takePhotoIntervalSec, task.takePhotoIntervalSec) &&
                Objects.equals(active, task.active);
    }

    /** Generated. */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, cron, displayName, takePhoto, takePhotoIntervalSec);
    }

    /** Generated. */
    @Override
    public String toString() {
        return "Task{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", displayName='" + displayName + '\'' +
            ", cron=" + cron + '\'' +
            ", takePhoto='" + takePhoto + '\'' +
            ", takePhotoIntervalSec='" + takePhotoIntervalSec + '\'' + 
            ", active='" + active + '\'' +
            '}';
    }
}
