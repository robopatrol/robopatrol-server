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
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

public class ScheduleTest {

    private Schedule schedule;

    @Before
    public void createSchedule() {
        schedule = new Schedule(RobopatrolStoreProvider.inMemory());
    }

    @Test
    public void testInject(){
      RobopatrolStoreProvider provider = mock(RobopatrolStoreProvider.class);
      new Schedule(provider);
      verify(provider).get(eq(Schedule.class));
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
    public void testUpdateNoId() {
        Task t = task();

        Response response = schedule.updateTask("asdf", t);
        assertThat(response.getStatus(), is(404));
        assertThat(response.getStatusInfo(), is(Response.Status.NOT_FOUND));
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

    @Test
    public void testHash(){
      Task t1 = task();
      Task t2 = task();

      assertThat(t1.hashCode(), is(t2.hashCode()));
    }

    @Test
    public void testEquals(){
      Task t1 = task();
      Task t2 = task();

      assertThat(t1.equals(t2), is(true));
    }

    @Test
    public void testEquals2(){
      Task t1 = task();

      assertThat(t1.equals(null), is(false));
    }

    @Test
    public void testEquals3(){
      Task t1 = task();

      assertThat(t1.equals(new Object()), is(false));
    }

    @Test
    public void testEquals4(){
      Task t1 = task();
      t1.setId("asdf");
      Task t2 = task();
      t2.setId("asdf");

      assertThat(t1.equals(t2), is(true));
    }

    @Test
    public void testEquals5(){
      Task t1 = task();

      assertThat(t1.equals(t1), is(true));
    }

    @Test
    public void testEquals6(){
      Task t1 = task();
      t1.setId("foo");
      Task t2 = task();

      assertThat(t1.equals(t2), is(false));
    }

    @Test
    public void testEquals7(){
      Task t1 = task();
      t1.setCron("something");
      Task t2 = task();

      assertThat(t1.equals(t2), is(false));
    }

    @Test
    public void testEquals8(){
      Task t1 = task();
      t1.setDescription("foo");
      Task t2 = task();

      assertThat(t1.equals(t2), is(false));
    }

    @Test
    public void testEquals9(){
      Task t1 = task();
      t1.setName("foo");
      Task t2 = task();

      assertThat(t1.equals(t2), is(false));
    }

    @Test
    public void testEquals10(){
      Task t1 = task();
      Task t2 = task();
      t2.setDisplayName(null);

      assertThat(t1.equals(t2), is(true));
    }

    @Test
    public void testEquals11(){
      Task t1 = task();
      t1.setDisplayName("test");
      Task t2 = task();

      assertThat(t1.equals(t2), is(false));
    }

    @Test
    public void testToString(){
      Task t1 = task();
      t1.setDisplayName("my_task");

      String result = t1.toString();
      assertThat(result, containsString("Task"));
      assertThat(result, containsString("id="));
      assertThat(result, containsString("name="));
      assertThat(result, containsString("description="));
      assertThat(result, containsString("displayName="));
      assertThat(result, containsString("cron="));
      assertThat(result, containsString(t1.getName()));
      assertThat(result, containsString(t1.getDescription()));
      assertThat(result, containsString(t1.getDisplayName()));
      assertThat(result, containsString(t1.getCron()));
    }

    private Task task() {
        Task task = new Task();
        task.setName("Test Task");
        task.setCron("0 18 * * 1-5");
        task.setDescription("Workday Patrols");

        return task;
    }

}
