package com.flipkart.cp.convert.europa.databuilderframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a DataBuilder to become usable in a {@link com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderMetadataManager}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBuilderInfo {
    public String name();
    public String[] consumes();
    public String produces();
}
