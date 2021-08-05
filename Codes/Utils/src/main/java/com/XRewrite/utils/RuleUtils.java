package com.XRewrite.utils;

import com.XRewrite.bean.*;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.bean.impl.Position;
import com.XRewrite.bean.impl.StringTerm;
import com.XRewrite.bean.impl.Variable;
import com.XRewrite.cache.SubsumingCache;
import com.XRewrite.cache.NormalizingAuxCache;
import com.XRewrite.cache.RenamingCache;
import com.XRewrite.graph.Graph;
import com.google.common.collect.*;
import javafx.util.Pair;


import java.util.*;

public class RuleUtils {

    public static boolean isSubsumed(final Graph queryGraph, final IRule q, final Set<IRule> explored,
                                     final Set<IRule> newQueries) {

        boolean subsumed = false;
        for (final IRule qPrime : ImmutableSet.copyOf(queryGraph.getRules())) {
            // Compare two different queries!
            if (queryGraph.contains(q) && queryGraph.contains(qPrime) && !q.equals(qPrime)) {
                if (mapsTo(qPrime, q)) {
                    queryGraph.removeAndBypassSuccessors(qPrime, q, explored, newQueries);
                } else if (mapsTo(q, qPrime)) {
                    queryGraph.removeAndBypassSuccessors(q, qPrime, explored, newQueries);
                    subsumed = true;
                    break;
                }
            }
        }

        final long post = queryGraph.getRules().size();

        return subsumed;
    }

    //r1 subsumed by r2
    public static boolean mapsTo(IRule r1, IRule r2){
        Pair<Set<ILiteral>,Set<ILiteral>> pair = new Pair<>(r1.getBody(),r2.getBody());
        if(SubsumingCache.isCached(pair))
            return SubsumingCache.getCache(pair);
        if(r1.getBody().containsAll(r2.getBody())) return true;
        if(!getBodyPredicates(r1).equals(getBodyPredicates(r2))) return false;
        if(r1.getBody().size()!= r2.getBody().size()) return false;
        Map<ITerm,ITerm> map = new HashMap<>();
        List<ILiteral> l1 = new ArrayList<>(r1.getBody());
        List<ILiteral> l2 = new ArrayList<>(r2.getBody());
        l1.sort(Comparator.comparingInt(o -> o.getAtom().getPredicate().hashCode()));
        l2.sort(Comparator.comparingInt(o -> o.getAtom().getPredicate().hashCode()));

        /*System.out.println(getBodyPredicates(r1));
        System.out.println(getBodyPredicates(r2));
        System.out.println("l1: "+l1);
        System.out.println("l2: "+l2);*/

        for(int i=0;i<l1.size();i++){
            for(int j=0;j<l1.get(i).getAtom().getTuple().size();j++){

                ITerm t1 = l1.get(i).getAtom().getTuple().get(j);
                ITerm t2 = l2.get(i).getAtom().getTuple().get(j);
                if(t2.isGround()){
                    if(t1!=(t2)) {
                        SubsumingCache.putCache(pair,false);
                        return false;
                    }
                }else {
                    if(t1.isGround()) continue;
                    if(map.containsKey(t2)) {
                        if(!map.get(t2).equals(t1)) {
                            SubsumingCache.putCache(pair,false);
                            return false;
                        }
                    }else {
                        map.put(t2,t1);
                    }
                }
            }
        }
        SubsumingCache.putCache(pair,true);
        return true;
    }

    public static IRule renaming(IRule r){
        if(RenamingCache.isCache(r)) return RenamingCache.getCache(r);

        Map<ITerm, ITerm> map = new HashMap<>();
        IRule rule = BasicFactory.getInstance().createRule(renaming(r.getHead(),map),renaming(r.getBody(),map));
        RenamingCache.put(r,rule);
        return rule;
    }

