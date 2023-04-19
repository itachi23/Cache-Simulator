package com.simulator.controller;

import com.simulator.Cache;

import java.util.Optional;

public class FIFOCacheController {
    private Cache L1;
    private Cache L2;

    public FIFOCacheController(Cache L1, Optional<Cache> L2){
        this.L1 = L1;
        this.L2 = (L2.isPresent())? L2.get() : null;
    }

}
