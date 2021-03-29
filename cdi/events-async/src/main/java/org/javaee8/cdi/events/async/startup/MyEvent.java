/*
 *    Copyright (c) [2019] Payara Foundation and/or its affiliates.
 */

package org.javaee8.cdi.events.async.startup;

import java.io.Serializable;

public class MyEvent implements Serializable {

    private String source;

    public MyEvent(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
