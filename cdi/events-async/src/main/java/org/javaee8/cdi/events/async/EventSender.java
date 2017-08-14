package org.javaee8.cdi.events.async;

import java.util.concurrent.CompletionStage;

/**
 * @author Radim Hanus
 * @author Arun Gupta
 * @author Arjan Tijms
 */
public interface EventSender {

    void sendSync(String message);
    CompletionStage<String> sendAsync(String message);
}
