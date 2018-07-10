package org.javaee8.cdi.dynamic.bean.decorated;

import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
@Priority(100)
public class MyDecorator implements MyBean {

    @Inject
    @Delegate
    MyBean mybean;

    @Override
    public String sayHi() {
        return mybean.sayHi() + " decorated";
    }

}
