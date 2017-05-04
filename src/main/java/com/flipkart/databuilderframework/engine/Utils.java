package com.flipkart.databuilderframework.engine;

import com.google.common.base.CaseFormat;

/**
 * Utilities for various stuff
 */
public interface Utils {

    static String name(Object object) {
        return name(object.getClass());
    }

    static String name(Class<?> clazz) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, clazz.getSimpleName());
    }
}
