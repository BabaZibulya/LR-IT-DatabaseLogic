package com.univ.it.table;

import java.lang.reflect.Constructor;

public class Column {
    private String name;
    private Class attributeType;
    private Constructor attributeConstructor;

    public Column(String s) {
        try {
            name = s.substring(s.lastIndexOf(".") + 1);
            attributeType = Class.forName(s);
            attributeConstructor = findStringConstructor(attributeType);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Column(Class c) {
        String s = attributeType.getCanonicalName();
        name = s.substring(s.lastIndexOf(".") + 1);
        attributeType = c;
        try {
            attributeConstructor = attributeType.getConstructor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getName() {
        return name;
    }

    public Constructor getAttributeConstructor() {
        return attributeConstructor;
    }

    private Constructor findStringConstructor(Class c) throws NoSuchMethodException {
        return c.getConstructor(String.class);
    }

    public boolean equals(Column other) {
        return (attributeType.equals(other.attributeType));
    }

    @Override
    public String toString() {
        return attributeType.getCanonicalName();
    }
}
