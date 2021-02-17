package io.appform.databuilderframework.flowtest.builders;

import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.flowtest.data.DPD;
import io.appform.databuilderframework.model.Data;

public class DPBuilder extends DataBuilder {
    @Override
    public Data process(DataBuilderContext context) throws DataBuilderException {
        return new DPD();
    }
}
