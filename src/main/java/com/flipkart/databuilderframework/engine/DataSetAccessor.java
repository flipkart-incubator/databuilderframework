package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataDelta;
import com.flipkart.databuilderframework.model.DataSet;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

/**
 * Accessor for data access from DataSet.
 * Abstracts out access patterns from the {@link com.flipkart.databuilderframework.model.DataSet}
 */
public class DataSetAccessor {

    private DataSet dataSet;

    public DataSetAccessor(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public <T extends Data> T get(Class<T> tClass) {
        return get(Utils.name(tClass), tClass);
    }

    /**
     * Get a data from {@link com.flipkart.databuilderframework.model.DataSet}.
     * @param key    Key for the data
     * @param tClass Class to cast the data to. Should inherit from {@link com.flipkart.databuilderframework.model.Data}
     * @param <T>    Sub-Type for {@link com.flipkart.databuilderframework.model.Data}. Should be same as <i>tClass</i>
     * @return data
     */
    public <T extends Data> T get(String key, Class<T> tClass) {
        Map<String, Data> availableData = dataSet.getAvailableData();
        if (availableData.containsKey(key)) {
            Data data = availableData.get(key);
            return tClass.cast(data);
        }
        return null;
    }

    /**
     * Get data from {@link com.flipkart.databuilderframework.model.DataSet}, with accessibility checks.
     * This will throw an exception if the calling data builder is not supposed to use the requested data.
     * @param key     Key for the data
     * @param builder {@link com.flipkart.databuilderframework.engine.DataBuilder} that is accessing the data
     * @param tClass  Class to cast the data to. Should inherit from {@link com.flipkart.databuilderframework.model.Data}
     * @param <T>     Sub-Type for {@link com.flipkart.databuilderframework.model.Data}. Should be same as <i>tClass</i>
     * @return data
     */
    public <B extends DataBuilder, T extends Data> T getAccessibleData(String key, B builder, Class<T> tClass) {
        Preconditions.checkArgument(!builder.getDataBuilderMeta().getAccessibleDataSet().contains(key),
                String.format("Builder %s can access only %s",
                        builder.getDataBuilderMeta().getName(),
                        builder.getDataBuilderMeta().getConsumes()));
        return get(key, tClass);
    }

    /**
     * Get only the accesible data for this builder.
     * @param builder
     * @return
     */
    public DataSet getAccesibleDataSetFor(DataBuilder builder) {
        return new DataSet(Maps.filterKeys(dataSet.getAvailableData(),
                Predicates.in(builder.getDataBuilderMeta().getAccessibleDataSet())));
    }

    /**
     * Merge data with current {@link com.flipkart.databuilderframework.model.DataSet}.
     * Data will be added to the current data-set and override data with same type as identified by
     * {@link com.flipkart.databuilderframework.model.Data#getData()}
     * @param data {@link com.flipkart.databuilderframework.model.Data} to be merged.
     */
    public void merge(Data data) {
        dataSet.getAvailableData().put(data.getData(), data);
    }

    public void unset(String data){
        dataSet.getAvailableData().remove(data);
    }

    /**
     * Merge all {@link com.flipkart.databuilderframework.model.Data} elements from the {@link com.flipkart.databuilderframework.model.DataDelta}
     * Will overwrite all data present in the current {@link com.flipkart.databuilderframework.model.DataSet}.
     * Each elemnt of data is identified using {@link Data#getData()}
     * @param dataDelta {@link com.flipkart.databuilderframework.model.DataDelta} to be merged
     */
    public void merge(DataDelta dataDelta) {
        Map<String, Data> availableData = dataSet.getAvailableData();
        for (Data data : dataDelta.getDelta()) {
            availableData.put(data.getData(), data);
        }
    }

    /**
     * Check if all specified data is present in the {@link com.flipkart.databuilderframework.model.DataSet}
     * @param dataList List of all name of all data items to be checked.
     * @return <i>true</i> if all elements are present. <i>false</i> otherwise.
     */
    public boolean checkForData(Set<String> dataList) {
        Map<String, Data> availableData = dataSet.getAvailableData();
        return availableData.keySet().containsAll(dataList);
    }

    /**
     * Check if a specified data is present in the {@link com.flipkart.databuilderframework.model.DataSet}
     * @param data Name of the data items to be checked.
     * @return <i>true</i> if all elements are present. <i>false</i> otherwise.
     */
    public boolean checkForData(String data) {
        return dataSet.getAvailableData().containsKey(data);
    }

    /**
     * Check if a specified data is present in the {@link com.flipkart.databuilderframework.model.DataSet}
     * @param clazz Class to be checked.
     * @return <i>true</i> if all elements are present. <i>false</i> otherwise.
     */
    public boolean checkForData(Class<?> clazz) {
        return dataSet.getAvailableData().containsKey(Utils.name(clazz));
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
        if (null != transients && !transients.isEmpty()) {
            dataMap.putAll(Maps.filterKeys(dataSet.getAvailableData(), Predicates.not(Predicates.in(transients))));
        } else {
            dataMap.putAll(dataSet.getAvailableData());
        }
        return new DataSet(dataMap);
    }
}
