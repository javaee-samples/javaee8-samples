package org.javaee8.cdi.events.async;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;

/**
 * @author Radim Hanus
 * @author Arun Gupta
 * @author Arjan Tijms
 */
@ApplicationScoped
public class GreetingReceiver implements EventReceiver, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String greet = "Willkommen";
    
    @Inject
    private Synchronizer synchronizer;
    
    /**
     * Synchronous observer
     * 
     * @param greet 
     */
    void receiveSync(@Observes String greet) {
        System.out.println("receivesync");
        this.greet = greet + "-sync";
    }

    /**
     * Asynchronous observer
     * 
     * @param greet 
     */
    void receiveAsync(@ObservesAsync String greet) {
        // Signal that we've started
        synchronizer.receiverStarted();
        
        // Wait till we're allowed to process this event
        synchronizer.waitTillReceiverMayProcess();
        
        try {
            // Simulate some amount of work
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        this.greet += greet + "-async";
    }

    @Override
    public String getGreet() {
        return greet;
    }
}
