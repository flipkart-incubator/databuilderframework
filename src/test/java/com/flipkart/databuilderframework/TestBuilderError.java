package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderError", consumes = "X", produces = "Y")
public class TestBuilderError extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        DataSet dataSet = context.getDataSet();
        if (dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        if (!dataSetAccessor.get("A", TestDataA.class).isPresent() && !dataSetAccessor.get("B", TestDataB.class).isPresent()) {
            throw new DataBuilderException("TestError");
        }
        return null;
    }
}
