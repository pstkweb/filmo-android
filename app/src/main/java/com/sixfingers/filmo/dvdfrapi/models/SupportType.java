package com.sixfingers.filmo.dvdfrapi.models;

public enum SupportType {
    ALL("ALL"),
    DVD("DVD"),
    BLU_RAY("BRD"),
    HD_DVD("HDDVD"),
    HD("HD"),
    UMD("UMD");

    private String name = "";

    SupportType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
