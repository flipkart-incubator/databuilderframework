package com.flipkart.cp.convert.europa.databuilderframework.engine;

import com.flipkart.cp.convert.europa.databuilderframework.model.DataSet;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Context object passed to the builder
 */
public class DataBuilderContext {

    /**
     * The working {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataSet} for this system.
     */
    private DataSet dataSet;

    private Map<String, Object> contextData = Maps.newHashMap();

    public DataBuilderContext() {
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public <T> void saveContextData(String key, T value) {
        if(null == key || key.isEmpty())  {
            throw new RuntimeException("Invalid key for context data. Key cannot be null/empty");
        }
        contextData.put(key, value);
    }

    public <T> T getContextData(String key, Class<T> tClass) {
        Object value = contextData.get(key);
        if(null == value) {
            return null;
        }
        return tClass.cast(value);
    }
}
