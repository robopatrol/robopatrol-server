package ch.zhaw.robopatrol.res;
import ch.zhaw.robopatrol.*;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Schoggi on 17.04.2016.
 * {"jobname":"someName","attributes":{"someKey":"someValue",[..]}}
 * jobname could also be an ID, someValue can be any JSON String
 */
@Path("schedule")
public class Scheduler {
    private XodusDAO db = new XodusDAO("Schedule");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMessage(String jsonString) {
        JSONObject config = new JSONObject(jsonString);
        db.putToDatabase(config.getString("jobname"), config.getJSONObject("attributes").toString());
        return Response.ok(config, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{key}")
    public Response getMessageByKey(@PathParam("key") String key) {
        String result;
        JSONObject task;
        JSONObject schedule = new JSONObject();
        try {
            task = new JSONObject(db.getByKey(key));
        }
        catch (NullPointerException e){
            return Response.status(404).entity("").build();
        }
        schedule.put("attributes", task);
        schedule.put("jobname", key);
        result = schedule.toString();
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("{key}")
    public Response deleteMessage(@PathParam("key") String key){
            db.deleteByKey(key);
        return Response.ok(key).build();
    }
}
