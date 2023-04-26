package com.simulator;

import com.simulator.controller.CacheController;
import com.simulator.controller.OptimalCacheController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Simulator {

    final static  String BLOCKSIZE = "block_size";
    final static  String L1_SIZE = "L1_cache_size";
    final static  String L1_ASSOCIATIVITY = "L1_associativity";
    final static  String L2_SIZE = "L2_cache_size";
    final static  String L2_ASSOCIATIVITY = "L2_associativity";
    final static  String REPLACEMENT_POLICY = "replacement_policy";
    final static  String INCLUSION_POLICY = "inclusion_property";
    static Inclusivity inclusivity;
    static ReplacementPolicy replacementPolicy;
    static int L1_size;
    static int L2_size;
    static int L1_associativity;
    static int L2_associativity;
    static int blockSize;
    static Cache L1 = null;
    static Cache L2 = null;
    static List<String> addresses = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {

       if(validArgs(args)){
           readArguments(args);
       }
       L1 = new Cache(L1_size,blockSize,L1_associativity,inclusivity,replacementPolicy);
       L2 = (L2_size != 0) ? new Cache(L2_size,blockSize,L2_associativity,inclusivity,replacementPolicy): null;
       if(L1.replacementPolicy == ReplacementPolicy.OPTIMAL){
           OptimalCacheController optimalCacheController = new OptimalCacheController(addresses.toArray(new String[0]),L1, L2);
       }
       else {
           CacheController cacheController = new CacheController(addresses.toArray(new String[0]), L1, L2);
       }
    }

    public static boolean validArgs(String args[]){
        boolean ans;
        if (args.length < 8) {
            System.err.println("Not all arguments are provided");
            ans = false;
        }
        else{
            ans = true;
        }
        return ans;
    }

    public static void readArguments(String args[]){
        for(int i = 0; i < args.length; i++){
            int input = 0;
            if(i >= 0 & i < 7){
                input = Integer.parseInt(args[i]);
            }
            if(i == 0){
                if(isValid(input,BLOCKSIZE)){
                    blockSize = input;
                }
                else{
                    System.exit(1);
                }
            }
            else if(i == 1){
                if(isValid(input, L1_SIZE)){
                    L1_size = input;
                }
                else{
                    System.exit(1);
                }
            }
            else if(i == 2){
                if(isValid(input, L1_ASSOCIATIVITY)){
                    L1_associativity = input;
                }
                else{
                    System.exit(1);
                }
            }
            else if(i == 3){
                if(isValid(input, L2_SIZE)){
                    L2_size = input;
                }
                else{
                    System.exit(1);
                }
            }
            else if(i == 4){
                if(input >= 1 && L2_size == 0){
                    System.out.println("invalid as l2 is 0");
                    System.exit(1);
                }
                else if(isValid(input, L2_ASSOCIATIVITY)){
                    L2_associativity = input;
                }
                else{
                    System.exit(1);
                }
            }
            else if(i == 6){
                if(isValidOption(input, INCLUSION_POLICY)){
                    inclusivity = (input == 1) ? Inclusivity.INCLUSIVE : Inclusivity.NONINCLUSIVE;
                }
                else{
                    System.exit(1);
                }
            }
            else if(i == 5){
                if(isValidOption(input, REPLACEMENT_POLICY)){
                    switch(input){
                        case 0 : replacementPolicy = ReplacementPolicy.LRU;
                            break;
                        case 1 : replacementPolicy = ReplacementPolicy.FIFO;
                            break;
                        case 2 : replacementPolicy = ReplacementPolicy.OPTIMAL;
                            break;
                    }
                }
                else{
                    System.exit(1);
                }
            }
            else{
                addresses = readTraceFile(args[i]);
            }
        }
    }

    public static List<String> readTraceFile(String filePath){
        List<String> addresses = addresses = new ArrayList<>();;
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                addresses.add(line);
            }
        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        }
        return addresses;
    }


    public static boolean isValid(int num, String property){
        boolean ans;
         if(num <= 0 && !property.equals(L2_SIZE) && !property.equals(L2_ASSOCIATIVITY)){
            System.out.println(property +" can't be 0 or less");
            ans = false;
        }
        else if((num & (num - 1)) != 0 && !property.equals(L1_ASSOCIATIVITY) && !property.equals((L2_ASSOCIATIVITY))){
             System.out.println(property +" should be power of 2");
             ans = false;
        }
        else{
           ans = true;
        }
        return ans;
    }

    public static boolean isValidOption(int num, String property){
        boolean ans;
        if((num != 0 && num != 1) && property.equals(INCLUSION_POLICY)){
            System.out.println("not a valid option for "+ INCLUSION_POLICY);
            ans = false;
        }
        else if((num != 0 && num != 1 && num != 2) && property.equals(REPLACEMENT_POLICY)){
            System.out.println("not a valid option for "+ REPLACEMENT_POLICY);
            ans = false;
        }
        else{
            ans = true;
        }
        return ans;
    }
}
