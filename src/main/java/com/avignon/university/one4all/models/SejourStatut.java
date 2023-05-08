package com.avignon.university.one4all.models;

public enum SejourStatut {
    AJOUTE_AU_PANIER(1),
    LIBRE(2),
    RESERVE(3),
    EN_COURS_VALIDATION(4),

    REFUSE(5);

    private final int value;

    private SejourStatut(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name();
    }
}
