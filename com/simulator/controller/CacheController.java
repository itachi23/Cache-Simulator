package com.simulator.controller;

import com.simulator.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;


public class CacheController {
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
    int l2_tag;
    int l2_index;
    String mode;
    String address;
    CacheSet l2_set;
    String l1Address;
    int[] l2Block;
    int correspondingL2Tag;
    int correspondingL1Tag;
    int correspondingL2Index;
    Output output;
    String l2Address;
    int index;
    int index2;
    int number;
    int l2dirtyBit;
    String binaryAddress;
    PrintStream file;
    PrintStream console;
    public CacheController(String[] trace, Cache L1, Cache L2) throws FileNotFoundException {
        this.L1 = L1;
        this.L2 = L2;
        this.trace = trace;
       // System.out.println(L1.toString());
        l1Index = calculateIndex(L1.blockSize, L1.sets.length);
        l1Tag = calculateTag(l1Index[1]);
       // System.out.println("L1- index- "+ ((l1Index[0] -l1Index[1]) + 1) + " tag- "+( (l1Tag[0] -l1Tag[1]) + 1));
        if(L2 != null){
            //System.out.println(L2.toString());
            l2Index = calculateIndex(L2.blockSize,L2.sets.length);
            l2Tag = calculateTag(l2Index[1]);
        }
        output = new Output(0,0,0,0,0,0,0,0,0,0,0,0);
        perform();
        output.setCache(L1, L2);
        output.calculateTraffic();
        output.calculateMissRate();
                //System.out.println(output.toString());
        output.printOutput(L1,L2);

    }

    public void perform(){
        for(String mode_address : trace){
            String[] s =  mode_address.split(" ");
            mode = s[0];
            address  = s[1];
           // System.out.println("new "+address + "  " + mode);
            number = Integer.parseInt(address,16);
            binaryAddress = getBinary(number);
            l1_tag = extractValue(binaryAddress,l1Tag);
            l1_index = extractValue(binaryAddress,l1Index);
            l1_set = L1.getSet(l1_index);
            if(!l1_set.blocks.isEmpty()){
                block = l1_set.getBlock(0);
                index = l1_set.isCached(l1_tag);
            }
            else
                index = -1;

            if(index >= 0){
                if(mode.equals(READ)){
                    output.l1Reads++;
                    if(L1.replacementPolicy == ReplacementPolicy.LRU){
                        l1_set.updateIndex(index);
                    }
                }
                else{
                    output.l1Writes++;
                    l1_set.getBlock(index)[2] = 1;
                    if(L1.replacementPolicy == ReplacementPolicy.LRU){
                        l1_set.updateIndex(index);
                    }
                }
           }
           else{
               if(L2 != null){
                   output.l2Reads++;
                   if(l1_set.isFull() && l1_set.getBlock(0)[2] == 1){
                       output.l1WriteBacks++;
                       output.l2Writes++;
                   }
                   index2 = -1;
                   assignRequiredData();
                   if(index2 >= 0){
                       // l1 miss l2 hit
                     //  System.out.println("l1 miss l2 hit");
                       if(L2.replacementPolicy == ReplacementPolicy.LRU){
                           l2_set.updateIndex(index2);
                           l2Block =  l2_set.getBlock(0);
                           l2Address = getBinary(l2Block[3]);
                           correspondingL1Tag = extractValue(l2Address,l1Tag);
                       }
                       if(mode.equals(READ)){
                           output.l1ReadMisses++;
                           output.l1Reads++;
                       }
                       else{
                           output.l1WriteMisses++;
                           output.l1Writes++;

                       }
                       if(l1_set.isFull()) {
                           handleL1Miss();
                       }
                       //System.out.println("adding in l1 "+l1_index + " "+ l1_tag + " "+number);
                       l1_set.addBlock(l1_tag,mode.equals(READ) ? 0 : 1,number);
                   }
                   else{
                       // miss in l1 and l2
                       output.l2ReadMisses++;
                      // System.out.println("l1 miss l2 miss");
                       if(mode.equals(READ)){
                           output.l1ReadMisses++;
                           output.l1Reads++;
                       }
                       else{
                           output.l1WriteMisses++;
                           output.l1Writes++;
                       }
                       performWriteBack(l2_set);
                      // System.out.println("adding in l2 "+  " "+l2_index + " " +output.convertToHex(l2_tag) + " "+number);
                       l2_set.addBlock(l2_tag, 0,number);
                       if(l1_set.isFull()){
                           handleL1Miss();
                       }
                       //System.out.println("adding in l1 "+ l1_index+ " "+ l1_tag + " "+number);
                       l1_set.addBlock(l1_tag, (mode.equals(READ))? 0 : 1,number);
                   }
               }
               else{
                   if(l1_set.isFull()){
                       if(block[2] == 1){
                           output.l1WriteBacks++;
                       }
                       l1_set.evictBlock(0);
                   }
                   if(mode.equals(READ)){
                       output.l1ReadMisses++;
                       output.l1Reads++;
                   }
                   else{
                       output.l1WriteMisses++;
                       output.l1Writes++;
                   }
                   l1_set.addBlock(l1_tag, mode.equals(READ)?0:1,number);
               }
           }
        }

    }
    public void assignRequiredData(){
        l2_tag = extractValue(binaryAddress, l2Tag);
        l2_index = extractValue(binaryAddress, l2Index);
        l2_set = L2.getSet(l2_index);
        if(!l1_set.blocks.isEmpty()) {
            l1Address = getBinary(block[3]);
            correspondingL2Tag = extractValue(l1Address, l2Tag);
            correspondingL2Index = extractValue(l1Address,l2Index);
            //System.out.println(l1_index + " "+block[1]+" "+ correspondingL2Index+" "+correspondingL2Tag);
        }
        if(!l2_set.blocks.isEmpty()) {
            l2Block = l2_set.getBlock(0);
            index2 = l2_set.isCached(l2_tag);
            if(index2 != -1)
                l2dirtyBit =l2_set.getBlock(index2)[2];
            l2Address = getBinary(l2Block[3]);
            correspondingL1Tag = extractValue(l2Address, l1Tag);
        }
    }

