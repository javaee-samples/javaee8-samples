/** Copyright Payara Services Limited **/
package org.javaee8.jsf.hello.world;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class HelloBacking {

    public String getHello() {
        return "Hello world, from JSF!";
    }

}
