package com.avignon.university.one4all.models;

public enum PanierStatut {
    VALIDE(1),
    AJOUTE(2);

    private final int value;

    private PanierStatut(int value) {
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
