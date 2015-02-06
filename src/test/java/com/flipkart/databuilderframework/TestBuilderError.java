package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;
import com.flipkart.databuilderframework.engine.DataSetAccessor;

@DataBuilderInfo(name = "BuilderError", consumes = "X", produces = "Y")
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
