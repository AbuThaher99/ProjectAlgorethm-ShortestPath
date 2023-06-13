package com.example.projectonealgorethm;

public class Path {
    int cost;
    String path;

    public Path(int cost, String path) {
        this.cost = cost;
        this.path = path;
    }

    public int getCost() {
        return cost;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "" + cost + " ";
    }
}