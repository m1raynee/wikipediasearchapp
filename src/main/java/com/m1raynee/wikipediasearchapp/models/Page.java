package com.m1raynee.wikipediasearchapp.models;

public class Page {
    private int pageid;
    private String title;

    public Page() {}

    public int getPageid() {
        return pageid;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + " (ID: " + pageid + ")";
    }
}
