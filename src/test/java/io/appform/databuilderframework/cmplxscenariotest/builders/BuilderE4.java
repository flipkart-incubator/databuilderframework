package io.appform.databuilderframework.cmplxscenariotest.builders;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.cmplxscenariotest.ThreadUtils;
import io.appform.databuilderframework.cmplxscenariotest.data.DataD;
import io.appform.databuilderframework.cmplxscenariotest.data.DataE4;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.engine.DataValidationException;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderE4", accesses={"A","C"}, consumes = {"D"}, produces = "E4")
public class BuilderE4 extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		DataD dataD = dataSetAccessor.get("D", DataD.class);
		if(dataD.val <= 3){ // RUN FOR 2 VAL
			ThreadUtils.INSTANCE.putToSleep(20, "E4");
			return new DataE4();
		}
		return null;
	}

}
