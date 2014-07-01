package com.flipkart.cp.convert.europa.databuilderframework;

import com.flipkart.cp.convert.europa.databuilderframework.engine.DataBuilderMetadataManager;
import com.flipkart.cp.convert.europa.databuilderframework.engine.DataFrameworkException;
import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DataBuilderMetadataManagerTest {
    @Test
    public void testRegister() throws Exception {
        DataBuilderMetadataManager dataBuilderMetadataManager
                                        = new DataBuilderMetadataManager();
        dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderA.class );
        try {
            dataBuilderMetadataManager.register(Lists.newArrayList("A", "B"), "C", "BuilderA", TestBuilderB.class );
        } catch (DataFrameworkException e) {
            if(e.getErrorCode() == DataFrameworkException.ErrorCode.BUILDER_EXISTS) {
                return;
            }
        }
        fail("Duplicate error should have come");
    }
}
