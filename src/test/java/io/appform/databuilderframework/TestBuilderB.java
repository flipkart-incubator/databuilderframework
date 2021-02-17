package io.appform.databuilderframework;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderB", consumes = {"C", "D"}, produces = "E")
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
