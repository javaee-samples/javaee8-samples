package org.javaee8.cdi.events.priority;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.interceptor.Interceptor;
import java.io.Serializable;

/**
 * Using {@link javax.annotation.Priority} annotation to define an order in which observer methods are called.<br/>
 * Observers with smaller priority values are called first.
 *
 * @author Radim Hanus
 * @author Arun Gupta
 *
 * @see <a href="http://docs.jboss.org/cdi/spec/2.0.EDR2/cdi-spec.html#observer_ordering">relevant chapter in cdi-2.0 spec</a>
 */
@RequestScoped
public class GreetingReceiver implements EventReceiver, Serializable {

    private String greet = "Willkommen";

    /**
     * Higher priority
     */
    void receiveFirst(@Observes @Priority(Interceptor.Priority.APPLICATION) String greet) {
        this.greet = greet + "First ";
    }

    /**
     * Middle priority, uses default priority Interceptor.Priority.APPLICATION + 500
     */
    void receiveSecond(@Observes String greet) {
        this.greet += greet + "Second ";
    }

    /**
     * Lower priority
     */
    void receiveThird(@Observes @Priority(Interceptor.Priority.APPLICATION + 1000) String greet) {
        this.greet += greet + "Third";
    }

    @Override
    public String getGreet() {
        return greet;
    }
}
