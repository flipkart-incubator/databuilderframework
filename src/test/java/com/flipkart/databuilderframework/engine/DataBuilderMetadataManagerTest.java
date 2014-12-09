package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.complextest.SummaryBuilderTest;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Test;

public class DataBuilderMetadataManagerTest {

    @Test
    public void testGetConsumesSetFor() throws Exception {
        DataBuilderMetadataManager manager = new DataBuilderMetadataManager();
        Assert.assertEquals(null, manager.getConsumesSetFor("A"));
        manager.register(Lists.newArrayList("CR", "CAID", "VAS"), "OO", "SB", SummaryBuilderTest.class);
        Assert.assertEquals(1, manager.getConsumesSetFor("CR").size());
    }
}
