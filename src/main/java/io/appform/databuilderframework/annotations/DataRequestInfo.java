package io.appform.databuilderframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a sub-class of {@link io.appform.databuilderframework.model.Data} class to become
 * usable in a {@link io.appform.databuilderframework.model.DataFlow}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataRequestInfo {
    public String value();
}
