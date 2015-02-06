package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.DataSetAccessor;
import com.flipkart.databuilderframework.model.DataDelta;
import com.flipkart.databuilderframework.model.DataSet;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DataSetAccessorTest {

    @Test
    public void testGet() throws Exception {
        DataSet dataSet = new DataSet();
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        dataSetAccessor.merge(new TestDataA("RandomValue"));
        TestDataA testDataA = dataSetAccessor.get("A", TestDataA.class);
        Assert.assertEquals("RandomValue", testDataA.getValue());
        Assert.assertNull(dataSetAccessor.get("X", TestDataA.class));

        try {
            TestDataB testDataB = dataSetAccessor.get("A", TestDataB.class);
        } catch (ClassCastException e) {
            return;
        }
        fail();
    }

    @Test
    public void testMerge() throws Exception {
        DataSet dataSet = new DataSet();
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        DataDelta dataDelta = new DataDelta(Lists.newArrayList(new TestDataA("Hello"),
                                                                new TestDataB("World")));
        dataSetAccessor.merge(dataDelta);
        TestDataA testDataA = dataSetAccessor.get("A", TestDataA.class);
        Assert.assertEquals("Hello", testDataA.getValue());
        TestDataB testDataB = dataSetAccessor.get("B", TestDataB.class);
        Assert.assertEquals("World", testDataB.getValue());
    }

    @Test
    public void testCheckForData() throws Exception {
        DataSet dataSet = new DataSet();
        DataSetAccessor dataSetAccessor = DataSet.accessor(dataSet);
        DataDelta dataDelta = new DataDelta(Lists.newArrayList(new TestDataA("Hello"),
                new TestDataB("World")));
        dataSetAccessor.merge(dataDelta);
        Assert.assertTrue(dataSetAccessor.checkForData("A"));
        Assert.assertFalse(dataSetAccessor.checkForData("X"));
        Assert.assertTrue(dataSetAccessor.checkForData(ImmutableSet.of("A", "B")));
        Assert.assertFalse(dataSetAccessor.checkForData(ImmutableSet.of("A", "X")));
        Assert.assertFalse(dataSetAccessor.checkForData(ImmutableSet.of("X", "A")));
        Assert.assertFalse(dataSetAccessor.checkForData(ImmutableSet.of("X", "Ys")));
    }
}
