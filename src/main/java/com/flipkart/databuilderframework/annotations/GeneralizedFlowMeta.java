package com.flipkart.databuilderframework.annotations;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.model.DataAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GeneralizedFlowMeta {
    Class<? extends DataBuilder>[] builders();
    Class<? extends DataAdapter<?>> target() ;
    Class<? extends DataAdapter<?>>[] transients() default {};
}