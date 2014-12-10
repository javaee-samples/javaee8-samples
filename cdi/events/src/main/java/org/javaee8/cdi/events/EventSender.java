package org.javaee8.cdi.events;

/**
 * @author Radim Hanus
 * @author Arun Gupta
 */
public interface EventSender {

    void send(String message);
}
