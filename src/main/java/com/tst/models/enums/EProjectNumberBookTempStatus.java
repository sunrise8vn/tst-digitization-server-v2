package com.tst.models.enums;

public enum EProjectNumberBookTempStatus {
    NEW("NEW"),
    ORGANIZED("ORGANIZED"),
    ACCEPT("ACCEPT"),
    REORGANIZE("REORGANIZE"),
    CANCEL("CANCEL");

    private final String value;

    EProjectNumberBookTempStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
