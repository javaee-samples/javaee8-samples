package org.javaee8.jaxrs.sseproducer.producer;

import javax.annotation.PostConstruct;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import org.javaee8.jaxrs.sseproducer.data.EventData;

/**
 * Produces server side events.
 *
 * @author Daniel Contreras
 */
@Path("sse")
public class SseResource {

    @Context
    private Sse sse;

    private volatile SseBroadcaster sseBroadcaster;

    @PostConstruct
    public void init() {
        this.sseBroadcaster = sse.newBroadcaster();
    }

    @GET
    @Path("register")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void register(@Context SseEventSink eventSink) {

        final Jsonb json = JsonbBuilder.create();
        eventSink.send(sse.newEvent("INIT", json.toJson(new EventData("event:intialized"))));

        sseBroadcaster.register(eventSink);

        for (int i = 0; i < 5; i++) {

            sseBroadcaster.broadcast(sse.newEvent("EVENT", json.toJson(new EventData("event:" + i))));

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        eventSink.send(sse.newEvent("FINISH", json.toJson(new EventData("event:finished"))));
    }
}
