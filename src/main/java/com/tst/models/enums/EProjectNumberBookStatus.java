package com.tst.models.enums;

public enum EProjectNumberBookStatus {
    NEW("NEW"),
    ACCEPT("ACCEPT"),
    CANCEL("CANCEL");

    private final String value;

    EProjectNumberBookStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
