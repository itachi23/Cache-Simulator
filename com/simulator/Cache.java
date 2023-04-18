package com.simulator;

public class Cache {

    private int size;
    private int blockSize;
    private int assocaitivity;

    private CacheSet[] sets;
    private Inclusivity inclusivity;
    private ReplacementPolicy replacementPolicy;

    public Cache(int size, int blockSize, Inclusivity inclusivity, ReplacementPolicy replacementPolicy){
        setSize(size);
        setBlockSize(blockSize);
        setAssocaitivity(assocaitivity);
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
        return assocaitivity;
    }

    public void setReplacementPolicy(ReplacementPolicy replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }

    public void setAssocaitivity(int assocaitivity) {
        this.assocaitivity = assocaitivity;
    }

    public void setSets(int size, int blockSize, int assocaitivity){
        int setSize = size/(blockSize * assocaitivity);
        this.sets = new CacheSet[size];
        for(int i = 0; i < size; i++){
            sets[i] = new CacheSet(assocaitivity, setSize);
        }
    }

}
