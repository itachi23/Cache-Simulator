package com.simulator;

public class Cache {

    private int size;
    private int blockSize;
    private int associativity;

    private CacheSet[] sets;
    private Inclusivity inclusivity;
    private ReplacementPolicy replacementPolicy;

    public Cache(int size, int blockSize, int associativity ,Inclusivity inclusivity, ReplacementPolicy replacementPolicy){
        setSize(size);
        setBlockSize(blockSize);
        setAssocaitivity(associativity);
        setInclusivity(inclusivity);
        setReplacementPolicy(replacementPolicy);
    }
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public Inclusivity getInclusivity() {
        return inclusivity;
    }

    public void setInclusivity(Inclusivity inclusivity) {
        this.inclusivity = inclusivity;
    }

    public ReplacementPolicy getReplacementPolicy() {
        return replacementPolicy;
    }

    public int getAssocaitivity() {
        return associativity;
    }

    public void setReplacementPolicy(ReplacementPolicy replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }

    public void setAssocaitivity(int associativity) {
        this.associativity = associativity;
    }

    public void setSets(int size, int blockSize, int associativity){
        int setSize = size/(blockSize * associativity);
        this.sets = new CacheSet[size];
        for(int i = 0; i < size; i++){
            sets[i] = new CacheSet(associativity, setSize);
        }
    }

}
