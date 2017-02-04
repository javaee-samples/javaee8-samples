package org.javaee8.jcache.infinispan.externalize;

import org.infinispan.commons.marshall.AdvancedExternalizer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Set;


/**
 * @author Radim Hanus
 */
public class MyValueExternalizer implements AdvancedExternalizer<MyValue> {
    @Override
    public Set<Class<? extends MyValue>> getTypeClasses() {
        return Collections.singleton(MyValue.class);
    }

    @Override
    public Integer getId() {
        return 123;
    }

    @Override
    public void writeObject(ObjectOutput objectOutput, MyValue myValue) throws IOException {
        objectOutput.writeObject(myValue.getValue());
    }

    @Override
    public MyValue readObject(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return new MyValue((String) objectInput.readObject());
    }
}
