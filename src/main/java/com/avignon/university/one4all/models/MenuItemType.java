package com.avignon.university.one4all.models;

public enum MenuItemType {
    DASHBOARD(1),
    PANIER(2),
    HISTORIQUE(3),
    SEJOURS(4),
    VALIDATION(5);

    private final int value;

    private MenuItemType(int value) {
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
