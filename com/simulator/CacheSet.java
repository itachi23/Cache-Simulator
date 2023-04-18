package com.simulator;

import java.util.LinkedList;
import java.util.Queue;

public class CacheSet {
    private int associativity;
    private int size;
    private LinkedList<int[]> blocks;

    private Queue<int[]> queue;

    public CacheSet(int associativity, int size) {
        setAssociativity(associativity);
        setSize(size);
        setBlocks(size);
    }

    public int getAssociativity() {
        return associativity;
    }

    public void setAssociativity(int associativity) {
        this.associativity = associativity;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LinkedList<int[]> getBlocks() {
        return blocks;
    }

    public void setBlocks(int size) {
        for(int i = 0; i < size; i++){
            this.blocks.add(new int[3]);
        }
    }

}
