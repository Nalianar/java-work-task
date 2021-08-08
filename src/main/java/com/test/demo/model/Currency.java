package com.test.demo.model;

public enum Currency {
    USD, GBP, EUR;

    public String getNameLowerCase() {
        return name().toLowerCase();
    }
    public String getName() {
        return name();
    }
}
