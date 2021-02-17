package io.appform.databuilderframework.cmplxscenariotest.builders;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.cmplxscenariotest.ThreadUtils;
import io.appform.databuilderframework.cmplxscenariotest.data.DataC;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.engine.DataValidationException;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderC", consumes = {"A"},
optionals={"A1","A2","B1","B2", "B3", "B4", "B5"}, produces = "C")
public class BuilderC extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		DataSetAccessor accessor = DataSet.accessor(context.getDataSet());
		if(accessor.checkForData("A1")){
			if(accessor.checkForData("B1") &&
					accessor.checkForData("B2") &&
					accessor.checkForData("B3") &&
					accessor.checkForData("B4") &&
					accessor.checkForData("B5")){
				ThreadUtils.INSTANCE.putToSleep(20, "C");

				return new DataC();
			}
		}else if(accessor.checkForData("A2")){
			ThreadUtils.INSTANCE.putToSleep(20, "C");
			return new DataC();
		}
		return null;
	}

}
