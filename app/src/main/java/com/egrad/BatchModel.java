package com.egrad;

public class BatchModel {
    public static final String BATCH_NAME = "BATCH NAME";
    private final String batchName;
    private final int numOfStudents;

    public BatchModel(String batchName, int numOfStudents) {
        this.batchName = batchName;
        this.numOfStudents = numOfStudents;
    }

    public String getBatchName() {
        return batchName;
    }

    public int getNumOfStudents() {
        return numOfStudents;
    }
}
