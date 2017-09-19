package org.javaee8.cdi.qualified.lookup;

import javax.inject.Named;

@Named("northern")
public class MyGreeting1 implements MyGreeting {

    @Override
    public String getGreet() {
        return "Ay-up!";
    }

}
