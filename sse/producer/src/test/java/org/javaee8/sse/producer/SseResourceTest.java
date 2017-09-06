package org.javaee8.sse.producer;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;

import java.io.IOException;
import java.net.URL;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.sse.SseEventSource;

/**
 * @author Daniel Contreras
 */
//@RunWith(Arquillian.class)
public class SseResourceTest {
/*
    @ArquillianResource
    private URL base;

    private Client sseClient;
    WebTarget target;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClass(SseResource.class);
    }

    @Before
    public void setup() {
        System.out.println("01");
        this.sseClient = ClientBuilder.newClient();
        System.out.println("02");
    }

    @After
    public void teardown() {

    }

    @Test
    @RunAsClient
    public void testClient() throws IOException {
        System.out.println("Client");
        System.out.println(base + "producer/register");
       System.out.println(this.sseClient.getConfiguration().toString());
    }
    
    @Test
    public void testServer() throws IOException {
        System.out.println("Server");
        System.out.println(base + "producer/register");
        //this.sseClient.target(base + "producer/register");
        System.out.println(this.sseClient.getConfiguration().getProperties().toString());
        System.out.println(this.sseClient.getConfiguration().getRuntimeType().toString());
        System.out.println(this.sseClient.getConfiguration().getInstances().toArray().toString());
        //this.sseClient.getHostnameVerifier();

        System.out.println(base + "producer/register");
        WebTarget target = sseClient.target(base + "producer/register");
        System.out.println("1");
        try (SseEventSource source = SseEventSource.target(target).build()) {
            System.out.println("2");
            source.register(System.out::println);
            System.out.println("3");
            source.open();
            System.out.println("5");
            Thread.sleep(500);      // Consume events for just 500 ms

        } catch (Exception e) {

            System.out.println("error!!!");
            System.out.println(e.getMessage());

        }

    }
*/
}
