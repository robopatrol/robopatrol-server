package ch.zhaw.robopatrol.res;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class DummyResource {

    public static final String PATH = "test";

    public static final String CONTENT = "This is the test content";

    @GET
    public String test() {
        return CONTENT;
    }

}
