package com.flipkart.cp.convert.europa.databuilderframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a sub-class od {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data} class to become
 * usable in a {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataFlow}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataRequestInfo {
    public String value();
}
