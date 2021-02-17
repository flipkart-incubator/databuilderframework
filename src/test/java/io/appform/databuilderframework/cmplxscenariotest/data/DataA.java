package io.appform.databuilderframework.cmplxscenariotest.data;

import io.appform.databuilderframework.model.Data;

public class DataA extends Data{

	public final int val;
	public DataA() {
		super("A");
		this.val = (int) (Math.random()*10);
		
	}
	
	public DataA(int val) {
		super("A");
		this.val = val;
		
	}

}
