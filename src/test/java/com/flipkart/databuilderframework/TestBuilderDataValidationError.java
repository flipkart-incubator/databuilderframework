package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

/**
 * Created by kaustav.das on 26/03/15.
 */
@DataBuilderInfo(name = "BuilderDataValidationError", consumes = "C", produces = "Y")
public class TestBuilderDataValidationError extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException, DataValidationException {
        DataSet dataSet = context.getDataSet();
        if (dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        if (!dataSetAccessor.get("A", TestDataA.class).isPresent() && !dataSetAccessor.get("B", TestDataB.class).isPresent()) {
            throw new DataValidationException("DataValidationError");
        }
        return null;
    }
}
