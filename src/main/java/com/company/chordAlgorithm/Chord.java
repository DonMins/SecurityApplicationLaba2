package com.company.chordAlgorithm;

import java.util.*;

public class Chord {

    public static void printList(List<ChordNode> nodes, List<Integer> indexes, ChordNode head) {
        System.out.println("___________________________________________________");
        System.out.println("Finger Table: ");


        for (ChordNode node : nodes) {
            System.out.println("Finger Table: ");
            System.out.println("ID: " + node.getId());

            System.out.println("start  interval  succ");
            for (Finger finger : node.getFingerTable().getTable()) {
                System.out.println("  " + finger.getStart() + "  | [ " + finger.getInterval()[0] + ", " + finger.getInterval()[1] + " ) | "
                        + finger.getNode().getId());
            }
            System.out.println("______________________________________________________");
        }
    }

    public static void stabilize(List<ChordNode> nodes) {
        for (int i = 0; i < 100; i++) {
            for (ChordNode node : nodes) {
                node.stabilize();
            }
        }

        for (int i = 0; i < 100; i++) {
            for (ChordNode node : nodes) {
                node.fixFingers();
            }
        }
    }

    public static void main(String[] args) {

        int count = 8;
        int m = (int) (Math.log(count) / Math.log(2));

        List<Integer> indexes = new ArrayList<>();
        indexes.add(0);
        indexes.add(1);
        indexes.add(3);
        List<ChordNode> nodes = new ArrayList<>();

        ChordNode head = null;

        for (int n : indexes) {
            ChordNode node = new ChordNode(m, n);
            node.joinStable(head);
            nodes.add(node);
            if (head == null) {
                head = nodes.get(0);
            }
        }

        stabilize(nodes);
        printList(nodes, indexes, head);
        int data = indexes.get(2);
        System.out.println("Data for node  " + data);
        System.out.println();
        for (Finger finger : head.findById(data).getFingerTable().getTable()) {
            System.out.println(finger.getStart() + " | [ " + finger.getInterval()[0] + ", " + finger.getInterval()[1] + " ] | "
                    + finger.getNode().getId());
        }

        indexes.add(6);
        ChordNode node1 = new ChordNode(m, 6);
        node1.joinStable(head);
        nodes.add(node1);
        if (head == null) {
            head = nodes.get(0);
        }

        node1.join(nodes.get(0));
        node1.join(nodes.get(1));
        node1.join(nodes.get(2));

        stabilize(nodes);
        System.out.println("\nfinger table after joining node 6");
        printList(nodes, indexes, head);


        System.out.println("\nfinger table after removing node 6");
        indexes.remove(3);
        if (head == null) {
            head = nodes.get(0);
        }

        nodes.get(3).remove();
        nodes.remove(3);
        stabilize(nodes);
        printList(nodes, indexes, head);

    }
}
