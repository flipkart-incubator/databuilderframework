package com.flipkart.databuilderframework;

import com.flipkart.databuilderframework.model.Data;

public class TestDataB extends Data {
    private String value;

    public TestDataB(String value) {
        super("B");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
