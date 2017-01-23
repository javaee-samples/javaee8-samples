package org.javaee8.jcache.configuration;

import java.io.Serializable;
import java.util.Objects;


/**
 * @author Radim Hanus
 */
public class MyValue implements Serializable {
    private String value;

    public MyValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyValue that = (MyValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return getValue();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
