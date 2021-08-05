package com.XRewrite.utils;

import com.XRewrite.bean.IAtom;
import com.XRewrite.bean.ILiteral;
import com.XRewrite.bean.ITerm;
import com.XRewrite.bean.IVariable;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.bean.impl.Position;
import com.XRewrite.bean.impl.Variable;

import java.util.*;

public class LiteralUtils {
    public static ILiteral renaming(ILiteral l) {
        String prefix = "variable";
        Map<ITerm, ITerm> map = new HashMap<>();
        final List<ITerm> freshTerms = new ArrayList<ITerm>();
        for (ITerm t : l.getAtom().getTuple()) {
            if (map.containsKey(t))
                freshTerms.add(map.get(t));
            else {
                while (TermUtils.isHas(prefix + map.size())) {
                    prefix = prefix + 1;
                }
                IVariable v = new Variable(prefix + map.size());
                freshTerms.add(v);
                map.put(t, v);
            }
        }
        return BasicFactory.getInstance().createLiteral(l.isPositive(), l.getAtom().getPredicate(),
                BasicFactory.getInstance().createTuple(freshTerms));
    }

    public static Set<Position> getTermPositionsInLiteral(ITerm t, ILiteral a) {
        final Set<Position> pos = new HashSet<Position>();

        final List<ITerm> terms = a.getAtom().getTuple();
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).equals(t)) {
                pos.add(new Position(a.getAtom().getPredicate().getPredicateSymbol(), i + 1));
            }
        }
        return pos;
    }

    public static List<Position> getPositionsinLiteral(ILiteral literal) {
        IAtom atom = literal.getAtom();
        List<Position> positions = new ArrayList<>();
        int pos = 0;
        for (ITerm term : atom.getTuple()) {
            pos++;
            positions.add(new Position(atom.getPredicate().getPredicateSymbol(), pos));
        }

        return positions;
    }

    public static Set<ITerm> getAllTerms(Collection<ILiteral> ls) {
        Set<ITerm> terms = new HashSet<>();
        for (ILiteral l : ls)
            terms.addAll(getTerms(l));
        return terms;
    }

    public static Set<ITerm> getTerms(ILiteral l) {
        Set<ITerm> terms = new HashSet<>();
        for (final ITerm t : l.getAtom().getTuple()) {
            if (t instanceof IVariable && !terms.contains(t)) {
                terms.add((IVariable) t);
            }
        }
        return terms;
    }
}
