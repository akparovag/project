package com.example.letslearnjava.Models;

public class CategoryModel {

    private String dovID;
    private String name;
    private int noOfTests;

    public CategoryModel(String dovID, String name, int noOfTests) {
        this.dovID = dovID;
        this.name = name;
        this.noOfTests = noOfTests;
    }

    public void setDovID(String dovID) {
        this.dovID = dovID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNoOfTests(int noOfTests) {
        this.noOfTests = noOfTests;
    }

    public String getDovID() {
        return dovID;
    }

    public int getNoOfTests() {
        return noOfTests;
    }

    public String getName() {
        return name;
    }
}
