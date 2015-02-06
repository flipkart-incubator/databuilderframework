package com.flipkart.databuilderframework.complextest;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;

public class SB extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        return new OO();
    }
}
