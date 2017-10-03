package com.univ.it.table;

import java.lang.reflect.Constructor;

public class Column {
    private Class attributeType;
    private Constructor attributeConstructor;

    public Column(String s) {
        try {
            attributeType = Class.forName(s);
            attributeConstructor = findStringConstructor(attributeType);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Column(Class c) {
        attributeType = c;
        try {
            attributeConstructor = attributeType.getConstructor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Constructor getAttributeConstructor() {
        return attributeConstructor;
    }

    private Constructor findStringConstructor(Class c) throws NoSuchMethodException {
        return c.getConstructor(String.class);
    }

    @Override
    public String toString() {
        return attributeType.getCanonicalName();
    }
}
