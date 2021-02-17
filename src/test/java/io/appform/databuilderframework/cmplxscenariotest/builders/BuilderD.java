package io.appform.databuilderframework.cmplxscenariotest.builders;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.cmplxscenariotest.ThreadUtils;
import io.appform.databuilderframework.cmplxscenariotest.data.DataD;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.engine.DataValidationException;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderD", consumes = {"A", "C"}, produces = "D")
public class BuilderD extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		ThreadUtils.INSTANCE.putToSleep(20, "D");
		return new DataD();
	}

}
