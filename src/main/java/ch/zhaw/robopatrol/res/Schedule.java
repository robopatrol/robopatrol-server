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
        System.out.println(jsonString);
        JSONObject config = new JSONObject(jsonString);
        System.out.println(config.getString("jobname"));
        System.out.println(config.getJSONObject("attributes").toString());
        db.putToDatabase(config.getString("jobname"), config.getJSONObject("attributes").toString());

        return Response.ok(config, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage() {
        String result;
        JSONObject task = getJsonFromDB();
        result = task.toString();
        System.out.println(result);
        return result;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{key}")
    public Response getMessageByKey(@PathParam("key") String key) {
        String result;
        JSONObject task = new JSONObject();
        task.put("attributes", db.getByKey(key));
        task.put("jobname", key);
        result = task.toString();
        System.out.println(result);
        return Response.status(200).entity(result).build();
    }

    @DELETE
    @Path("{key}")
    public Response deleteMessage(@PathParam("key") String key){
        db.deleteByKey(key);
        return Response.ok(key).build();
    }

    JSONObject getJsonFromDB(){
        JSONObject name = new JSONObject();
        name.put("jobname", db.getByKey("somename"));
        return name;
    }

}
