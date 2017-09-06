package org.javaee8.sse.producer;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

/**
 * @author Daniel Contreras
 */
@Path("/")
@Singleton
public class SseResource {

    @Context
    private Sse sse;

    private volatile SseBroadcaster sseBroadcaster;

    @PostConstruct
    public void init() {
        System.out.println("1");
        this.sseBroadcaster = sse.newBroadcaster();
        System.out.println("2");
    }

    @GET
    @Path("register")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@Context SseEventSink eventSink) {
        System.out.println("3");
        eventSink.send(sse.newEvent("welcome!"));
        System.out.println("4");
        sseBroadcaster.register(eventSink);
        System.out.println("5");
        eventSink.send(sse.newEvent("event1"));
        eventSink.send(sse.newEvent("event2"));
        eventSink.send(sse.newEvent("event3"));

        for (int i = 0; i < 20; i++) {

            sseBroadcaster.broadcast(sse.newEvent("Repeated" + i));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
