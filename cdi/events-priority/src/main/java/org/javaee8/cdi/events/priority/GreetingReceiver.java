package org.javaee8.cdi.events.priority;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

import java.io.Serializable;

import javax.annotation.Priority;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;

/**
 * @author Radim Hanus
 * @author Arun Gupta
 */
@SessionScoped
public class GreetingReceiver implements EventReceiver, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String greet = "Willkommen";

    /**
     * Lower priority
     * @param greet 
     */
    void receive(@Observes @Priority(APPLICATION + 200) String greet) {
        this.greet += greet + "2";
    }

    /**
     * Higher priority
     * @param greet 
     */
    void receive2(@Observes @Priority(APPLICATION) String greet) {
        this.greet = greet + "1";
    }

    @Override
    public String getGreet() {
        return greet;
    }
}
