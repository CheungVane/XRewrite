package com.XRewrite.cache;

import com.XRewrite.bean.IRule;

import java.util.HashMap;
import java.util.Map;

public class RenamingCache {
    private static Map<IRule,IRule> renamingMap = new HashMap<>();

    public static void put(IRule r1,IRule r2){
        renamingMap.put(r1,r2);
    }

    public static boolean isCache(IRule r){
        return renamingMap.containsKey(r);
    }

    public static IRule getCache(IRule r){
        return renamingMap.get(r);
    }

    public static void clean(){
        renamingMap.clear();
    }

}
