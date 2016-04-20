package ch.zhaw.robopatrol;

import ch.zhaw.robopatrol.res.*;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import javax.ws.rs.core.*;
import java.io.IOException;

/**
 * Created by Schoggi on 18.04.2016.
 */
public class RestTest {
    Scheduler scheduler;
    String key;
    String input;
    JSONObject expectedJson;

    @Before
    public void setup(){
        scheduler = new Scheduler();
        key = "someName";
        input = "{\"jobname\":\"someName\",\"attributes\":{\"someKey\":\"someValue\"}}";
        expectedJson = new JSONObject(input);
    }
    @Test
    public void postGetTest(){
        Response postResponse = scheduler.postMessage(input);
        Response getResponse = scheduler.getMessageByKey(key);
        JSONObject result = new JSONObject(getResponse.getEntity().toString());
        assertEquals("compare expected and actual Entity of the Response", expectedJson.toString(), result.toString());
        assertEquals("check length", 2, result.length());
        assertEquals("check Status Code of POST", 200, postResponse.getStatus());
        assertEquals("check Status Code of GET", 200, getResponse.getStatus());
    }

    @Test
    public void deleteTest(){
        scheduler.deleteMessage(key);
        Response response = scheduler.getMessageByKey(key);
        assertEquals("Key should not be in DB anymore -> empty String is shown", response.getEntity(), "");
        assertEquals("check Status Code. Should be 404 not found", 404, response.getStatus());
    }
    @Test
    public void negativeDeleteTest(){

    }
}
