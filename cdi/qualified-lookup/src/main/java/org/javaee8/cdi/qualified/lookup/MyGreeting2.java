package org.javaee8.cdi.qualified.lookup;

import javax.inject.Named;

@Named("informal")
public class MyGreeting2 implements MyGreeting {

    @Override
    public String getGreet() {
        return "Hiya!";
    }

}
