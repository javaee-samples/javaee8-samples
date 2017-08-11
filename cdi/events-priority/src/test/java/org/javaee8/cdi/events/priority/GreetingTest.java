package org.javaee8.cdi.events.priority;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {

    @Deployment
    public static Archive<?> deploy() {
        return create(JavaArchive.class)
                .addClasses(EventReceiver.class, EventSender.class, GreetingReceiver.class, GreetingSender.class)
                .addAsManifestResource("beans.xml");
    }

    @Inject
    private EventSender sender;

    @Inject
    private EventReceiver receiver;

    @Test
    public void test() throws Exception {
        assertThat(sender, is(notNullValue()));
        assertThat(sender, instanceOf(GreetingSender.class));

        assertThat(receiver, is(notNullValue()));
        assertThat(receiver, instanceOf(GreetingReceiver.class));

        // Default greet
        assertEquals("Willkommen", receiver.getGreet());
        
        // Send a new greet
        sender.send("Welcome");
        
        // Receiver must not belongs to the dependent pseudo-scope since we are checking the result
        assertEquals("Welcome1Welcome2", receiver.getGreet());
    }
}
