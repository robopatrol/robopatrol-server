package ch.zhaw.robopatrol.res;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;


    /**
     * Created by Schoggi on 20.04.2016.
     */
public class WaypointTest {
    Waypoint waypoint;
    String key;
    String input;
    JSONObject expectedJson;

    @Before
    public void setup(){
        waypoint = new Waypoint();
        key = "someName";
        input = "{\"name\":\"someName\",\"waypoints\":{\"1\":{\"x-val\":\"int\",\"y-val\":\"int\"}}}";
        expectedJson = new JSONObject(input);
    }
    @Test
    public void postGetTest(){
        Response postResponse = waypoint.postMessage(input);
        Response getResponse = waypoint.getMessageByKey(key);
        JSONObject result = new JSONObject(getResponse.getEntity().toString());
        assertEquals("compare expected and actual Entity of the Response", expectedJson.toString(), result.toString());
        assertEquals("check length", 2, result.length());
        assertEquals("check length of content", 1, result.getJSONObject("waypoints").length());
        assertEquals("check Status Code of POST", 200, postResponse.getStatus());
        assertEquals("check Status Code of GET", 200, getResponse.getStatus());
    }

    @Test
    public void deleteTest(){
        waypoint.deleteMessage(key);
        Response response = waypoint.getMessageByKey(key);
        assertEquals("Key should not be in DB, return empty String", "", response.getEntity());
        assertEquals("check Status Code. Should be 404 not found", 404, response.getStatus());
    }
}

