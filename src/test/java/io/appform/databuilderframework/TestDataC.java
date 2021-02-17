package io.appform.databuilderframework;

import io.appform.databuilderframework.model.Data;

public class TestDataC extends Data {
    private String value;

    public TestDataC(String value) {
        super("C");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
