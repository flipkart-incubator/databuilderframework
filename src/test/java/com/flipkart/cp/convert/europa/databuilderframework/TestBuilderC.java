package com.flipkart.cp.convert.europa.databuilderframework;

import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderContext;
import com.flipkart.cp.convert.europa.databuilderframework.model.Data;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataSet;
import com.flipkart.cp.convert.europa.databuilderframework.util.DataSetAccessor;

public class TestBuilderC extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataC c = dataSetAccessor.get("C", TestDataC.class);
        TestDataE d = dataSetAccessor.get("E", TestDataE.class);
        return new TestDataF(c.getValue() + " " + d.getValue());
    }
}
