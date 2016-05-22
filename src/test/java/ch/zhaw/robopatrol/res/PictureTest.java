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

public class PictureTest {

    private Pictures pics;

    @Before
    public void setUp() {
        pics = new Pictures(RobopatrolStoreProvider.inMemory());
    }

    @Test
    public void testInject(){
      RobopatrolStoreProvider provider = mock(RobopatrolStoreProvider.class);
      new Pictures(provider);
      verify(provider).get(eq(Pictures.class));
    }

    @Test
    public void testNotFound() {
        Response response = pics.getPicture("123");
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void testAdd() {
        Response response = pics.addPicture(picture());
        assertThat(response.getStatus(), is(200));

        Picture pic = (Picture)response.getEntity();
        pic.setId(null); // Reset id
        assertThat(pic, equalTo(picture()));
    }

    @Test
    public void testGet() {
        Picture pic = (Picture) pics.addPicture(picture()).getEntity();

        Response response = pics.getPicture(pic.getId());
        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is(pic));
    }

    @Test
    public void testUpdate() {
        Picture pic = (Picture) pics.addPicture(picture()).getEntity();

        // Update entity locally.
        pic.setName("Updated Name.");
        assertThat((Picture) pics.getPicture(pic.getId()).getEntity(), is(not(pic)));

        // Send update request. Entity should then be updated.
        Response response = pics.updatePicture(pic.getId(), pic);
        assertThat(response.getStatus(), is(200));
        assertThat((Picture) pics.getPicture(pic.getId()).getEntity(), is(pic));
    }

    @Test
    public void testUpdateNoId() {
        Picture t = picture();

        Response response = pics.updatePicture("asdf", t);
        assertThat(response.getStatus(), is(404));
        assertThat(response.getStatusInfo(), is(Response.Status.NOT_FOUND));
    }

    @Test
    public void testGetAll() {
        pics.addPicture(picture());
        pics.addPicture(picture());
        pics.addPicture(picture());

        Response response = pics.getAll();
        assertThat(response.getStatus(), is(200));

        Collection<Picture> pics = (Collection<Picture>)response.getEntity();

        assertThat(pics, hasSize(3));
        pics.forEach(pic -> {
            pic.setId(null); // Reset id
            assertThat(pic, is(picture()));
        });
    }

    @Test
    public void testDelete() {
        Picture pic1 = (Picture) pics.addPicture(picture()).getEntity();
        Picture pic2 = (Picture) pics.addPicture(picture()).getEntity();

        Response response;

        response = pics.deletePicture(pic1.getId());
        assertThat(response.getStatus(), is(200));

        response = pics.deletePicture(pic2.getId());
        assertThat(response.getStatus(), is(200));

        response = pics.getAll();
        assertThat(response.getStatus(), is(200));
        Collection<Picture> pics = (Collection<Picture>)response.getEntity();
        assertThat(pics, hasSize(0));
    }

    @Test
    public void testHash(){
      Picture t1 = picture();
      Picture t2 = picture();

      assertThat(t1.hashCode(), is(t2.hashCode()));
    }

    @Test
    public void testEquals(){
      Picture t1 = picture();
      Picture t2 = picture();

      assertThat(t1.equals(t2), is(true));
    }

    @Test
    public void testEquals2(){
      Picture t1 = picture();

      assertThat(t1.equals(null), is(false));
    }

    @Test
    public void testEquals3(){
      Picture t1 = picture();

      assertThat(t1.equals(new Object()), is(false));
    }

    @Test
    public void testEquals4(){
      Picture t1 = picture();
      t1.setId("asdf");
      Picture t2 = picture();
      t2.setId("asdf");

      assertThat(t1.equals(t2), is(true));
    }

    @Test
    public void testEquals5(){
      Picture t1 = picture();

      assertThat(t1.equals(t1), is(true));
    }

    @Test
    public void testEquals6(){
      Picture t1 = picture();
      t1.setId("foo");
      Picture t2 = picture();

      assertThat(t1.equals(t2), is(false));
    }


    @Test
    public void testToString(){
      Picture t1 = picture();
      t1.setId("test");

      String result = t1.toString();
      assertThat(result, containsString("Picture"));
      assertThat(result, containsString("id="));
      assertThat(result, containsString("name="));
      assertThat(result, containsString(t1.getName()));
      assertThat(result, containsString(t1.getId()));
    }

    private Picture picture() {
        Picture pic = new Picture();
        pic.setName("Test Picture");
        pic.setImage("asdf");

        return pic;
    }

}
