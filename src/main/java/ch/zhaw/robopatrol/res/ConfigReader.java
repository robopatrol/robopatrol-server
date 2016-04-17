package ch.zhaw.robopatrol.res;

/**
 * Created by simmljoe on 17/04/16.
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.json.*;

/**
 * Delivers current configuration if robopatrol/getConfig is accessed
 * TODO: Testing
 */
@Path("robopatrol/getConfig")
public class ConfigReader {
    XodusDAO db = new XodusDAO("Configuration");
    public ConfigReader() {

    }

    @GET
    @Produces("application/json")
    public String getMessage() {
        JSONObject task = new JSONObject();
        return task.put("scheduler_config", getJsonFromDB()).toString();
    }

    JSONObject getJsonFromDB(){
        JSONObject data = new JSONObject();
        data.put("interval_sec", db.getByKey("interval_sec"));
        data.put("functionCall", db.getByKey("functionCall"));
        return data;
    }

}
