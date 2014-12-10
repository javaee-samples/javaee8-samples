package org.javaee8.cdi.events;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import java.io.Serializable;
import javax.interceptor.Interceptor;
import org.jboss.weld.experimental.Priority;

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
