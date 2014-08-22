package com.flipkart.cp.convert.europa.databuilderframework.speed;

import com.flipkart.cp.convert.europa.databuilderframework.TestDataD;
import com.flipkart.cp.convert.europa.databuilderframework.TestDataF;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilder;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderContext;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderException;
import com.flipkart.cp.convert.europa.databuilderframework.model.Data;

public class ServiceCallerF extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new TestDataF("F");
    }
}
