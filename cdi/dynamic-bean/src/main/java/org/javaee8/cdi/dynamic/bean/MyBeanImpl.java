package org.javaee8.cdi.dynamic.bean;

import javax.enterprise.inject.Typed;

/**
 * 
 * @author Arjan Tijms
 *
 */
// Typed: Extra guard so that MyBeanImpl has no types of itself, but extension archive is not scanned
//        so not strictly needed.
@Typed
public class MyBeanImpl implements MyBean {
    
    private final String greet;
    
    // Note: There's no default ctor, so CDI cannot directly inject an instance of this
    //       bean.
    
    public MyBeanImpl(String greet) {
        this.greet = greet;
    }

    @Override
    public String sayHi() {
        return greet;
    }

}
