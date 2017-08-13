package org.javaee8.cdi.interception.factory;

public class MyGreetingImpl implements MyGreeting {
    
    private String greet;

    public String getGreet() {
        return greet;
    }

    public void setGreet(String greet) {
        this.greet = greet;
    }
}
