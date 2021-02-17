package io.appform.databuilderframework.model;

import io.appform.databuilderframework.engine.Utils;

public abstract class DataAdapter<T extends Data> extends Data {
    protected DataAdapter(Class<T> dataClass) {
        super(Utils.name(dataClass));
    }
}
