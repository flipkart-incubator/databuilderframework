package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.ThreadUtils;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataD;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataE2;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderE2", accesses={"A","C"}, consumes = {"D"}, produces = "E2")
public class BuilderE2 extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		DataD dataD = dataSetAccessor.get("D", DataD.class);
		if(dataD.val <= 2){ // RUN FOR 2 VAL
			ThreadUtils.INSTANCE.putToSleep(20, "E2");
			return new DataE2();
		}
		return null;
	}

}
