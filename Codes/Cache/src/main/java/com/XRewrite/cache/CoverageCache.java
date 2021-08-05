package com.XRewrite.cache;

import com.XRewrite.bean.ILiteral;
import javafx.util.Pair;

import java.util.HashMap;

public class CoverageCache {
    private static HashMap<Pair<ILiteral, ILiteral>, Boolean> isCovereds = new HashMap<>();

    public static void putCache(Pair<ILiteral, ILiteral> pair, boolean isCovered){
        isCovereds.put(pair,isCovered);
    }

    public static boolean isCached(Pair<ILiteral, ILiteral> pair){
        return isCovereds.containsKey(pair);
    }

    public static boolean getCache(Pair<ILiteral, ILiteral> pair){
        return isCovereds.get(pair);
    }

    public static void clean(){
        isCovereds.clear();
    }
}
