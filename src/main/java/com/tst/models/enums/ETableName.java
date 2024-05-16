package com.tst.models.enums;

public enum ETableName {
    ParentsChildrenExtractShort("CMCS"),
    ParentsChildrenExtractFull("CMCF"),
    BirthExtractShort("KSS"),
    BirthExtractFull("KSF"),
    MarryExtractShort("KHS"),
    MarryExtractFull("KHF"),
    WedlockExtractShort("HNS"),
    WedlockExtractFull("HNF"),
    DeathExtractShort("KTS"),
    DeathExtractFull("KTF");

    private final String value;

    ETableName(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
