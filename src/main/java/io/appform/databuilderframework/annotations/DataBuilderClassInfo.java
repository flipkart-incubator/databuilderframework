package io.appform.databuilderframework.annotations;

import io.appform.databuilderframework.model.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBuilderClassInfo {
    String name() default "";
    Class<? extends Data> produces();
    Class<? extends Data>[] consumes();
    Class<? extends Data>[] accesses() default {};  //enable builder to access these data - plays no role in triggering builder flow
    Class<? extends Data>[] optionals() default {}; //enable builder to trigger on this data but unlike consumers these are not mandatory for builder to run
}
