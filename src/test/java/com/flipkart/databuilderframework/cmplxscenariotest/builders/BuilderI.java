package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataI;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderI", accesses={"A"},
	optionals={"E2","E3","E4","E5","E6","F","G","IE6"}, 
	consumes = {"C","A3"}, produces = "I")
public class BuilderI extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		try {
			Thread.sleep(200); //simulate work being done
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		return new DataI();
	}

}
