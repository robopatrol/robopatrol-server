package ch.zhaw.robopatrol.res;
import ch.zhaw.robopatrol.*;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Schoggi on 17.04.2016.
 */
@Path("shedule")
public class Schedule {
    private XodusDAO db = new XodusDAO("Shedule");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(String jsonString) {
        JSONObject config = new JSONObject(jsonString);
        JSONObject schedulerConfig = config.getJSONObject("scheduler_config");
        db.putToDatabase("interval_sec", schedulerConfig.getString("interval_sec"));
        db.putToDatabase("functionCall", schedulerConfig.getString("functionCall"));
        return Response.ok(config, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage() {
        JSONObject task = new JSONObject();
        return task.put("scheduler_config", getJsonFromDB()).toString();
    }

    @DELETE
    @Path("{id}")
    public Response deleteMessage(@PathParam("id") String key){
        db.deleteByKey(key);
        return Response.ok(key).build();
    }

    JSONObject getJsonFromDB(){
        JSONObject data = new JSONObject();
        data.put("interval_sec", db.getByKey("interval_sec"));
        data.put("functionCall", db.getByKey("functionCall"));
        return data;
    }

}
