package ch.zhaw.robopatrol.res;

/**
 * Created by simmljoe on 17/04/16.
 */

import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Takes content via GET method and does a very static transformation and puts it to the Database.
 * TestInput: robopatrol/putConfig/{"scheduler_config":{"interval_sec":"3600","functionCall":"foo(bar)"}}
 * TODO: Testing
 */
@Path("robopatrol/putConfig/{jsonString}")
public class ConfigWriter {
    XodusDAO db = new XodusDAO("Configuration");

    @GET
    @Consumes("application/json")
    @Produces("")
    public Response getMessage(@PathParam("jsonString") String jsonString) {
        JSONObject config = new JSONObject(jsonString);
        JSONObject schedulerConfig = config.getJSONObject("scheduler_config");
        db.putToDatabase("interval_sec", schedulerConfig.getString("interval_sec"));
        db.putToDatabase("functionCall", schedulerConfig.getString("functionCall"));
        return Response.ok(config, MediaType.APPLICATION_JSON).build();
    }
}
