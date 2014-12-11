package org.javaee8.cdi.events.priority;

/**
 * @author Radim Hanus
 * @author Arun Gupta
 */
public interface EventSender {

    void send(String message);
}
