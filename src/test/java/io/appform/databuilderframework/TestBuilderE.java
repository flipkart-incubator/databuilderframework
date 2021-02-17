package io.appform.databuilderframework;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderE", consumes = {"B", "A"}, produces = "G")
public class TestBuilderE extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataA a = dataSetAccessor.get("A", TestDataA.class);
        TestDataB b = dataSetAccessor.get("B", TestDataB.class);
        return new TestDataG(a.getValue() + " " + b.getValue());
    }
}
