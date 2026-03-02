package com.example.ejb;

import jakarta.persistence.EntityManager;

import java.lang.reflect.Field;

public class TestUtils {
    static void injectEntityManager(Object target, EntityManager em) {
        try {
            Field field = target.getClass().getDeclaredField("em");
            field.setAccessible(true);
            field.set(target, em);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
