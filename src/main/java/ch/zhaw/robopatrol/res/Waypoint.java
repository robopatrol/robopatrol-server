package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.*;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Schoggi on 17.04.2016.
 * {"name":"someName","waypoints":{"1":{}"x-val":"int","y-val":"int"}, [...] "n":{[...]}}
 * name could also be amount of waypoints or an ID. Form of x/y coordinates must be like this!
 */
@Path("waypoint")
public class Waypoint {
    private XodusDAO db = new XodusDAO("waypoint");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMessage(String jsonString) {
        System.out.println(jsonString);
        JSONObject config = new JSONObject(jsonString);
        db.putToDatabase(config.getString("name"), config.getJSONObject("waypoints").toString());
        return Response.ok(config, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{key}")
    public Response getMessageByKey(@PathParam("key") String key) {
        String result;
        JSONObject waypoint;
        try {
            waypoint = new JSONObject(db.getByKey(key));
        } catch (NullPointerException e){
            return Response.status(404).entity("").build();
        }
        JSONObject waypoints = new JSONObject();
        waypoints.put("waypoints", waypoint);
        waypoints.put("name", key);
        result = waypoints.toString();
        System.out.println(result);
        return Response.status(200).entity(result).build();
    }

    @DELETE
    @Path("{key}")
    public Response deleteMessage(@PathParam("key") String key){
        db.deleteByKey(key);
        return Response.ok(key).build();
    }
}
