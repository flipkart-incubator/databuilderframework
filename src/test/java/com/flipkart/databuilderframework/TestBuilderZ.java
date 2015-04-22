package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.*;
import com.flipkart.databuilderframework.model.Data;

/**
 * Created with IntelliJ IDEA.
 * User: vinay.varma
 * Date: 4/22/15
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
@DataBuilderInfo(consumes = {"G"}, produces = "Z", name = "BuilderZ", accesses = {"OP1", "A"})
public class TestBuilderZ extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException, DataValidationException {

        DataSetAccessor accessor = new DataSetAccessor(context.getDataSet());

        TestDataZ testDataZ = new TestDataZ();
        testDataZ.isDataAPresent = accessor.get("A", TestDataA.class).isPresent();
        testDataZ.isDataOp1Present = accessor.get("OP1", TestDataOp1.class).isPresent();

        return testDataZ;
    }
}
