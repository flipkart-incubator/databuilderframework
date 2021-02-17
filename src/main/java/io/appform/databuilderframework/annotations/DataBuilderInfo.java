package io.appform.databuilderframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a DataBuilder to become usable in a {@link io.appform.databuilderframework.engine.DataBuilderMetadataManager}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBuilderInfo {
    public String name();
    public String[] consumes();
    public String[] optionals() default {}; //enable builder to trigger on this data but unlike consumers these are not mandatory for builder to run
    public String[] accesses() default {};//enable builder to access these data - plays no role in triggering builder flow
    public String produces();
}
