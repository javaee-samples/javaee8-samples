package org.javaee8.sse.producer;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.javaee8.sse.data.EventData;
import org.javaee8.sse.rest.RestApplication;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Daniel Contreras
 */
@RunWith(Arquillian.class)
public class SseResourceTest {

    @ArquillianResource
    private URL base;

    private Client sseClient;
    private WebTarget target;

    SseEventSource eventSource;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClasses(RestApplication.class, SseResource.class, EventData.class, JsonbBuilder.class, Jsonb.class);
    }

    @Before
    public void setup() {
        this.sseClient = ClientBuilder.newClient();
        this.target = this.sseClient.target(base + "rest/sse/register");
        eventSource = SseEventSource.target(target).build();
        System.out.println("SSE Event source created........");
    }

    @After
    public void teardown() {
        eventSource.close();
        System.out.println("Closed SSE Event source..");
        sseClient.close();
        System.out.println("Closed JAX-RS client..");
    }

    String[] types = {"INIT", "EVENT", "FINISH"};

    @Test
    @RunAsClient
    public void testSSE() throws IOException {

        Jsonb jsonb = JsonbBuilder.create();

        System.out.println("SSE Client triggered in thread " + Thread.currentThread().getName());
        try {
            eventSource.register(
                    (sseEvent)
                    -> {
                assertTrue(Arrays.asList(types).contains(sseEvent.getName()));
                assertNotNull(sseEvent.readData());
                EventData event = jsonb.fromJson(sseEvent.readData(), EventData.class);
                assertThat(event.getTime(), instanceOf(Date.class));
                assertNotNull(event.getId());
                assertTrue(event.getComment().contains("event:"));
                System.out.println("\nSSE Event received :: " + event.toString() +"\n");
                
            },
                    (e) -> e.printStackTrace());

            eventSource.open();
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Error on SSE Test");
            System.out.println(e.getMessage());
        }

    }

}
