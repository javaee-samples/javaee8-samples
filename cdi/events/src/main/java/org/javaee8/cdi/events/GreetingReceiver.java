package org.javaee8.cdi.events;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import java.io.Serializable;
import javax.annotation.Priority;
import javax.interceptor.Interceptor;

/**
 * @author Radim Hanus
 * @author Arun Gupta
 */
@SessionScoped
public class GreetingReceiver implements EventReceiver, Serializable {

    private String greet = "Willkommen";

    void receive(@Observes @Priority(Interceptor.Priority.APPLICATION + 200) String greet) {
        this.greet = greet;
    }

    @Override
    public String getGreet() {
        return greet;
    }
}
