package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.store.Entity;
import ch.zhaw.robopatrol.store.RobopatrolStore;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
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
 *      "cron": { "minutes": [5] }
 *  }
 * </code></pre>
 */
final class Task implements Entity {

    private String id;

    private String name;

    private String description;

    private Cron cron;

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

    public Cron getCron() {
        return cron;
    }

    public void setCron(Cron cron) {
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

/**
 * Simplified cron for task scheduling. JSON examples:
 * <ul>
 * <li>Every day at 12:10 <=> {@code { hour: [12], minute: [10] }}</li>
 * <li>Every hour on mondays <=> {@code { weekdays: [0], minute: [0] }}</li>
 * <li>Every day at 12:00 and at 18:00 <=> {@code { hour: [12, 18], minute: [0] }}</li>
 * </ul>
 */
final class Cron implements Serializable {

    /** Monday = 0, Sunday = 6 */
    Integer[] weekdays = new Integer[0];

    /** 0-23 */
    Integer[] hours = new Integer[0];

    /** 0-59 */
    Integer[] minutes = new Integer[0];

    public Integer[] getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(Integer[] weekdays) {
        this.weekdays = weekdays;
    }

    public Integer[] getHours() {
        return hours;
    }

    public void setHours(Integer[] hours) {
        this.hours = hours;
    }

    public Integer[] getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer[] minutes) {
        this.minutes = minutes;
    }

    /** Generated. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cron cron = (Cron) o;
        return Arrays.equals(weekdays, cron.weekdays) &&
                Arrays.equals(hours, cron.hours) &&
                Arrays.equals(minutes, cron.minutes);
    }

    /** Generated. */
    @Override
    public int hashCode() {
        return Objects.hash(weekdays, hours, minutes);
    }

    /** Generated. */
    @Override
    public String toString() {
        return "Cron{" +
            "weekdays=" + Arrays.toString(weekdays) +
            ", hours=" + Arrays.toString(hours) +
            ", minutes=" + Arrays.toString(minutes) +
            '}';
    }
}
