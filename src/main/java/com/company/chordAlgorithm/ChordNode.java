package com.company.chordAlgorithm;

import java.util.*;

public class ChordNode {
    private int id;
    private FingerTable fingerTable;
    private ChordNode predecessor;

    public ChordNode(int m, int n) {
        this.id = n;
        this.fingerTable = new FingerTable(m, n);
        for (Finger finger : fingerTable.getTable()) {
            finger.setNode(this);
        }
        this.predecessor = this;
    }

    public FingerTable getFingerTable() {
        return fingerTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChordNode getSuccessor() {
        return fingerTable.getFinger(0).getNode();
    }

    public void setSuccessor(ChordNode node) {
        fingerTable.getFinger(0).setNode(node);
    }

    public ChordNode getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(ChordNode predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public String toString() {
        return "ChordNode{ id: " + id + "; predecessor: " + predecessor.getId() + "; FingerTable: " + fingerTable.toString() + " }";
    }

    public boolean idInRange(int id, int a, int b) {
        if (a >= b) {
            if (a > id) {
                id += (int) Math.pow(2, fingerTable.getTable().length);
            }
            b += (int) Math.pow(2, fingerTable.getTable().length);
        }
        return ((id > a) && (id < b));
    }

    public boolean idInRangeLeft(int id, int a, int b) {
        return idInRange(id, a, b) || id == a;
    }

    public boolean idInRangeRight(int id, int a, int b) {
        return idInRange(id, a, b) || id == b;
    }

    public ChordNode findSuccessor(int id) {
        ChordNode node = this.findPredecessor(id);
        return node.getSuccessor();
    }

    public ChordNode findPredecessor(int id) {
        ChordNode node = this;
        while (!idInRangeRight(id, node.getId(), node.getSuccessor().getId())) {
            node = node.closestPrecedingFinger(id);
        }
        return node;
    }

    public ChordNode closestPrecedingFinger(int id) {
        int m = this.fingerTable.getTable().length;
        for (int i = m - 1; i >= 0; i--) {
            ChordNode node = this.getFingerTable().getFinger(i).getNode();
            if (idInRange(node.getId(), this.getId(), id)) {
                return node;
            }
        }
        return this;
    }

    public void join(ChordNode node) {
        if (node != null) {
            initFingerTable(node);
            updateOthers();
        } else {
            for (Finger finger : this.getFingerTable().getTable()) {
                finger.setNode(this);
            }
            this.setPredecessor(this);
        }
    }

    public void initFingerTable(ChordNode node) {
        this.getFingerTable().getFinger(0).setNode(node.findSuccessor(this.getFingerTable().getFinger(0).getStart()));
        this.setPredecessor(getSuccessor().getPredecessor());
        this.getSuccessor().setPredecessor(this);
        int m = this.getFingerTable().getTable().length;
        for (int i = 0; i < m - 1; i++) {
            Finger fingerIPlus1 = this.getFingerTable().getFinger(i + 1);
            Finger fingerI = this.getFingerTable().getFinger(i);
            if (idInRangeRight(fingerIPlus1.getStart(), this.getId(), fingerI.getNode().getId())) {
                fingerIPlus1.setNode(fingerI.getNode());
            } else {
                fingerIPlus1.setNode(node.findSuccessor(fingerIPlus1.getStart()));
            }
        }
    }

    public void updateOthers() {
        int m = this.getFingerTable().getTable().length;
        for (int i = 0; i < m; i++) {
            int id = (int) (this.getId() - Math.pow(2, i));
            id = id < 0 ? (int) (id + Math.pow(2, m)) : id;
            ChordNode p = this.findPredecessor(id);
            p.updateFingerTable(this, i);
        }
    }

    public void updateFingerTable(ChordNode s, int i) {
        Finger fingerI = this.getFingerTable().getFinger(i);
        if (idInRangeLeft(s.getId(), this.getId(), fingerI.getNode().getId())) {
            fingerI.setNode(s);
            ChordNode p = this.getPredecessor();
            p.updateFingerTable(s, i);
        }
    }

    public void remove() {
        getPredecessor().setSuccessor(getSuccessor());
        getSuccessor().setPredecessor(getPredecessor());
        int m = this.getFingerTable().getTable().length;
        for (int i = 0; i < m; i++) {
            int id = (int) (this.getId() - Math.pow(2, i));
            id = id < 0 ? (int) (id + Math.pow(2, m)) : id;
            ChordNode p = this.findPredecessor(id);
//            System.out.println(p.getId());
            p.updateFingerTable(getSuccessor(), i);
        }
    }

    public void joinStable(ChordNode node) {
        if (node != null) {
            setPredecessor(null);
            setSuccessor(node.findSuccessor(this.getId()));
        } else {
            for (Finger finger : this.getFingerTable().getTable()) {
                finger.setNode(this);
            }
            this.setPredecessor(this);
        }
    }

    public void stabilize() {
        ChordNode x = getSuccessor().getPredecessor();
        if (idInRange(x.getId(), this.getId(), getSuccessor().getId())) {
            setSuccessor(x);
        }
        getSuccessor().notify(this);
    }

    public void notify(ChordNode node) {
        if (getPredecessor() == null || idInRange(node.getId(), getPredecessor().getId(), this.getId())) {
            setPredecessor(node);
        }
    }

    public void fixFingers() {
        Random r = new Random();
        int i = r.nextInt(getFingerTable().getTable().length);
        getFingerTable().getFinger(i).setNode(findSuccessor(getFingerTable().getFinger(i).getStart()));
    }

    public ChordNode findById(int id) {
        ChordNode node = this;
        LinkedList<ChordNode> history = new LinkedList<>();
        while (node.getId() != id) {
            history.add(node);
            for (Finger finger : node.getFingerTable().getTable()) {
                if (this.idInRangeLeft(id, finger.getInterval()[0], finger.getInterval()[1])) {
                    node = finger.getNode();
                }
            }
            if (history.contains(node)) {
                return null;
            }
        }
        return node;
    }
}
