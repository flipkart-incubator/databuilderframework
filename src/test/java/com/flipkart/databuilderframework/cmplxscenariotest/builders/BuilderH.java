package com.flipkart.databuilderframework.cmplxscenariotest.builders;

import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.cmplxscenariotest.data.DataH;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataBuilderException;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.engine.DataValidationException;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataSet;

@DataBuilderInfo(name = "BuilderH", accesses={"A","C","D"}, consumes = {"G"},
	optionals ={"E2","E3","E4","F","G"}, produces = "H")
public class BuilderH extends DataBuilder{

	@Override
	public Data process(DataBuilderContext context)
			throws DataBuilderException, DataValidationException {
		try {
			Thread.sleep(200); //simulate work being done
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DataSetAccessor dataSetAccessor = DataSet.accessor(context.getDataSet());
		return new DataH();
	}

}
