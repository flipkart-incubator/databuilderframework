package com.flipkart.databuilderframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a DataBuilder to become usable in a {@link com.flipkart.databuilderframework.engine.DataBuilderMetadataManager}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBuilderInfo {
    public String name();
    public String[] consumes();
    public String[] optionals() default {}; //optionally consumer
    public String produces();
}
