package com.XRewrite.utils;

import com.XRewrite.Configuration;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.PropagationGraph.PropagationGraph;

import com.XRewrite.bean.*;


import java.util.*;

public class RewritingUtils {

    public static List<IRule> getQueries(final Set<IRule> rules, final List<IQuery> queries) {

        final List<IRule> queryList = new ArrayList<>();

        for (final IQuery q : queries)
            for (final ILiteral ql : q.getLiterals()) {
                for (final IRule r : rules) {
                    if (r.getHead().iterator().next().getAtom().getPredicate().equals(ql.getAtom().getPredicate())) {
                        queryList.add(getQueryWithData(BasicFactory.getInstance().createRule(r), ql));
                    }
                }
            }

        return queryList;
    }

    public static IRule getQueryWithData(IRule rule, ILiteral query) {
        HashMap<ITerm, ITerm> map = new HashMap();

        MGUUtils.unify(query.getAtom(), rule.getHead().iterator().next().getAtom(), map);
        return Common.applyMGU(rule, map);
    }


    public static Set<IRule> getTGDs(final Set<IRule> rules, final List<IQuery> queryHeads) {
        Set<IRule> tgdList = new HashSet<>();
        for (final IRule r : rules) {
            for (final IQuery q : queryHeads)
                if (!r.getHead().iterator().next().getAtom().getPredicate().equals(q.getLiterals().get(0).getAtom().getPredicate())) {
                    tgdList.add(r);
                }
        }
        return tgdList;
    }

    public static IRule applyRewriteSingle(IRule q, IAtom a, IRule r, PropagationGraph pg) {
        final Map<ITerm, ITerm> gamma = new HashMap<ITerm, ITerm>();

        if (!MGUUtils.unify(a, r.getHead().iterator().next().getAtom(), gamma)) return null;
        if (RuleUtils.getExistentialVariables(r).size() > 0) {
            // test if the shared variables in q will be preserved by the rewriting
            // For each term in a
            for (int i = 0; i < a.getTuple().size(); i++) {
                if ((a.getTuple().get(i).isGround() || RuleUtils.isShared(q, a.getTuple().get(i)))
                        && !RuleUtils.getBodyVariables(r).contains(r.getHead().iterator().next().getAtom().getTuple().get(i))) {
                    return null;
                }
            }
        }

        if (Configuration.getInstance().isEnableCommandline())
            System.out.println("gamma: " + gamma);

        final Set<ILiteral> qPrimeHeadLiterals = new HashSet<>();
        final Set<ILiteral> qPrimeBodyLiterals = new HashSet<>();


        // Rewrite the atom a in the query q with the atoms in body producing a
        // query q'
        // For each literal in the body of q
        for (final ILiteral l : q.getBody()) {
            if (!l.getAtom().equals(a)) {
                qPrimeBodyLiterals.add(Common.applyMGU(l, gamma));
            }
        }
        for (final ILiteral l : r.getBody()) {
            qPrimeBodyLiterals.add(Common.applyMGU(l, gamma));
        }

        IRule rule = BasicFactory.getInstance().createRule(q.getHead(), qPrimeBodyLiterals);

        //System.out.println(rule);

        if (Configuration.getInstance().isElimination()) {
            long rewriteEliminateTime = System.currentTimeMillis();
            rule = CoverageUtils.eliminateQuery(rule, pg);
            ReportUtils.getCurrentReport().addEliminateTime(System.currentTimeMillis() - rewriteEliminateTime);
        }
        return rule;
    }


}
