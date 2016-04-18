package ch.zhaw.robopatrol;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import javax.ws.rs.core.*;
import java.io.IOException;

/**
 * Created by Schoggi on 18.04.2016.
 */
public class RestTest {

    @Test
    public void givenPathExists()throws Exception{
        assertTrue(Main.runServer());
        HttpUriRequest request = new HttpGet( "http://localhost:9998/wrongURI/");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        // Then
        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        request = new HttpGet("http://localhost:9998/schedule");
        httpResponse = HttpClientBuilder.create().build().execute(request);

        //assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
    }

}
