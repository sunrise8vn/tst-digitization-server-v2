package com.tst.models.enums;

public enum EProjectNumberBookFileStatus {
    NEW("NEW"),
    ORGANIZED("ORGANIZED"),
    ACCEPT("ACCEPT"),
    REORGANIZE("REORGANIZE"),
    CANCEL("CANCEL");

    private final String value;

    EProjectNumberBookFileStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
