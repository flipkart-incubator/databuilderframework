package com.flipkart.cp.convert.europa.databuilderframework.flowtest.builders;

import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderContext;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderException;
import com.flipkart.cp.convert.europa.databuilderframework.flowtest.data.OrderCompleteData;
import com.flipkart.cp.convert.europa.databuilderframework.model.Data;

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
