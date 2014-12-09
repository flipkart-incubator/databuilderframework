package com.flipkart.databuilderframework.flowtest.builders;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.flowtest.data.OrderCompleteData;
import com.flipkart.databuilderframework.model.Data;

public class CountingOrderCompletedBuilder extends DataBuilder {
    private static int count = 0;
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        if(count > 1) {
            return new OrderCompleteData();
        }
        count++;
        return null;
    }
}
