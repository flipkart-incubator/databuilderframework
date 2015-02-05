package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;
import com.flipkart.databuilderframework.engine.DataSetAccessor;

public class TestBuilderD extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataF f = dataSetAccessor.get("F", TestDataF.class);
        return new TestDataG(f.getValue());
    }
}
