package com.simulator.controller;

import com.simulator.Cache;
import com.simulator.CacheSet;
import com.simulator.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OptimalCacheController {
    private Cache L1;
    private Cache L2;
    private String[] trace;
    private int[] l1Tag;
    private int[] l1Index;
    private int[] l2Tag;
    private int[] l2Index;
    final String READ = "r";
    int[] block;
    int l1_tag;
    int l1_index;
    CacheSet l1_set;
    String address;
    Output output;
    int number;
    String mode;
    String binaryAddress;
    int index;

    public OptimalCacheController(String[] trace, Cache L1, Cache L2) throws FileNotFoundException {
        this.L1 = L1;
        this.L2 = L2;
        this.trace = trace;
       // System.out.println(L1.toString());
        l1Index = calculateIndex(L1.blockSize, L1.sets.length);
        l1Tag = calculateTag(l1Index[1]);
        output = new Output(0,0,0,0,0,0,0,0,0,0,0,0);
        perform();
        output.setCache(L1, L2);
        output.calculateTraffic();
        output.calculateMissRate();
        //System.out.println(output.toString());
        output.printOutput(L1,L2);
    }
    public void perform(){

        for(int i = 0; i < trace.length; i++) {
           // System.out.println();
            String mode_address = trace[i];
            String[] s = mode_address.split(" ");

            mode = s[0];
            address = s[1];
            number = Integer.parseInt(address, 16);
           // System.out.print(address);
            binaryAddress = getBinary(number);
            l1_tag = extractValue(binaryAddress, l1Tag);
            l1_index = extractValue(binaryAddress, l1Index);
            l1_set = L1.getSet(l1_index);
            index = l1_set.doesAddressExist(l1_tag);
           // System.out.println("index "+l1_index);
            if(index >= 0){
                //System.out.print("  hit  ");
                if(mode.equals(READ)){
                    output.l1Reads++;
                 //   l1_set.updateIndex(index);
                }
                else{
                    output.l1Writes++;
                    l1_set.blocks.get(index)[2] = 1;
                   // l1_set.updateIndex(index);
                }
            }
            else{
                //System.out.print("  miss  ");

                if (mode.equals(READ)) {
                    output.l1Reads++;
                    output.l1ReadMisses++;
                } else {
                    output.l1Writes++;
                    output.l1WriteMisses++;
                }
                if(l1_set.isFull())
                {
//                    for (int j = 0; j < l1_set.blocks.size(); j++) {
//                        int k =  i + 1;
//
//                    }

                    HashMap<Integer, Integer> map = new HashMap<>();
                    for (int m = 0; m < l1_set.blocks.size(); m++) {
                        map.put(l1_set.blocks.get(m)[3], m);
                    }
                    int min = Integer.MAX_VALUE;
                    int max = Integer.MIN_VALUE;
                    int max_index = -1;
                    int[] appearedFirst = new int[l1_set.associativity];
                    for (int j = i + 1; j < trace.length; j++) {
                        if (map.isEmpty()) {
                            break;
                        }
                        int address =Integer.parseInt(trace[j].split(" ")[1], 16);
                        if (map.containsKey(address)) {
                            appearedFirst[map.get(address)] = j;
                            map.remove(address);
                        }
                    }

                    if (map.isEmpty()) {
                        for (int n = 0; n < appearedFirst.length; n++) {
                          //  System.out.print(output.convertToHex(l1_set.blocks.get(n)[1]) + " = "+appearedFirst[n] + " ");
                            if (appearedFirst[n] > max) {
                                max = appearedFirst[n];
                                max_index = n;

                            }
                        }
                        evict(max_index,l1_index);

                    } else {

                        for (int key : map.keySet()) {
                            int index = map.get(key);
                            min = Math.min(min, index);
                        }
                        evict(min,l1_index);
                    }

                }
              //  System.out.println("adding tag in set "+ l1_index + " " +output.convertToHex(l1_tag));
                l1_set.addBlock(l1_tag, mode.equals(READ) ? 0 : 1, number);


            }
        }
    }
    public void evict(int index,int l1_index){
        if(l1_set.blocks.get(index)[2] == 1){
            output.l1WriteBacks++;
        }
       // System.out.println("removing  "+ index+ " " + l1_index + " " + output.convertToHex(L1.getSet(l1_index).getBlock(index)[1]));
        L1.getSet(l1_index).evictBlock(index);
    }
    public int[] calculateIndex(int blockSize, int setSize){
        int a[] = new int[2];
        a[0] = 31 - (int)(Math.log(blockSize) / Math.log(2));
        a[1] = (a[0] - (int)(Math.log(setSize) / Math.log(2))) + 1;
        return a;
    }
    public int[] calculateTag(int indexEnd){
        int[] a = new int[2];
        a[0] = indexEnd - 1;
        return a;
    }

    public int extractValue(String binaryAddress,int[] a){
        return Integer.parseInt(binaryAddress.substring(a[1], a[0] + 1),2);
    }

    public String appendZeros(String address){
        int intialLength = address.length();
        for(int i = 0; i < 32 - intialLength; i++){
            address = "0" + address;
        }
        return address;
    }
    public String getBinary(int number){
        String binaryAddress = Integer.toBinaryString(number);
        binaryAddress = binaryAddress.length() < 32 ? appendZeros(binaryAddress) : binaryAddress;
        return binaryAddress;
    }
}
