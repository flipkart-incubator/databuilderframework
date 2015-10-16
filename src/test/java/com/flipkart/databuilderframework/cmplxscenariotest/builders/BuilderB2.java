package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataB2;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderB2", consumes = {"A", "A1"}, produces = "B2")
public class BuilderB2 extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		try {
			Thread.sleep(200); //simulate work being done
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new DataB2();
	}

}
