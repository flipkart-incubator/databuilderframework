package com.flipkart.databuilderframework.engine;

/**
 * A generic factory to build any {@link DataBuilder}.
 * Used by {@link DataFlowExecutor} during flow execution.
 */
public interface DataBuilderFactory {
    /**
     * Create a {@link DataBuilder} of the given name.
     * @param builderName Name of the builder to create
     * @return A {@link DataBuilder}
     * @throws DataFrameworkException
     */
    public DataBuilder create(String builderName) throws DataFrameworkException;
}
