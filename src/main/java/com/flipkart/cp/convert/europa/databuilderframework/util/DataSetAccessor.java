package com.flipkart.cp.convert.europa.databuilderframework.util;

import com.flipkart.cp.convert.europa.databuilderframework.model.Data;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataDelta;
import com.flipkart.cp.convert.europa.databuilderframework.model.DataSet;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Accessor for data access from DataSet.
 * Abstracts out access patterns from the {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataSet}
 */
public class DataSetAccessor {

    private DataSet dataSet;

    public DataSetAccessor(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * Get a data from {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataSet}
     * @param key Key for the data
     * @param tClass Class to cast the data to. Should inherit from {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data}
     * @param <T> Sub-Type for {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data}. Should be same as <i>tClass</i>
     * @return
     */
    public <T extends Data> T get(String key, Class<T> tClass) {
        Map<String, Data> availableData = dataSet.getAvailableData();
        if(availableData.containsKey(key)) {
            Data data = availableData.get(key);
            return tClass.cast(data);
        }
        return null;
    }

    /**
     * Merge data with current {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataSet}.
     * Data will be added to the current data-set and override data with same type as identified by
     * {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data#getData()}
     * @param data {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data} to be merged.
     */
    public void merge(Data data) {
        dataSet.getAvailableData().put(data.getData(), data);
    }

    /**
     * Merge all {@link com.flipkart.cp.convert.europa.databuilderframework.model.Data} elements from the {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataDelta}
     * Will overwrite all data present in the current {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataSet}.
     * Each elemnt of data is identified using {@link Data#getData()}
     * @param dataDelta {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataDelta} to be merged
     */
    public void merge(DataDelta dataDelta) {
        Map<String, Data> availableData = dataSet.getAvailableData();
        for(Data data : dataDelta.getDelta()) {
            availableData.put(data.getData(), data);
        }
    }

    /**
     * Check if all specified data is present in the {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataSet}
     * @param dataList List of all name of all data items to be checked.
     * @return <i>true</i> if all elements are present. <i>false</i> otherwise.
     */
    public boolean checkForData(List<String> dataList) {
        Map<String, Data> availableData = dataSet.getAvailableData();
        for(String data : dataList) {
            if(!availableData.containsKey(data)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a specified data is present in the {@link com.flipkart.cp.convert.europa.databuilderframework.model.DataSet}
     * @param data Name of the data items to be checked.
     * @return <i>true</i> if all elements are present. <i>false</i> otherwise.
     */
    public boolean checkForData(String data) {
        return dataSet.getAvailableData().containsKey(data);
    }

    /**
     * Get a copy of the underlying data set.
     */
    public DataSet copy() {
        return copy(null);
    }

    /**
     * Get a copy of the underlying data set. Don't copy transients.
     */
    public DataSet copy(Set<String> transients) {
        Map<String, Data> dataMap = Maps.newHashMap();
        for(Map.Entry<String, Data> data : dataSet.getAvailableData().entrySet()) {
            if(null == transients || !transients.contains(data.getKey())) {
                dataMap.put(data.getKey(), data.getValue());
            }
        }
        DataSet tmpDataSet = new DataSet();
        tmpDataSet.setAvailableData(dataMap);
        return tmpDataSet;
    }
}
