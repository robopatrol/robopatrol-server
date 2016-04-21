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

public class ScheduleTest {

    private Schedule schedule;

    @Before
    public void createSchedule() {
        schedule = new Schedule(RobopatrolStore.inMemory());
    }

    @Test
    public void testNotFound() {
        Response response = schedule.getTask("test");
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void testAdd() {
        Response response = schedule.addTask(task());
        assertThat(response.getStatus(), is(200));

        Task task = (Task)response.getEntity();
        task.setId(null); // Reset id
        assertThat(task, equalTo(task()));
    }

    @Test
    public void testGet() {
        Task task = (Task) schedule.addTask(task()).getEntity();

        Response response = schedule.getTask(task.getId());
        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is(task));
    }

    @Test
    public void testUpdate() {
        Task task = (Task) schedule.addTask(task()).getEntity();

        // Update entity locally.
        task.setDescription("Updated description.");
        assertThat((Task) schedule.getTask(task.getId()).getEntity(), is(not(task)));

        // Send update request. Entity should then be updated.
        Response response = schedule.updateTask(task.getId(), task);
        assertThat(response.getStatus(), is(200));
        assertThat((Task) schedule.getTask(task.getId()).getEntity(), is(task));
    }

    @Test
    public void testGetAll() {
        schedule.addTask(task());
        schedule.addTask(task());
        schedule.addTask(task());

        Response response = schedule.getAll();
        assertThat(response.getStatus(), is(200));

        Collection<Task> tasks = (Collection<Task>)response.getEntity();

        assertThat(tasks, hasSize(3));
        tasks.forEach(task -> {
            task.setId(null); // Reset id
            assertThat(task, is(task()));
        });
    }

    @Test
    public void testDelete() {
        Task task1 = (Task) schedule.addTask(task()).getEntity();
        Task task2 = (Task) schedule.addTask(task()).getEntity();

        Response response;

        response = schedule.deleteTask(task1.getId());
        assertThat(response.getStatus(), is(200));

        response = schedule.deleteTask(task2.getId());
        assertThat(response.getStatus(), is(200));

        response = schedule.getAll();
        assertThat(response.getStatus(), is(200));
        Collection<Task> tasks = (Collection<Task>)response.getEntity();
        assertThat(tasks, hasSize(0));
    }

    private Task task() {
        Cron cron = new Cron();
        cron.setMinutes(new Integer[] { 0, 30 });
        cron.setHours(new Integer[] { 9, 19 });

        Task task = new Task();
        task.setName("Test Task");
        task.setCron(cron);
        task.setDescription("Daily Patrol");

        return task;
    }

}