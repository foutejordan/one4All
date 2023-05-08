package com.avignon.university.one4all.models;

public enum Role {
    VOYAGEUR(1),
    HOTE(2);

    private final int value;

    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name();
    }

    public static Role intToEnum(int value){
        for (Role r:values()) {
            if(r.value == value){
                return r;
            }
        }
        return VOYAGEUR;
    }
}
