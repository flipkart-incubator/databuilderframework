package io.appform.databuilderframework;

import io.appform.databuilderframework.model.Data;

public class TestDataF extends Data {
    private String value;

    public TestDataF(String value) {
        super("F");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
