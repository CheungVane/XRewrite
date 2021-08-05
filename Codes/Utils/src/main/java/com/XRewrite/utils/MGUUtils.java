package com.XRewrite.utils;

import com.XRewrite.bean.*;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MGUUtils {
    public static boolean unify(final Set<IAtom> atoms, final Map<ITerm, ITerm> substitutionMap) {

        if (atoms.size() > 1) {
            if (!unify(atoms.iterator().next(), atoms.iterator().next(), substitutionMap))
                return false;
            return true;
        } else
            return false;
    }

    public static boolean unify(final IAtom a1, final IAtom a2, final Map<ITerm, ITerm> substitutionMap) {

        if (a1.getPredicate().compareTo(a2.getPredicate()) != 0)
            return false;
        else if (unify(a1.getTuple(), a2.getTuple(), substitutionMap))
            return true;
        else
            return false;

    }

    public static boolean unify( ITuple tup1, ITuple tup2, Map<ITerm, ITerm> variableMap ) {

        if (tup1.size() != tup2.size())
            return false; // Arity-match failed

        if (tup1.size() == 0)
            return true; // Trivial case: Both tuples have no terms [ () unifies with () ]

        boolean unifyTerms = false;

        for ( int i = 0; i < tup1.size(); i++ ) {
            ITerm t1 = tup1.get(i);
            ITerm t2 = tup2.get(i);

            if (t1.isGround() && t2.isGround() && !t1.equals(t2))
                return false;
            unifyTerms = unify(t1, t2, variableMap);
        }
        return unifyTerms;
    }

    public static boolean unify( ITerm t1, ITerm t2, Map<ITerm, ITerm> variableMap)
    {
        if( t1.isGround() && t2.isGround() )
            return t1.equals( t2 ) ;

        if ( t1 instanceof IVariable && t2 instanceof IVariable )
        {
            if (!t1.equals(t2)) {
                if(variableMap.get(t1) != null && !variableMap.get(t1).equals(t2))
                    return false;
                variableMap.put(t1, t2);
            }
            return true;
        }

        if( t1 instanceof IVariable )
        {
            return unifyCheckBinding( (IVariable) t1, t2, variableMap);
        }

        if( t2 instanceof IVariable )
        {
            return unifyCheckBinding( (IVariable) t2, t1, variableMap);
        }

        return false;
    }

    private static boolean unifyCheckBinding( IVariable variable, ITerm term, Map<ITerm, ITerm> variableMap)
    {
        ITerm existingMapping = variableMap.get( variable );
        if( existingMapping == null )
        {
            variableMap.put( variable, term );
            return true;
        }
        if(existingMapping.equals( term ))
            return true;

        return false;
    }

}
