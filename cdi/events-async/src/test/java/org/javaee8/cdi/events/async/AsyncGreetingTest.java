package org.javaee8.cdi.events.async;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Radim Hanus
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class AsyncGreetingTest {

    @Deployment
    public static Archive<?> deploy() {
        return create(JavaArchive.class)
                .addClasses(
                    EventReceiver.class, EventSender.class, 
                    GreetingReceiver.class, GreetingSender.class,
                    Synchronizer.class)
                .addAsManifestResource("beans.xml");
    }

    @Inject
    private EventSender sender;

    @Inject
    private EventReceiver receiver;
    
    @Inject
    private Synchronizer synchronizer;

    @Test
    public void test() throws Exception {
        assertThat(sender, is(notNullValue()));
        assertThat(sender, instanceOf(GreetingSender.class));

        assertThat(receiver, is(notNullValue()));
        assertThat(receiver, instanceOf(GreetingReceiver.class));

        // Default greet
        assertEquals("Willkommen", receiver.getGreet());
        
        // Send a new greet synchronously
        sender.sendSync("Welcome");
        
        // Receiver must not belong to the dependent pseudo-scope since we are checking the result
        assertEquals("Welcome-sync", receiver.getGreet());
        
        // Send a new greet asynchronously
        CompletionStage<String> completionStage = sender.sendAsync("Hi");
        
        synchronizer.waitTillReceiverStarted();
        
        // The receiver has started, signal that it may now start processing
        // This is done to test that the receiver is really on a different thread
        synchronizer.receiverMayProcess();
        
        completionStage.toCompletableFuture().get(15, SECONDS);
        
        assertEquals("Welcome-syncHi-async", receiver.getGreet());
        
    }
}
