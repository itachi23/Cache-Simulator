package com.simulator;

import java.util.Scanner;

public class Simulator {

    public static void main(String[] args) {
        int cacheLevels;
        Inclusivity inclusivity;
        ReplacementPolicy replacementPolicy;
        Cache L1 = null;
        Cache L2 = null;
        Scanner sc;
        sc = new Scanner(System.in);
        cacheLevels = setCacheLevels(sc);
        inclusivity = setInclusivity(sc);
        replacementPolicy = setReplacementPoliciy(sc);
        for(int i = 1; i <= cacheLevels; i++){
           if(i == 1){
               L1 = createCache(sc, i, inclusivity, replacementPolicy);
           }
           else{
               L2 = createCache(sc, i, inclusivity, replacementPolicy);
           }
        }

    }
    public static int setCacheLevels(Scanner sc){
        int levels;
        while(true){
            System.out.println("Enter number of cache levels: ");
            levels = sc.nextInt();
            if(levels < 1){
                System.out.println("number of cache levels can't be less than 1");
            }
            else if(levels > 2){
                System.out.println("number of cache levels can't be more than 2");
            }
            else{
                break;
            }
        }
        return levels;
    }

    public static ReplacementPolicy setReplacementPoliciy(Scanner sc){
        ReplacementPolicy replacementPolicy = null;
        int input = -1;
        while(input != 0 || input != 1 || input != 3){
            System.out.println("Enter 0 for FIFO 1 for LRU 2 for Optimal replacement policy");
            input = sc.nextInt();
        }
        switch(input){
            case 0 : replacementPolicy = ReplacementPolicy.FIFO;
                break;
            case 1 : replacementPolicy = ReplacementPolicy.LRU;
                break;
            case 2 : replacementPolicy = ReplacementPolicy.OPTIMAL;
                break;
        }
        return replacementPolicy;
    }
    public static Inclusivity setInclusivity(Scanner sc){
        int input = -1;
        while(input != 0 && input != 1 ){
            System.out.println("Enter 1 for inclusivity and 0 for standard procedure");
            input = sc.nextInt();
        }
        return (input == 1) ? Inclusivity.INCLUSIVE : Inclusivity.NONINCLUSIVE;
    }

    public static Cache createCache(Scanner sc, int level, Inclusivity inclusivity, ReplacementPolicy replacementPolicy){
        System.out.println("Enter L" + level + "cache configurations in order of cache size, block size, associativity");
        int size = -3;
        int blockSize = -3;
        int associativity = 0;
        while(!isValid(size, "size") && !isValid(blockSize, "block size") && !isValid(associativity, "associativity")){
            size = sc.nextInt();
            blockSize = sc.nextInt();
            associativity = sc.nextInt();
        }
          return new Cache(size,blockSize,associativity,inclusivity,replacementPolicy);
    }


    public static boolean isValid(int num, String property){
        boolean ans;
        if((num & (num - 1)) == 0){
            ans = true;
        }
        else{
            System.out.println(property + " is invalid. Please enter numbers which are powers of 2");
            ans = false;
        }
        return ans;
    }
}
