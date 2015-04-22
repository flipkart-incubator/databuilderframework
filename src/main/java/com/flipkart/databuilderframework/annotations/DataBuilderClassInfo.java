package com.flipkart.databuilderframework.annotations;

import com.flipkart.databuilderframework.model.Data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBuilderClassInfo {
    public String name() default "";

    public Class<? extends Data> produces();

    public Class<? extends Data>[] accesses() default {};

    public Class<? extends Data>[] consumes();
}
