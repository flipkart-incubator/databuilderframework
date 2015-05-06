package com.flipkart.databuilderframework;


import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.engine.DataBuilder;
import com.flipkart.databuilderframework.engine.DataBuilderContext;
import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.Data;
import com.google.common.base.Optional;

@DataBuilderInfo(name = "BuilderOptional", consumes = {"A"}, optionals= {"B"}, produces = "C")
public class TestBuilderOptional extends DataBuilder {

    @Override
    public Data process(DataBuilderContext context) {
        DataSetAccessor dataSetAccessor = context.getDataSet().accessor();
        TestDataA a = dataSetAccessor.get("A", TestDataA.class);
        Optional<TestDataB> b = dataSetAccessor.getOptional("B", TestDataB.class);
        if(a.getValue().contains(" ")){
        	return new TestDataC(a.getValue());
        }else{
        	if(b.isPresent()){
        		TestDataB bData = b.get();
        		return new TestDataC(a.getValue()+"_"+bData.getValue());
        	}
        }
        return null;
    }
}
