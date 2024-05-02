package com.tst.models.enums;

public enum ERegistrationType {
    CMC("CMC"),
    KS("KS"),
    KH("KH"),
    HN("HN"),
    KT("KT");

    private final String value;

    ERegistrationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
