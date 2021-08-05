package com.XRewrite.cache;

import com.XRewrite.bean.ILiteral;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Set;

public class SubsumingCache {
    private static HashMap<Pair<Set<ILiteral>,Set<ILiteral>>, Boolean> isMapsTo = new HashMap<>();

    public static void putCache(Pair<Set<ILiteral>,Set<ILiteral>> pair, boolean is){
        isMapsTo.put(pair,is);
    }

    public static boolean isCached(Pair<Set<ILiteral>,Set<ILiteral>> pair){
        return isMapsTo.containsKey(pair);
    }

    public static boolean getCache(Pair<Set<ILiteral>,Set<ILiteral>> pair){
        return isMapsTo.get(pair);
    }

    public static void clean(){
        isMapsTo.clear();
    }
}
