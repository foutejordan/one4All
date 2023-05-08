package com.avignon.university.one4all.models;

public class CountType {
    public int type;
    public int count;

    public CountType() {
        type = SejourStatut.RESERVE.getValue();
        count = 0;
    }
}
