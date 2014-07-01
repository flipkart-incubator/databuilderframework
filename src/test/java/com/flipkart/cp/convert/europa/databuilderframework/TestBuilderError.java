package com.flipkart.cp.convert.europa.databuilderframework;

import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderContext;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderException;
import com.flipkart.cp.convert.europa.databuilderframework.model.Data;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataSet;
import com.flipkart.cp.convert.europa.databuilderframework.util.DataSetAccessor;

public class TestBuilderError extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataA a = dataSetAccessor.get("A", TestDataA.class);
        TestDataB b = dataSetAccessor.get("B", TestDataB.class);
        throw new DataBuilderException("TestError");
    }
}
