package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;

/**
 * Created with IntelliJ IDEA.
 * User: vinay.varma
 * Date: 4/22/15
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
@DataBuilderInfo(produces = "OP1", name = "BuilderOp1", consumes = {"G"})
public class TestBuilderOp1 extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException, DataValidationException {
        System.out.println("Running Data Builder Op1");
        throw new DataBuilderException("Should not have run.");
    }
}
