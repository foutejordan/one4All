package com.avignon.university.one4all.models;

public enum Decision {
    ACCEPTEE(1),
    REFUSEE(2);

    private final int value;

    private Decision(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name()+"(" + value +')';
    }
}
