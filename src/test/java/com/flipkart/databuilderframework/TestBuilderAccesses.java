package com.flipkart.databuilderframework;


import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.Data;

@DataBuilderInfo(name = "BuilderAccesses", consumes = {"A"}, accesses = {"D"}, produces = "X")
public class TestBuilderAccesses extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSetAccessor dataSetAccessor = context.getDataSet().accessor();
        TestDataA a = dataSetAccessor.get("A", TestDataA.class);
        boolean bIsPresent = dataSetAccessor.checkForData("D");
        return new TestDataX(bIsPresent ? "TRUE" : "FALSE");

    }
}