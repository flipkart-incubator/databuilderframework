package io.appform.databuilderframework;


import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.model.Data;

@DataBuilderInfo(name = "BuilderAccesses", consumes = {"A"}, produces = "X")
public class TestBuilderWithoutAccesses extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSetAccessor dataSetAccessor = context.getDataSet().accessor();
        TestDataA a = dataSetAccessor.get("A", TestDataA.class);
        boolean bIsPresent = dataSetAccessor.checkForData("D");
        return new TestDataX(bIsPresent ? "TRUE" : "FALSE");

    }
}
