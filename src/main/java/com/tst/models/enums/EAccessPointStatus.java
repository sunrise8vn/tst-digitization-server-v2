package com.tst.models.enums;

public enum EAccessPointStatus {
    PROCESSING("PROCESSING"),
    FULL("FULL"),
    DONE("DONE");

    private final String value;

    EAccessPointStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
