package com.flipkart.databuilderframework.speed;

import com.flipkart.databuilderframework.TestDataD;
import com.flipkart.databuilderframework.TestDataE;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.model.Data;

public class ServiceCallerE extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new TestDataE("E");
    }
}