    public static List<ILiteral> renaming(Set<ILiteral> literals, Map<ITerm, ITerm> map){
        List<ILiteral> ls = new ArrayList<>(literals);
        final List<ILiteral> renamedLiterals = Lists.newArrayList();
        String prefix = "variable";

        Collections.sort(ls);
        for(ILiteral l: ls){
            final List<ITerm> freshTerms = new ArrayList<ITerm>();
            for(ITerm t: l.getAtom().getTuple()){
                if(t.isGround()){
                    freshTerms.add(t);
                    continue;
                }else {
                    if(map.containsKey(t))
                        freshTerms.add(map.get(t));
                    else {
                        while (TermUtils.isHas(prefix + map.size())){
                            prefix = prefix+1;
                        }
                        IVariable v = new Variable(prefix + map.size());
                        freshTerms.add(v);
                        map.put(t, v);
                    }
                }
            }
            renamedLiterals.add(BasicFactory.getInstance().createLiteral(l.isPositive(), l.getAtom().getPredicate(),
                    BasicFactory.getInstance().createTuple(freshTerms)));
        }
        return renamedLiterals;
    }


    public static Set<ITerm> getHeadVariables(IRule rule) {
        return LiteralUtils.getAllTerms(rule.getHead());

    }

    public static Set<ITerm> getBodyVariables(IRule rule) {
        return LiteralUtils.getAllTerms(rule.getBody());
    }


    public static Set<ITerm> getExistentialVariables(IRule rule) {
        final Set<ITerm> res = new HashSet<ITerm>();
        for (final ITerm v : getHeadVariables(rule)) {
            if (!getBodyVariables(rule).contains(v)) {
                res.add(v);
            }
        }
        return res;
    }

    public static Set<Position> getTermHeadPositions(final IRule rule, final ITerm term) {
        final Set<Position> res = new LinkedHashSet<Position>();

        for (final ILiteral l : rule.getHead()) {
            final IAtom a = l.getAtom();

            int i = 0;
            for (final ITerm t : a.getTuple()) {
                i++;
                if (t.compareTo(term) == 0) {
                    res.add(new Position(a.getPredicate().getPredicateSymbol(), i));
                }
            }
        }
        return res;
    }

    public static Set<Position> getTermBodyPositions(final IRule rule, final ITerm term) {
        final Set<Position> res = new LinkedHashSet<Position>();

        for (final ILiteral l : rule.getBody()) {
            final IAtom a = l.getAtom();

            int i = 0;
            for (final ITerm t : a.getTuple()) {
                i++;
                if (t.compareTo(term) == 0) {
                    res.add(new Position(a.getPredicate().getPredicateSymbol(), i));
                }
            }
        }
        return res;
    }


    public static Set<Position> getExistentialPositions(final IRule r) {

        final Set<Position> exPos = new HashSet<>();

        for (ITerm v : getExistentialVariables(r)) {
            exPos.addAll(getTermHeadPositions(r, v));
            /*for (Position p : getTermHeadPositions(r, v)) {
                for (ILiteral literal : r.getBody()) {
                    final IAtom a = literal.getAtom();
                    if (a.getPredicate().equals(p.getPredicate())) {
                        exPos.add(p);
                    }
                }
            }*/
        }

        return exPos;
    }

    public static Set<Position> getPositions(final Collection<ILiteral> literals) {
        final Set<Position> positions = Sets.newLinkedHashSet();

        // Get positions in literal.
        for (final ILiteral l : literals) {
            final IAtom a = l.getAtom();

            for (int i = 1; i <= a.getTuple().size(); i++) {
                positions.add(new Position(a.getPredicate().getPredicateSymbol(), i));
            }
        }
        return positions;
    }

