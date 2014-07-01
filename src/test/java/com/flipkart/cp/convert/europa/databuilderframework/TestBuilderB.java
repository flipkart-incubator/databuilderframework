package com.flipkart.cp.convert.europa.databuilderframework;

import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderContext;
import com.flipkart.cp.convert.europa.databuilderframework.model.Data;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataSet;
import com.flipkart.cp.convert.europa.databuilderframework.util.DataSetAccessor;

public class TestBuilderB extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataC c = dataSetAccessor.get("C", TestDataC.class);
        TestDataD d = dataSetAccessor.get("D", TestDataD.class);
        return new TestDataE(c.getValue() + " " + d.getValue());
    }
}
