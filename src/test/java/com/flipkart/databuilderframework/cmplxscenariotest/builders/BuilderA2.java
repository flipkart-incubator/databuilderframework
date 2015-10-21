package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.ThreadUtils;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataA;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataA2;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderA2", consumes = {"A"}, produces = "A2")
public class BuilderA2 extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		DataA dataA = dataSetAccessor.get("A", DataA.class);
		String name = Thread.currentThread().getName();
		if(dataA.val > 7){ // this builder will run when BuilderA1 is not running
			ThreadUtils.INSTANCE.putToSleep(20, "A2");
			return new DataA2();
		}
		return null ;
	}

}
