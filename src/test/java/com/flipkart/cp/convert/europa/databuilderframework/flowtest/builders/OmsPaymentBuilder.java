package com.flipkart.cp.convert.europa.databuilderframework.flowtest.builders;

import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderContext;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderException;
import com.flipkart.cp.convert.europa.databuilderframework.flowtest.data.OmsPayments;
import com.flipkart.cp.convert.europa.databuilderframework.model.Data;

public class OmsPaymentBuilder extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        return new OmsPayments();
    }
}
