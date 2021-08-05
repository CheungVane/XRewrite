package com.XRewrite.utils;

import com.XRewrite.Configuration;
import com.XRewrite.bean.*;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.bean.impl.Position;
import com.XRewrite.PropagationGraph.PropagationGraph;
import com.XRewrite.cache.CoverageCache;
import com.google.common.collect.Iterators;
import com.sun.javafx.geom.Edge;
import javafx.geometry.Pos;
import javafx.util.Pair;

import java.util.*;

public class CoverageUtils {

    public static IRule eliminateQuery(IRule rule, PropagationGraph pg){
        if(Configuration.getConfiguration().isEnableCommandline())
            System.out.println("dong eliminating");


        List<ILiteral> literals = new ArrayList<>(rule.getBody());
        List<ILiteral> temp;

        if(literals.size()>1){

            boolean isCovered = true;

            while(isCovered) {
                isCovered = false;
                temp = new ArrayList<>(literals);
                //System.out.println("cover");

                for (int i = 0; (i < (temp.size() - 1)); i++) {

                    for (int j = i + 1; (j < temp.size()); j++) {
                        ILiteral la = Iterators.get(temp.iterator(), i);
                        ILiteral lb = Iterators.get(temp.iterator(), j);

                        if (isCover(la, lb, pg) && literals.contains(lb)) {
                            if(Configuration.getConfiguration().isEnableCommandline())
                                System.out.println(la.getAtom().getPredicate().getPredicateSymbol()+" covered: " + lb.getAtom().getPredicate().getPredicateSymbol());

                            literals.remove(lb);


                            isCovered = true;
                            break;
                        }

                        if (isCover(lb, la, pg) && literals.contains(la)) {
                            if(Configuration.getConfiguration().isEnableCommandline())
                                System.out.println(lb.getAtom().getPredicate().getPredicateSymbol()+" covered: " + la.getAtom().getPredicate().getPredicateSymbol());
                            literals.remove(la);
                            isCovered = true;
                            break;
                        }
                    }
                }
            }
        }
        if(Configuration.getConfiguration().isEnableCommandline())
            System.out.println("finish eliminating");

        return BasicFactory.getInstance().createRule(rule.getHead(), literals);
    }

    public static Map<ITerm,ITerm> getMGUMap(ILiteral head, ILiteral l1, ILiteral l2){
        ITuple headTuple = head.getAtom().getTuple();
        Map<ITerm,ITerm> map = new HashMap<>();
        ITuple tuple1 = l1.getAtom().getTuple();
        ITuple tuple2 = l2.getAtom().getTuple();
        for(int i=0;i<tuple1.size();i++){
            ITerm term1 = tuple1.get(i);
            if(headTuple.contains(term1))
                map.put(tuple2.get(i),term1);
            else map.put(term1, tuple2.get(i));
        }
        return map;
    }

    public static boolean isCover(ILiteral l1, ILiteral l2, PropagationGraph pg){
        Pair<ILiteral,ILiteral> pair = new Pair<>(l1,l2);

        if(CoverageCache.isCached(pair))
            return CoverageCache.getCache(pair);

        boolean isCovered = false;



        Set<ITerm> coveredTerms = new HashSet<>();

        int i =0;
        for(ITerm t2: l2.getAtom().getTuple()){
            i++;
            Position p2 = new Position(l2.getAtom().getPredicate().getPredicateSymbol(),i);
            Set<Position> pl1s = LiteralUtils.getTermPositionsInLiteral(t2, l1);
            for(Position p1: pl1s){
                if(pg.isLinked(p1,p2)){
                    coveredTerms.add(t2);
                }
            }
        }



        if(coveredTerms.containsAll(l2.getAtom().getTuple()))
            isCovered = true;

        CoverageCache.putCache(pair,isCovered);

        if(l1.getAtom().getPredicate().getPredicateSymbol().equals("belongsToCompany")&&l2.getAtom().getPredicate().getPredicateSymbol().equals("FinantialInstrument")){
            System.out.println(isCovered);
            System.out.println(coveredTerms);

        }

        return isCovered;
    }

    public static boolean isPropagatable(ILiteral l1, ILiteral l2) {
        if(l1.equals(l2)) return true;
        //System.out.println(l1.getAtom().getPredicate().getPredicateSymbol()+" : "+l2.getAtom().getPredicate().getPredicateSymbol());
        Map<ITerm, ITerm> map = new HashMap<>();
        List<IVariable> v1 = l1.getAtom().getTuple().getAllVariables();
        List<IVariable> v2 = l2.getAtom().getTuple().getAllVariables();
        for(int i = 0;i<v1.size();i++){
            map.put(v1.get(i),v2.get(i));
        }
        if(l2.equals(Common.applyMGU(l1, map))){
            return true;
        }

        return false;
    }

    public static boolean checkForLoop(List<Edge> ways) {

        for (int i = 0; i < ways.size() - 1; i++) {
            for (int j = 1; j <= i && i + j <= ways.size(); j++) {
                if (ways.subList(i - j, i).equals(ways.subList(i, i + j))) {
                    return true;
                }
            }
        }

        return false;
    }


}
