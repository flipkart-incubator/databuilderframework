package io.appform.databuilderframework.cmplxscenariotest.builders;

import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.cmplxscenariotest.ThreadUtils;
import io.appform.databuilderframework.cmplxscenariotest.data.DataA;
import io.appform.databuilderframework.cmplxscenariotest.data.DataA2;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataBuilderException;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.engine.DataValidationException;
import io.appform.databuilderframework.model.Data;
import io.appform.databuilderframework.model.DataSet;

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
