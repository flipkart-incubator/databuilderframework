package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderB", consumes = {"C", "D"}, produces = "E")
public class TestBuilderB extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSet dataSet = context.getDataSet();
        if(dataSet == null) {
            return null;
        }
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        TestDataC c = dataSetAccessor.get("C", TestDataC.class).get();
        TestDataD d = dataSetAccessor.get("D", TestDataD.class).get();
        return new TestDataE(c.getValue() + " " + d.getValue());
    }
}
