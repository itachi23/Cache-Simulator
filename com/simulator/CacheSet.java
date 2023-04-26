package com.simulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class CacheSet {
    public int associativity;
    public LinkedList<int[]> blocks;
    public Queue<int[]> queue;

      public CacheSet(int associativity) {
        setAssociativity(associativity);
        setQueue();
        setLinkedList();
        setBlocks(associativity);
    }

    public void setAssociativity(int associativity) {
        this.associativity = associativity;
    }
    public void setBlocks(int associativity) {

        blocks = new LinkedList<>();
    }

    public void setQueue(){
        this.queue = new LinkedList<>();
    }
    public void setLinkedList(){
        this.blocks = new LinkedList<>();
    }

    public boolean isDirty(int tag){
        boolean ans = false;
        for(int i = 0; i < blocks.size(); i++){
            if(blocks.get(i)[1] == tag){
                if(blocks.get(i)[2] == 1){
                    ans = true;
                    break;
                }
            }
        }
        return ans;
    }
    public void evictBlock(int index){

        this.blocks.remove(index);
    }

    public void addBlock(int tag, int isDirty, int address){
        this.blocks.add(new int[]{1,tag,isDirty,address});

    }

    public int[] getBlock(int index){
        return blocks.get(index);
    }


    public int getBlockIndex(int tag){
        int a = -1;
        for(int i = 0; i < this.blocks.size(); i++){
            if(blocks.get(i)[1]== tag){
                a = i;
                break;
            }
        }
        return a;
    }
    public int isCached(int tag){
        int  ans = -1;
        for(int i = 0; i < blocks.size(); i++){
            if(blocks.get(i)[1] == tag){
                ans = i;
                break;
            }
        }
        return ans;
    }

    public int doesAddressExist(int number){
        int  ans = -1;
        for(int i = 0; i < blocks.size(); i++){
            if(blocks.get(i)[1] == number){
                ans = i;
                break;
            }
        }
        return ans;
    }

    public boolean isFull(){
        return blocks.size() == associativity? true: false;
    }

    public void updateIndex(int index){
        blocks.add(blocks.get(index));
        blocks.remove(index);
    }
    public void addFirst(int tag, int isDirty, int address){
        this.blocks.addFirst( new int[]{1,tag,isDirty,address});
    }
}
