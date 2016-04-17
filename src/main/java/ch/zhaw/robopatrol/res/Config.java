package ch.zhaw.robopatrol.res;

/**
 * Created by simmljoe on 17/04/16.
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.json.*;
import org.json.*;

/**
 * TODO: Connect to DB
 */
@Path("robopatrol/getConfig")
public class Config {

    public Config() {

    }

    @GET
    @Produces("application/json")
    public String getMessage() {
        JSONArray array;
        JSONObject task = new JSONObject();
        return task.put("Task", sampleData()).toString();
    }

    JSONObject sampleData(){
        JSONObject sampleData = new JSONObject();
        sampleData.put("StartDate", "01.01.2001");
        sampleData.put("StartTime", "10:30");
        sampleData.put("Mode","random");
        sampleData.put("repeat", "yes");
        return sampleData;
    }
}