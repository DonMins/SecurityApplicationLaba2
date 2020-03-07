package com.company.chordAlgorithm;

import java.util.Arrays;

public class FingerTable {
    private Finger[] table;

    public FingerTable(int m, int n) {
        this.table = new Finger[m];
        for (int i = 0; i < m; i++) {
            int start = Finger.generateStart(m, n, i);
            int[] interval = new int[]{start, Finger.generateStart(m, n, i + 1)};
            this.table[i] = new Finger(start, interval, null);
        }
    }

    public Finger getFinger(int i) {
        return table[i];
    }

    public Finger[] getTable() {
        return table;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FingerTable{ ");
        Arrays.stream(table).forEach(finger -> sb.append(finger.toString()).append(" "));
        sb.append("}");
        return sb.toString();
    }
}
