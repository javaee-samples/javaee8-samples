package org.javaee8.jaxrs.sseproducer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.hamcrest.Matchers;
import org.javaee8.jaxrs.sseproducer.data.EventData;
import org.javaee8.jaxrs.sseproducer.producer.SseResource;
import org.javaee8.jaxrs.sseproducer.rest.RestApplication;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test example for the Server-Sent Events with the Jersey JAX-RS implementation.
 *
 * @author Daniel Contreras
 * @author David Matějček
 */
@RunWith(Arquillian.class)
public class SseResourceTest {

    private static final String[] EVENT_TYPES = {"INIT", "EVENT", "FINISH"};

    @ArquillianResource
    private URL base;

    private Client sseClient;
    private WebTarget target;
    private SseEventSource eventSource;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        return create(WebArchive.class).addClasses(RestApplication.class, SseResource.class, EventData.class);
    }


    /**
     * Initializes the client, target and the eventSource used to create event consumers
     */
    @Before
    public void setup() {
        // this is needed to avoid a conflict with embedded server, that can have
        // customized configuration and connector providers.
        final ClientConfig configuration = new ClientConfig();
        configuration.property(ClientProperties.CONNECT_TIMEOUT, 100);
        configuration.property(ClientProperties.READ_TIMEOUT, 5000);
        configuration.connectorProvider(new HttpUrlConnectorProvider());
        this.sseClient = ClientBuilder.newClient(configuration);
        this.target = this.sseClient.target(this.base + "rest/sse/register");
        this.eventSource = SseEventSource.target(this.target).build();
        System.out.println("SSE Event source created........");
        final Response response = this.target.request().get();
        assertThat("GET response status - server is not ready", response.getStatus(),
            Matchers.equalTo(Response.Status.OK.getStatusCode()));
    }


    /**
     * Closes all client resources.
     */
    @After
    public void teardown() {
        this.eventSource.close();
        System.out.println("Closed SSE Event source..");
        this.sseClient.close();
        System.out.println("Closed JAX-RS client..");
    }


    /**
     * Registers reaction on events, waits for events and checks their content.
     *
     * @throws Exception
     */
    @Test(timeout = 5000)
    @RunAsClient
    public void testSSE() throws Exception {
        final Queue<Throwable> asyncExceptions = new ConcurrentLinkedQueue<>();
        final Queue<EventData> receivedEvents = new ConcurrentLinkedQueue<>();
        // jsonb is thread safe!
        final Jsonb jsonb = JsonbBuilder.create();
        final Consumer<InboundSseEvent> onEvent = (sseEvent) -> {
            assertThat("event type", sseEvent.getName(), Matchers.isOneOf(EVENT_TYPES));
            final String data = sseEvent.readData();
            System.out.println("Data received as string:\n" + data);
            assertNotNull("data received as string", data);
            final EventData event = jsonb.fromJson(data, EventData.class);
            receivedEvents.add(event);
            assertThat("event.time", event.getTime(), instanceOf(Date.class));
            assertNotNull("event.id", event.getId());
            assertThat("event.comment", event.getComment(), Matchers.containsString("event:"));
        };
        this.eventSource.register(onEvent, asyncExceptions::add);
        System.out.println("Server Side Events Client registered in the test thread.");
        // following line starts acceptation of events.
        this.eventSource.open();
        // don't end the test until we have all events or timeout or error comes.
        // this is not an obvious implementation, we only need to hold the test until all events
        // are asynchronously processed.
        while (receivedEvents.size() <= 5 && asyncExceptions.isEmpty()) {
            Thread.sleep(10L);
        }
        assertThat("receiver exceptions", asyncExceptions, Matchers.emptyIterable());
    }
}
