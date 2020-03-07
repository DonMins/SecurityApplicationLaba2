package com.company.chordAlgorithm;

public class Finger {
    private int start;
    private int[] interval;
    private ChordNode node;

    public Finger(int start, int[] interval, ChordNode node) {
        this.start = start;
        this.interval = interval;
        this.node = node;
    }

    public static int generateStart(int m, int n, int i) {
        return (int) ((n + Math.pow(2, i)) % Math.pow(2, m));
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int[] getInterval() {
        return interval;
    }

    public void setInterval(int[] interval) {
        this.interval = interval;
    }

    public ChordNode getNode() {
        return node;
    }

    public void setNode(ChordNode node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Finger{" + start + "|[" + interval[0] + "," + interval[1] + ")|" + node.getId() + "}";
    }
}
