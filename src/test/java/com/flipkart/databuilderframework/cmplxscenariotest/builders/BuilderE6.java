package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataE6;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderE6", accesses={"A","C"}, consumes = {"D","IE6"}, produces = "E6")
public class BuilderE6 extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		try {
			Thread.sleep(20); //simulate work being done
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		return new DataE6();
	}

}
