package io.appform.databuilderframework;

import io.appform.databuilderframework.model.Data;

public class TestDataD extends Data {
    private String value;

    public TestDataD(String value) {
        super("D");
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
