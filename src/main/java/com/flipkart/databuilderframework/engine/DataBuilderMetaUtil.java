package com.flipkart.databuilderframework.engine;

import java.util.Set;

import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.google.common.collect.Sets;

/**
 * Utility class to DataBuilderMetaD
 * @author gokulvanan.v
 *
 */
public class DataBuilderMetaUtil {


	public  static Set<String> getEffectiveConsumes(DataBuilderMeta builderMeta){
		if(null == builderMeta.getOptionals() && builderMeta.getOptionals().isEmpty()){
			return builderMeta.getConsumes();
		}else{
			return Sets.union(builderMeta.getConsumes(), builderMeta.getOptionals());
		}
	}
}
