package com.flipkart.databuilderframework.flowtest.builders;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.flowtest.data.OCD;
import com.flipkart.databuilderframework.model.Data;

public class CountingOCBuilder extends DataBuilder {
    private static int count = 0;
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        if(count > 1) {
            return new OCD();
        }
        count++;
        return null;
    }
}
