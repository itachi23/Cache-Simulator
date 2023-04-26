package com.simulator;

import java.util.Arrays;

public class Cache {

    public int size;
    public int blockSize;
    public int associativity;
    public CacheSet[] sets;
    public Inclusivity inclusivity;
    public ReplacementPolicy replacementPolicy;

    public Cache(int size, int blockSize, int associativity ,Inclusivity inclusivity, ReplacementPolicy replacementPolicy){
        setSize(size);
        setBlockSize(blockSize);
        setAssocaitivity(associativity);
        setSets(size,blockSize,associativity);
        setInclusivity(inclusivity);
        setReplacementPolicy(replacementPolicy);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }


    public void setInclusivity(Inclusivity inclusivity) {
        this.inclusivity = inclusivity;
    }


    public void setReplacementPolicy(ReplacementPolicy replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }

    public void setAssocaitivity(int associativity) {
        this.associativity = associativity;
    }

    public void setSets(int size, int blockSize, int associativity){
        int setSize = size/(blockSize * associativity);
        this.sets = new CacheSet[setSize];
        for(int i = 0; i < setSize; i++){
            sets[i] = new CacheSet(associativity);
        }
    }

    public CacheSet getSet(int setNumber){
        return this.sets[setNumber];
    }


    @Override
    public String toString() {
        return "Cache{" +
                "size=" + size +
                ", blockSize=" + blockSize +
                ", associativity=" + associativity +
                //", sets=" + Arrays.toString(sets) +
                ", inclusivity=" + inclusivity +
                ", replacementPolicy=" + replacementPolicy +
                '}';
    }

}
