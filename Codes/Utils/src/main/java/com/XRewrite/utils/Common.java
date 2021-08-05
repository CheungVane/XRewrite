package com.XRewrite.utils;

import com.XRewrite.bean.*;
import com.XRewrite.bean.impl.BasicFactory;


import java.util.*;

public class Common {
    public static ILiteral applyMGU(final ILiteral l, final Map<ITerm, ITerm> map) {
        final IBasicFactory bf = BasicFactory.getInstance();
        final List<ITerm> freshTerms = new LinkedList<ITerm>();
        IAtom a = l.getAtom();
        boolean applied;
        ITuple t = a.getTuple();
        final Set<ITuple> generated = new LinkedHashSet<ITuple>();

        do {
            generated.add(t);
            applied = false;
            final Iterator<ITerm> kIt = map.keySet().iterator();
            while (kIt.hasNext()) {
                freshTerms.clear();
                final ITerm k = kIt.next();
                final Iterator<ITerm> tIt = t.iterator();
                while (tIt.hasNext()) {
                    final ITerm v = tIt.next();
                    //System.out.println("v: "+v);
                    if (v.getValue().equals(k.getValue())) {
                        freshTerms.add(map.get(k));
                        applied = true;
                        //System.out.println("old v: "+v+"    old k:"+k);
                    } else {
                        freshTerms.add(v);
                        //System.out.println("fresh v: "+v);
                    }
                }
                t = bf.createTuple(freshTerms);
                //System.out.println("t: "+t);
            }

        } while (applied && !generated.contains(t));
        final ILiteral literal = bf.createLiteral(true, bf.createAtom(a.getPredicate(), t));
        return literal;
    }

    public static IRule applyMGU(final IRule r, final Map<ITerm, ITerm> map) {
        List<ILiteral> head = new ArrayList<>();
        List<ILiteral> body = new ArrayList<>();
        for(ILiteral l: r.getHead()){
            head.add(applyMGU(l,map));
        }
        for(ILiteral l: r.getBody()){
            body.add(applyMGU(l,map));
        }
        return BasicFactory.getInstance().createRule(head,body);
    }

    public static List<ILiteral> applyMGU(List<ILiteral> ls, final Map<ITerm, ITerm> map){
        List<ILiteral> res = new ArrayList<>();

        for(ILiteral l:ls){
            res.add(applyMGU(l,map));
        }
        return res;
    }
}