    public static Set<Position> getPositions(IRule r, ITerm v) {
        final Set<Position> res = new HashSet<Position>();

        for (final ILiteral l : r.getBody()) {
            int pos = 0;
            for (final ITerm t : l.getAtom().getTuple()) {
                pos++;
                if (t.equals(v)) {
                    res.add(new Position(l.getAtom().getPredicate().getPredicateSymbol(), pos));
                }
            }
        }
        for (final ILiteral l : r.getHead()) {
            int pos = 0;
            for (final ITerm t : l.getAtom().getTuple()) {
                pos++;
                if (t.equals(v)) {
                    res.add(new Position(l.getAtom().getPredicate().getPredicateSymbol(), pos));
                }
            }
        }
        return res;
    }

    public static boolean isShared(final IRule rule, final ITerm t) {

        if (t instanceof StringTerm)
            return true;
        int i = 0;
        for (final ILiteral l : rule.getBody()) {
            for (final ITerm term : l.getAtom().getTuple()) {
                if (term.equals(t)) {
                    i++;
                }
                if (i > 1)
                    return true;
            }
        }
        for (final ILiteral l : rule.getHead()) {
            for (final ITerm term : l.getAtom().getTuple()) {
                if (term.equals(t))
                    return true;
            }
        }
        return false;
    }

    public static List<IPredicate> getBodyPredicates(IRule r) {
        final List<IPredicate> predicates = new ArrayList<>();

        for (final ILiteral l : r.getBody()) {
            predicates.add(l.getAtom().getPredicate());
        }

        return predicates;
    }

    public static Set<IRule> normalizingHead(final IRule rule) {
        final Set<IRule> normalized = Sets.newHashSet();

        if (rule.getHead().size() == 1) {
            normalized.add(rule);
        } else {
            // Multiple head atoms
            IBasicFactory basicFactory = BasicFactory.getInstance();
            Set<ITerm> exVars = RuleUtils.getExistentialVariables(rule);
            if(exVars.size()==0) {
                final Collection<ILiteral> head = rule.getHead();
                for (final ILiteral headLit : head) {
                    normalized.add(basicFactory.createRule(ImmutableList.of(headLit), rule.getBody()));
                }
            }else{
                StringBuilder sb = new StringBuilder();
                sb.append("aux");
                for(ILiteral l: rule.getHead()){
                    sb.append(l.getAtom().getPredicate().getPredicateSymbol());
                }
                for(ILiteral l: rule.getBody()){
                    sb.append(l.getAtom().getPredicate().getPredicateSymbol());
                }
                Set<ITerm> headVariables = getHeadVariables(rule);

                String auxName = sb.toString();
                NormalizingAuxCache.putAux(auxName);
                IPredicate auxHeadPredicate = basicFactory.createPredicate(auxName, headVariables.size());
                ITuple auxHeadTuple = basicFactory.createTuple(new ArrayList<>(headVariables));
                IAtom auxAtom = basicFactory.createAtom(auxHeadPredicate,auxHeadTuple);
                ILiteral auxLiteral = basicFactory.createLiteral(true,auxAtom);
                // create aux
                Set<ILiteral> auxSet = Sets.newHashSet(auxLiteral);
                normalized.add(basicFactory.createRule(auxSet, rule.getBody()));

                for(ILiteral l: rule.getHead()){
                    normalized.add(basicFactory.createRule(Sets.newHashSet(l),auxSet));
                }
            }

        }
        return normalized;
    }

    public static Set<Position> getRulePositions(final IRule rule){
        // Get the head positions
        final Set<Position> positions = getHeadPositions(rule);

        // Get the body positions
        positions.addAll(getBodyPositions(rule));

        return positions;
    }

    public static Set<Position> getHeadPositions(IRule rule) {
        return getPositions(rule.getHead());
    }

    public static Set<Position> getBodyPositions(IRule rule) {
        return getPositions(rule.getBody());
    }

    public static Set<ITerm> getFrontierVariables(IRule rule) {
        final Set<ITerm> res = new HashSet<ITerm>();

        for (final ITerm v : getHeadVariables(rule)) {
            if (getBodyVariables(rule).contains(v)) {
                res.add(v);
            }
        }
        return res;
    }
}
