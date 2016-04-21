package ch.zhaw.robopatrol.res;

import ch.zhaw.robopatrol.store.RobopatrolStore;
import org.junit.Before;

public class WaypointTest {

    private Waypoint waypoint;
//    String key;
//    String input;
//    JSONObject expectedJson;

    @Before
    public void createWaypoint(){
        waypoint = new Waypoint(RobopatrolStore.inMemory());
    }

//    @Test
//    public void postGetTest(){
//        Response postResponse = waypoint.postMessage(input);
//        Response getResponse = waypoint.getMessageByKey(key);
//        JSONObject result = new JSONObject(getResponse.getEntity().toString());
//        assertEquals("compare expected and actual Entity of the Response", expectedJson.toString(), result.toString());
//        assertEquals("check length", 2, result.length());
//        assertEquals("check length of content", 1, result.getJSONObject("waypoints").length());
//        assertEquals("check Status Code of POST", 200, postResponse.getStatus());
//        assertEquals("check Status Code of GET", 200, getResponse.getStatus());
//    }
//
//    @Test
//    public void deleteTest(){
//        waypoint.deleteMessage(key);
//        Response response = waypoint.getMessageByKey(key);
//        assertEquals("Key should not be in DB, return empty String", "", response.getEntity());
//        assertEquals("check Status Code. Should be 404 not found", 404, response.getStatus());
//    }
}

