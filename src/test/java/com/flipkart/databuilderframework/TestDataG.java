package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.model.Data;

public class TestDataG extends Data {
    private String value;

    public TestDataG(String value) {
        super("G");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
