package io.appform.databuilderframework;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderC", consumes = {"E", "A"}, produces = "F")
public class TestBuilderC extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataA c = dataSetAccessor.get("A", TestDataA.class);
        TestDataE d = dataSetAccessor.get("E", TestDataE.class);
        return new TestDataF(c.getValue() + " " + d.getValue());
    }
}
