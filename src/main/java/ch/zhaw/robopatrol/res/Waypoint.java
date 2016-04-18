package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.XodusDAO;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Schoggi on 17.04.2016.
 * {"name":"someName","wayPoints":{"1":{}"x-val":"int","y-val":"int"}, [...] "n":{[...]}}
 * name could also be amount of waypoints or an ID. Form of x/y coordinates must be like this!
 */
@Path("waypoint")
public class Waypoint {
    private XodusDAO db = new XodusDAO("Schedule");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(String jsonString) {
        JSONObject config = new JSONObject(jsonString);
        JSONObject wayPoint = config.getJSONObject("name");
        db.putToDatabase("name", wayPoint.getString("name"));
        db.putToDatabase("wayPoints", wayPoint.getString("wayPoints"));
        return Response.ok(config, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage() {
        JSONObject task = new JSONObject();
        return task.put("name", getJsonFromDB()).toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{key}")
    public String getMessageByKey(@PathParam("key") String key) {
        JSONObject task = new JSONObject();
        return task.put("name", db.getByKey(key)).toString();
    }

    @DELETE
    @Path("{key}")
    public Response deleteMessage(@PathParam("key") String key){
        db.deleteByKey(key);
        return Response.ok(key).build();
    }

    JSONObject getJsonFromDB(){
        JSONObject name = new JSONObject();
        name.put("wayPoints", db.getByKey("wayPooints"));
        name.put("name", db.getByKey("name"));
        return name;
    }

}
