package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderA", consumes = {"B", "A"}, produces = "C")
public class TestBuilderA extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSetAccessor dataSetAccessor = context.getDataSet().accessor();
        TestDataA a = dataSetAccessor.get("A", TestDataA.class).get();
        TestDataB b = dataSetAccessor.get("B", TestDataB.class).get();
        return new TestDataC(a.getValue() + " " + b.getValue());
    }
}
