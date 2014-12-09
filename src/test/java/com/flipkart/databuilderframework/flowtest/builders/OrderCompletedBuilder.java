package com.flipkart.databuilderframework.flowtest.builders;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.flowtest.data.OrderCompleteData;
import com.flipkart.databuilderframework.model.Data;

public class OrderCompletedBuilder extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        return new OrderCompleteData();
    }
}
