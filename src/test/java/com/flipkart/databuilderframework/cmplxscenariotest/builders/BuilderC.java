package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.ThreadUtils;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataC;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

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
