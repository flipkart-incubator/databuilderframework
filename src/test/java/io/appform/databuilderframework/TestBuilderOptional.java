package io.appform.databuilderframework;


import io.appform.databuilderframework.annotations.DataBuilderInfo;
import io.appform.databuilderframework.engine.DataBuilder;
import io.appform.databuilderframework.engine.DataBuilderContext;
import io.appform.databuilderframework.engine.DataSetAccessor;
import io.appform.databuilderframework.model.Data;
import com.google.common.base.Optional;

@DataBuilderInfo(name = "BuilderOptional", consumes = {"A"}, optionals= {"B"}, produces = "C")
public class TestBuilderOptional extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSetAccessor dataSetAccessor = context.getDataSet().accessor();
        TestDataA a = dataSetAccessor.get("A", TestDataA.class);
        boolean bIsPresent = dataSetAccessor.checkForData("B");
        if(a.getValue().contains(" ")){
        	return new TestDataC(a.getValue());
        }else{
        	if(bIsPresent){
        		TestDataB bData = dataSetAccessor.get("B",TestDataB.class);
        		return new TestDataC(a.getValue()+"_"+bData.getValue());
        	}
        }
        return null;
    }
}
