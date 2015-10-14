package com.flipkart.databuilderframework.cmplxscenariotest.data;

import com.flipkart.databuilderframework.model.Data;

public class DataA extends Data{

	public final int val;
	public DataA() {
		super("A");
		this.val = (int) (Math.random()*10);
		
	}

}
