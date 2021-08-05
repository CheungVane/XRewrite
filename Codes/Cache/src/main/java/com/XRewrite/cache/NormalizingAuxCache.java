package com.XRewrite.cache;

import java.util.HashMap;
import java.util.HashSet;

public class NormalizingAuxCache {
    private static HashSet<String> auxSet = new HashSet<>();

    public static void putAux(String name){
        auxSet.add(name);
    }

    public static boolean isAux(String name){
        return auxSet.contains(name);
    }

    public static void clean(){
        auxSet.clear();
    }
}
