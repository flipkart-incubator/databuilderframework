package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.ThreadUtils;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataA;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataA1;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderA1", consumes = {"A"}, produces = "A1")
public class BuilderA1 extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		DataA dataA = dataSetAccessor.get("A", DataA.class);
		ThreadUtils.INSTANCE.putToSleep(20, "A1");
		if(dataA.val <= 7){ //70% of case this builder will run and genrate data
			return new DataA1();
		}
		
		return null;
	}

}
