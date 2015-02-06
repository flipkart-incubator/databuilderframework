package com.flipkart.databuilderframework.model;

public abstract class DataAdapter<T extends Data> extends Data {
    protected DataAdapter(Class<T> dataClass) {
        super(dataClass.getCanonicalName());
    }
}
