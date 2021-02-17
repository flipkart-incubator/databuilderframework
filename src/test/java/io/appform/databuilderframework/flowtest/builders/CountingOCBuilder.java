package io.appform.databuilderframework.flowtest.builders;

import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.flowtest.data.OCD;
import io.appform.databuilderframework.model.Data;

public class CountingOCBuilder extends DataBuilder {
    private static int count = 0;
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        if(count > 1) {
            return new OCD();
        }
        count++;
        return null;
    }
}
