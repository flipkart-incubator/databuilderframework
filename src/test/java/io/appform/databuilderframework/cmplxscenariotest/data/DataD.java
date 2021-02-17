package io.appform.databuilderframework.cmplxscenariotest.data;

import io.appform.databuilderframework.model.Data;

public class DataD extends Data{

	public final int val;
	public DataD() {
		super("D");
		this.val = (int) (Math.random()* 5);
	}

}