    public void performWriteBack(CacheSet set){
        if(set.isFull()) {
            int[] firstBlock = set.getBlock(0);
            if(L2.inclusivity == Inclusivity.INCLUSIVE){
                evictL1BlockIfPresent(firstBlock[3]);
            }
            if (firstBlock[2] == 1) {
                output.l2WriteBacks++;
            }
            //System.out.println("removing tag "+output.convertToHex(firstBlock[1]));
            set.evictBlock(0);
        }
    }
    public void handleL1Miss(){
//        System.out.println("removing from l1 "+ l1_index + " "+block[1]);
        CacheSet correspondingL2Set = L2.getSet(correspondingL2Index);
        int i = correspondingL2Set.isCached(correspondingL2Tag);
        if(i == -1) {
            output.l2WriteMisses++;
            performWriteBack(correspondingL2Set);
           //System.out.println("adding in l2 from l1 in " + correspondingL2Index + " " + output.convertToHex(correspondingL2Tag) + " " + Integer.parseInt(l1Address, 2));
            correspondingL2Set.addBlock(correspondingL2Tag, block[2], block[3]);
        }
        else {
            correspondingL2Set.getBlock(i)[2] = block[2];
        }
        l1_set.evictBlock(0);
    }


    public void evictL1BlockIfPresent(int addr){
        String forInclusive = getBinary(addr);
        int tag = extractValue(forInclusive,l1Tag);
        int l1_setNumber = extractValue(forInclusive,l1Index);
        CacheSet set2 = L1.getSet(l1_setNumber);
        int i = set2.isCached(tag);
        if(i >= 0){
            if(set2.getBlock(i)[2] == 1){
                output.l1WriteBacks++;
                output.toMemeory++;
            }
            set2.evictBlock(i);
        }
    }
    public String getBinary(int number){
        String binaryAddress = Integer.toBinaryString(number);
        binaryAddress = binaryAddress.length() < 32 ? appendZeros(binaryAddress) : binaryAddress;
        return binaryAddress;
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

}
