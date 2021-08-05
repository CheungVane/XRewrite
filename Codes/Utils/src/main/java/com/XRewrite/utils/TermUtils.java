package com.XRewrite.utils;

import com.XRewrite.bean.ITerm;
import com.XRewrite.bean.impl.Variable;

import java.util.HashMap;
import java.util.Map;

public class TermUtils {
    private static TermUtils termUtils = new TermUtils();

    public static TermUtils getInstance(){
        return termUtils;
    }

    private static Map<String, ITerm> termMap = new HashMap<>();

    public static void addTerm(String name, ITerm term){
        termMap.put(name, term);
    }

    public static boolean isHas(String name){
        return termMap.containsKey(name);
    }
}
