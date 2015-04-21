package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataBuilderMeta;

public class ProxyDataBuilder extends DataBuilder {
    private final DataBuilder impl;

    ProxyDataBuilder(DataBuilderMeta dataBuilderMeta, DataBuilder impl) {
        this.impl = impl;
        setDataBuilderMeta(dataBuilderMeta);
    }
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException, DataValidationException {
        return impl.process(context);
    }
}
