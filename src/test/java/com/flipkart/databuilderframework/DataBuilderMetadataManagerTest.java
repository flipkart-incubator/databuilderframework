package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.engine.DataBuilderFrameworkException;
import com.flipkart.databuilderframework.engine.DataBuilderMetadataManager;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DataBuilderMetadataManagerTest {
    @Test
    public void testRegister() throws Exception {
        DataBuilderMetadataManager dataBuilderMetadataManager
                = new DataBuilderMetadataManager();
        dataBuilderMetadataManager.register(ImmutableSet.of("A", "B"), "C", "BuilderA", TestBuilderA.class);
        try {
            dataBuilderMetadataManager.register(ImmutableSet.of("A", "B"), "C", "BuilderA", TestBuilderB.class);
        } catch (DataBuilderFrameworkException e) {
            if (e.getErrorCode() == DataBuilderFrameworkException.ErrorCode.BUILDER_EXISTS) {
                return;
            }
        }
        fail("Duplicate error should have come");
    }
}
