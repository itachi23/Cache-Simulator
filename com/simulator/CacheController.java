package com.simulator;

import java.util.Optional;

public class CacheController {
    private Cache L1;
    private Cache L2;

    public CacheController(Cache L1, Optional<Cache> L2){
        this.L1 = L1;
        this.L2 = L2.get();
    }

}
