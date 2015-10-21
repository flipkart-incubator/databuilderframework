package com.flipkart.databuilderframework.cmplxscenariotest;

/**
 * Util to name threads for easier debugging
 * @author gokulvanan.v
 *
 */
public enum ThreadUtils {
	INSTANCE;


	public void putToSleep(int duration,String builderName){
		String name = Thread.currentThread().getName().split(":")[0];
		try {
			Thread.currentThread().setName(name+":"+builderName+"-before-sleep");
			Thread.sleep(20); //simulate work being done
			Thread.currentThread().setName(name+":"+builderName+"-after-sleep");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
