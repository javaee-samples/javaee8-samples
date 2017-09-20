package org.javaee8.sse.producer;

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
import org.javaee8.sse.data.EventData;

/**
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
        
        Jsonb jsonb = JsonbBuilder.create();

        eventSink.send(sse.newEvent("INIT",new EventData("event:intialized").toString()));

        sseBroadcaster.register(eventSink);

        for (int i = 0; i < 5; i++) {

            sseBroadcaster.broadcast(sse.newEvent("EVENT",new EventData("event:"+i).toString()));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        eventSink.send(sse.newEvent("FINISH",new EventData("event:finished").toString()));
    }
    
}
