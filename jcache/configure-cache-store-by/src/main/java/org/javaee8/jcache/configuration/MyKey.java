package org.javaee8.jcache.configuration;

import java.io.Serializable;
import java.util.Objects;


/**
 * @author Radim Hanus
 */
public class MyKey implements Serializable {
    private String key;

    public MyKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyKey that = (MyKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return getKey();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
