package com.simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

public class Output {
    public int l1Reads;
    public int l2Reads;
    public int l2Writes;
    public int l1Writes;
    public int l1ReadMisses;
    public int l2ReadMisses;
    public int l1WriteMisses;
    public Cache L1;
    public Cache L2;
    public int l2WriteMisses;
    public int l2WriteBacks;
    public int memoryWritebacks;
    public int l1WriteBacks;
    public  double l1MissRate = 0;
    public double l2MissRate = 0;


    public int toMemeory = 0;
    public int totalMemoryTraffic = 0;

    public Output(int l1Reads, int l2Reads, int l1ReadMisses, int l2ReadMisses, int l1WriteMisses,
                  int l2WriteMisses, int l2WriteBacks, int memoryWritebacks, int l1Writes, int l2Writes,
                  int l1WriteBacks, int toMemeory) {
        setL1Reads(l1Reads);
        setL2Reads(l2Reads);
        setL1ReadMisses(l1ReadMisses);
        setL2ReadMisses(l2ReadMisses);
        setL1WriteMisses(l1WriteMisses);
        setL2WriteMisses(l2WriteMisses);
        setL2WriteBacks(l2WriteBacks);
        setMemoryWritebacks(memoryWritebacks);
        setL1Writes(l1Writes);
        setL2Writes(l2Writes);
        setL1WriteBacks(l1WriteBacks);

    }
    public void setCache(Cache c1, Cache c2){
        L1 = c1;
        L2 = c2;
    }
    public void setL1Reads(int l1ReadHits) {
        this.l1Reads = l1Reads;
    }

    public void setL2Reads(int l2ReadHits) {
        this.l2Reads = l2Reads;
    }

    public void setL1ReadMisses(int l1ReadMisses) {
        this.l1ReadMisses = l1ReadMisses;
    }

    public void setL2ReadMisses(int l2ReadMisses) {
        this.l2ReadMisses = l2ReadMisses;
    }

    public void setL1WriteMisses(int l1WriteMisses) {
        this.l1WriteMisses = l1WriteMisses;
    }

    public void setL2WriteMisses(int l2WriteMisses) {
        this.l2WriteMisses = l2WriteMisses;
    }

    public void setL2WriteBacks(int l2WriteBacks) {
        this.l2WriteBacks = l2WriteBacks;
    }

    public void setToMemeory(int toMemeory) {
        this.toMemeory = toMemeory;
    }


    public void setMemoryWritebacks(int memoryWritebacks) {
        this.memoryWritebacks = memoryWritebacks;
    }

    public void setL2Writes(int l2WriteHits) {
        this.l2Writes = l2WriteHits;
    }

    public void setL1Writes(int l1WriteHits) {
        this.l1Writes = l1WriteHits;
    }

    public void setL1(Cache l1) {
        L1 = l1;
    }

    public void setL1WriteBacks(int l1WriteBacks) {
        this.l1WriteBacks = l1WriteBacks;
    }

    @Override
    public String toString() {
        return "Output{" +
                "l1Reads=" + l1Reads +
                ", l2Reads=" + l2Reads +
                ", l2Writes=" + l2Writes +
                ", l1Writes=" + l1Writes +
                ", l1ReadMisses=" + l1ReadMisses +
                ", l2ReadMisses=" + l2ReadMisses +
                ", l1WriteMisses=" + l1WriteMisses +
                ", l2WriteMisses=" + l2WriteMisses +
                ", l2WriteBacks=" + l2WriteBacks +
                ", memoryWritebacks=" + memoryWritebacks +
                ", l1WriteBacks=" + l1WriteBacks +
                '}';
    }


    public void printOutput(Cache L1, Cache L2) throws FileNotFoundException {
        //PrintStream o = new PrintStream(new File("output.txt"));
        //System.setOut(o);
        System.setOut(System.out);
        System.out.println("=====Simulator configuration======");
        System.out.println("BLOCKSIZE:              "+L1.blockSize);
        System.out.println("L1_SIZE:                "+L1.size);
        System.out.println("L1_ASSOC:               "+L1.associativity);
        System.out.println("L2_SIZE:                "+((L2 != null)? L2.size:"0"));
        System.out.println("L2_ASSOC:               "+((L2 != null)? L2.associativity:"0"));
        System.out.println("REPLACEMENT POLICY:     "+L1.replacementPolicy);
        System.out.println("INCLUSION PROPERTY:     "+L1.inclusivity);

        System.out.println("=====L1 Contents=====");
        doPrint(L1);
        if(L2 != null){
            System.out.println("=====L2 Contents=====");
            doPrint(L2);
        }
        System.out.println("===== Simulation results (raw) =====");

        System.out.println("a. number of L1 reads:       "+l1Reads);
        System.out.println("b. number of L1 read misses: "+l1ReadMisses);
        System.out.println("c. number of L1 writes:      "+l1Writes);
        System.out.println("d. number of L1 write misses "+l1WriteMisses);
        System.out.println("e. L1 miss rate:             "+l1MissRate);
        System.out.println("f. number of L1 writebacks:  "+l1WriteBacks);
        System.out.println("g. number of L2 reads:       "+l2Reads);
        System.out.println("h. number of L2 read misses: "+l2ReadMisses);
        System.out.println("i. number of L2 writes:      "+l2Writes);
        System.out.println("j. number of L2 write misses:"+l2WriteMisses);
        System.out.println("k. L2 miss rate:             "+l2MissRate);
        System.out.println("l. number of L2 writebacks:  "+l2WriteBacks);
        System.out.println("m. total memory traffic:     "+totalMemoryTraffic);
    }
    public void doPrint(Cache c) throws FileNotFoundException {

        CacheSet[] sets = c.sets;
        for(int i = 0; i < sets.length; i++){
            List<int[]> blocks = sets[i].blocks;
            System.out.print("Set       "+ i +":" +"             ");
            for(int j = 0; j < sets[i].blocks.size() ; j++){
                int a[] = blocks.get(j);
                System.out.print("   "+convertToHex(a[1]) + " "+ ((a[2] == 1)? "D  " : "   "));
            }
            System.out.println();
        }
    }

    public void calculateMissRate(){
        l1MissRate = (double)(l1ReadMisses + l1WriteMisses)/(l1Reads + l1Writes);
        if(L2!=null)
            l2MissRate = (double)l2ReadMisses /l2Reads;
    }

    public void calculateTraffic(){
        if(L1.inclusivity == Inclusivity.NONINCLUSIVE){
            if(L2!=null){
                totalMemoryTraffic = l2ReadMisses + l2WriteMisses + l2WriteBacks;
            }
            else{
                totalMemoryTraffic = l1ReadMisses + l1WriteMisses + l1WriteBacks;
            }
        }
        else{
                totalMemoryTraffic =  l2WriteBacks + l2ReadMisses + l2WriteMisses + toMemeory;
            }
    }

    public String convertToHex(int num){
        return Integer.toHexString(num);
    }

}
