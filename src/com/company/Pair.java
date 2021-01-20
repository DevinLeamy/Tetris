package com.company;

public class Pair {
    public int first;
    public int second;
    Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public Pair clone() {
        return new Pair(this.first, this.second);
    }
}
