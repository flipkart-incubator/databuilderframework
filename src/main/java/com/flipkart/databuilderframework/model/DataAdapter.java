package com.flipkart.databuilderframework.model;

import com.flipkart.databuilderframework.engine.Utils;

public abstract class DataAdapter<T extends Data> extends Data {
    protected DataAdapter(Class<T> dataClass) {
        super(Utils.name(dataClass));
    }
}
