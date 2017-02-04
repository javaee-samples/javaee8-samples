package org.javaee8.jsonb.mapping;

import java.util.Objects;


public class Dog {
    private String name;
    private int age;
    private boolean bitable;

    public Dog() {
    }

    public Dog(String name, int age, boolean bitable) {
        this.name = name;
        this.age = age;
        this.bitable = bitable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return age == dog.age &&
                bitable == dog.bitable &&
                Objects.equals(name, dog.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, bitable);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isBitable() {
        return bitable;
    }

    public void setBitable(boolean bitable) {
        this.bitable = bitable;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", bitable=" + bitable +
                '}';
    }
}
