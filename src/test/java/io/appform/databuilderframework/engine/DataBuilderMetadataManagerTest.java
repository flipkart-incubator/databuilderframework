package io.appform.databuilderframework.engine;

import io.appform.databuilderframework.complextest.SB;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

public class DataBuilderMetadataManagerTest {

    @Test
    public void testGetConsumesSetFor() throws Exception {
        DataBuilderMetadataManager manager = new DataBuilderMetadataManager();
        Assert.assertEquals(null, manager.getConsumesSetFor("A"));
        manager.register(ImmutableSet.of("CR", "CAID", "VAS"), "OO", "SB", SB.class);
        Assert.assertEquals(1, manager.getConsumesSetFor("CR").size());
    }
}
