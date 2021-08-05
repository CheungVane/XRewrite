package com.XRewrite.utils;

import com.XRewrite.Configuration;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.bean.impl.Position;
import com.XRewrite.PropagationGraph.PropagationGraph;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.XRewrite.bean.*;


import java.util.*;

public class FactorizationUtils {
    public static Set<IRule> doFactorization(IRule q, IRule r, Set<IRule> exploredQueries, PropagationGraph pg) {
        final Set<IRule> factorizedQueries = new HashSet<IRule>();
        if (q.getBody().size() > 1) {
            IPredicate rHead = r.getHead().iterator().next().getAtom().getPredicate();
            Set<Position> headExPos = RuleUtils.getExistentialPositions(r);
            HashSet<IAtom> iAtoms = new HashSet<>();
            for (final ILiteral l : q.getBody()) {
                final IAtom qbodyAtom = l.getAtom();
                if (qbodyAtom.getPredicate().equals(rHead)) {
                    iAtoms.add(qbodyAtom);
                }
            }
            if (iAtoms.size() < 2) return factorizedQueries;
            Set<Set<IAtom>> powerSets = Sets.powerSet(iAtoms);
            final List<Set<IAtom>> sortedPowSet = Lists.newArrayList(powerSets);
            Collections.sort(sortedPowSet, (o1, o2) -> {
                if (((Set<?>) o1).size() < ((Set<?>) o2).size())
                    return 1;
                else if (((Set<?>) o1).size() > ((Set<?>) o2).size())
                    return -1;
                else
                    return 0;
            });

            final Set<Set<IAtom>> usedSets = new HashSet<Set<IAtom>>();


            for (Set<IAtom> iAtomSet : sortedPowSet) {
                if (iAtomSet.size() > 1 && !subsumed(iAtomSet, usedSets)) {
                    final Map<ITerm, ITerm> unifier = new HashMap<ITerm, ITerm>();
                    if (MGUUtils.unify(iAtomSet, unifier)) {
                        final Set<IVariable> variables = getVariablesInPositions(iAtomSet, headExPos);
                        for (final IVariable var : variables) {
                            // check that the variable does not occur in non-existential positions
                            if (headExPos.containsAll(RuleUtils.getPositions(q, var)) && containedInAllAtoms(var, iAtomSet)) {
                                usedSets.add(iAtomSet);
                                IRule newRule = RuleUtils.renaming(factoriseQuery(q, unifier, pg));
                                if (!exploredQueries.contains(newRule)) {
                                    factorizedQueries.add(newRule);
                                }
                            }
                        }
                    }
                }
            }

            //System.out.println("factorizedQueries: " + factorizedQueries);

            return factorizedQueries;
        }


        return factorizedQueries;
    }

    public static IRule factoriseQuery(final IRule q, final Map<ITerm, ITerm> map, final PropagationGraph pg) {

        // The list containing the literals for q'
        final Set<ILiteral> qPrimeBodyLiterals = new HashSet<ILiteral>();
        final Set<ILiteral> qPrimeHeadLiterals = new HashSet<ILiteral>();

        // For each literal in the body of q
        for (final ILiteral curLit : q.getBody()) {
            // add the non unified atoms of q to q'
            qPrimeBodyLiterals.add(BasicFactory.getInstance().createLiteral(Common.applyMGU(curLit, map)));
        }


        // for each literal (should be one) in the head of q
        for (final ILiteral curLit : q.getHead()) {
            // Apply the unification also to the head
            qPrimeHeadLiterals.add(BasicFactory.getInstance().createLiteral(Common.applyMGU(curLit, map)));
        }



        IRule factor = BasicFactory.getInstance().createRule(qPrimeHeadLiterals, qPrimeBodyLiterals);

        if (Configuration.getInstance().isElimination()) {
            long factorizeEliminateTime = System.currentTimeMillis();
            factor = CoverageUtils.eliminateQuery(factor, pg);
            ReportUtils.getCurrentReport().addEliminateTime(System.currentTimeMillis() - factorizeEliminateTime);
        }
        return factor;
    }


    private static boolean containedInAllAtoms(final IVariable var, final Set<IAtom> candidateSet) {

        for (final IAtom atom : candidateSet) {
            if (!atom.getTuple().contains(var))
                return false;
        }
        return true;
    }

    private static Set<IVariable> getVariablesInPositions(final Set<IAtom> candidateSet, final Set<Position> positions) {
        final Set<IVariable> exPosVariables = new LinkedHashSet<IVariable>();
        for (final IAtom atom : candidateSet) {
            int pos = 0;
            for (final ITerm t : atom.getTuple()) {
                pos++;
                final Position curPos = new Position(atom.getPredicate().getPredicateSymbol(), pos);
                if (t instanceof IVariable && positions.contains(curPos)) {
                    exPosVariables.add((IVariable) t);
                }
            }
        }
        return exPosVariables;
    }

    private static boolean subsumed(final Set<IAtom> sub, final Set<Set<IAtom>> usedSet) {
        for (final Set<IAtom> used : usedSet) {
            if (used.size() < sub.size())
                return false;
            if (used.containsAll(sub))
                return true;
        }
        return false;
    }
}
