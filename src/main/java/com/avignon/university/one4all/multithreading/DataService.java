package com.avignon.university.one4all.multithreading;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DataService extends Service<DataModel> {
    private final int nRows;
    private final int nCols;

    public DataService(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
    }

    @Override
    protected Task<DataModel> createTask() {
        return new Task<DataModel>() {
            @Override
            protected DataModel call() throws Exception {
                Thread.sleep(5000);
                return new DataModel(nRows, nCols);
            }
        };
    }
}
