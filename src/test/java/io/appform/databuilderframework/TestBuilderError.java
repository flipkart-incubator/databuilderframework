package io.appform.databuilderframework;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

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
