package ch.zhaw.robopatrol.res;

/**
 * Created by lneiva on 11/04/16.
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * TODO: This is just a dummy resource.
 */
@Path("/robopatrol")
public class TestResource {

    public TestResource() {

    }

    @GET
    @Produces("text/plain")
    public String getMessage() {
        return "Hello World";
    }

}
