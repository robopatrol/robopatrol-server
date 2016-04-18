package ch.zhaw.robopatrol.res;
import ch.zhaw.robopatrol.*;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Schoggi on 17.04.2016.
 * {"jobname":"someName","attributes":{"someKey":"someValue",[...]}
 * jobname could also be an ID, someValue can be any JSON String
 */
@Path("schedule")
public class Schedule {
    private XodusDAO db = new XodusDAO("Schedule");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(String jsonString) {
        JSONObject config = new JSONObject(jsonString);
        JSONObject schedulerConfig = config.getJSONObject("scheduler_config");
        db.putToDatabase("jobName", schedulerConfig.getString("jobname"));
        db.putToDatabase("attributes", schedulerConfig.getString("attributes"));
        return Response.ok(config, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage() {
        JSONObject task = new JSONObject();
        return task.put("jobname", getJsonFromDB()).toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{key}")
    public String getMessageByKey(@PathParam("key") String key) {
        JSONObject task = new JSONObject();
        return task.put("jobname", db.getByKey(key)).toString();
    }

    @DELETE
    @Path("{key}")
    public Response deleteMessage(@PathParam("key") String key){
        db.deleteByKey(key);
        return Response.ok(key).build();
    }

    JSONObject getJsonFromDB(){
        JSONObject name = new JSONObject();
        name.put("attributes", db.getByKey("attributes"));
        name.put("jobname", db.getByKey("jobName"));
        return name;
    }

}
