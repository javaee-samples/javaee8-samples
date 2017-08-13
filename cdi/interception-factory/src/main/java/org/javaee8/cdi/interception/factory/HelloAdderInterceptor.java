package org.javaee8.cdi.interception.factory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@HelloAdder
@Interceptor
@Priority(500)
public class HelloAdderInterceptor {
    
    @AroundInvoke
    public Object modifyGreet(InvocationContext context) throws Exception {

        if (context.getMethod().getName().equals("setGreet")) {
            context.setParameters(new Object[] { "Hello " + context.getParameters()[0] });
        }

        return context.proceed();
    }
}
