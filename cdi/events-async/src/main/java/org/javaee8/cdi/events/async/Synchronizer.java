package org.javaee8.cdi.events.async;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Synchronizer {
    
    private static final Logger logger = Logger.getLogger(Synchronizer.class.getName());

    private final CountDownLatch receiverStarted = new CountDownLatch(1);
    private final CountDownLatch receiverMayProcess = new CountDownLatch(1);
    
    public void waitTillReceiverStarted() {
        logger.info("Waiting for receiver to start");
        try {
            if (!receiverStarted.await(5,SECONDS)) {
                throw new IllegalStateException("Receiver of event not called within 5 seconds");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void receiverStarted() {
        logger.info("Receiver started");
        receiverStarted.countDown();
    }
    
    public void waitTillReceiverMayProcess() {
        logger.info("Waiting for receiver may process");
        try {
            if (!receiverMayProcess.await(5,SECONDS)) {
                throw new IllegalStateException("Receiver not given permission to process within 5 seconds");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void receiverMayProcess() {
        logger.info("Receiver may process");
        receiverMayProcess.countDown();
    }
    
}
