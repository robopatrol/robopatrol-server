package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.store.Entity;
import ch.zhaw.robopatrol.store.RobopatrolStore;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/** Collection of {@link Task} resources. */
@Path("schedule")
public class Schedule {

    // TODO: Consider setting up dependency injection via @Context for this.
    private static final RobopatrolStore<Task> STORE = RobopatrolStore.forClass(Schedule.class);

    private final RobopatrolStore<Task> store;

    public Schedule() {
        store = STORE;
    }

    Schedule(RobopatrolStore<Task> store) {
        this.store = store;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        // Sadly, GenericEnity is required for generic types, such as Collection<T>...
        return Response.ok(new GenericEntity<Collection<Task>>(store.getAll()) {}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTask(Task task) {
        task.generateId();
        store.put(task);
        return Response.ok(task).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("id") String id) {
        Task task = store.get(id);
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(task).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("id") String id, Task updatedTask) {
        Task task = store.get(id);
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        store.put(updatedTask);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTask(@PathParam("id") String id) {
        store.remove(id);
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

    private String name;

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

    /** Generated. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(cron, task.cron);
    }

    /** Generated. */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, cron);
    }

    /** Generated. */
    @Override
    public String toString() {
        return "Task{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", cron=" + cron +
            '}';
    }
}
