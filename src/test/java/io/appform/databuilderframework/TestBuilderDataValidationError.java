package io.appform.databuilderframework;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.engine.*;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

/**
 * Created by kaustav.das on 26/03/15.
 */
@DataBuilderInfo(name = "BuilderDataValidationError", consumes = "C", produces = "Y")
public class TestBuilderDataValidationError extends DataBuilder{
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException, DataValidationException {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataA a = dataSetAccessor.get("A", TestDataA.class);
        TestDataB b = dataSetAccessor.get("B", TestDataB.class);
        throw new DataValidationException("DataValidationError");
    }
}
