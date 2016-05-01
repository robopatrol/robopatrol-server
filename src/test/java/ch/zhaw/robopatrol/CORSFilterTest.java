package ch.zhaw.robopatrol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CORSFilterTest {

    @Spy
    private CORSFilter filter = new CORSFilter();

    @Mock
    private ContainerRequestContext request;

    @Mock
    private ContainerResponseContext response;

    @Test
    public void filter() {
        MultivaluedMap<String, Object> responseHeaders = new MultivaluedHashMap<>();
        when(response.getHeaders()).thenReturn(responseHeaders);

        filter.filter(request, response);

        assertThat(responseHeaders.get("Access-Control-Allow-Origin"), not(nullValue()));
        assertThat(responseHeaders.get("Access-Control-Allow-Headers"), not(nullValue()));
        assertThat(responseHeaders.get("Access-Control-Allow-Credentials"), not(nullValue()));
        assertThat(responseHeaders.get("Access-Control-Allow-Methods"), not(nullValue()));
    }

}