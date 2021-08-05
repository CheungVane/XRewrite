package com.XRewrite;

import com.XRewrite.PropagationGraph.PropagationGraph;
import com.XRewrite.cache.NormalizingAuxCache;
import com.XRewrite.graph.Graph;
import com.XRewrite.utils.*;

import com.XRewrite.bean.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XRewriteExecutor {
    private Set<IRule> rules;
    private List<IQuery> queries;
    private Configuration configuration;
    private PropagationGraph pg;

    private List<IRule> queryRules;
    private Set<IRule> tgds;
    private List<Set<IRule>> output;
    private Report report;


    public XRewriteExecutor(Set<IRule> rules, List<IQuery> queries, PropagationGraph pg){
        this.rules = rules;
        this.queries = queries;
        this.configuration = Configuration.getInstance();
        this.pg = pg;
    }

    public List<Set<IRule>> getOutput() {
        return output;
    }

    public Set<IRule> getRules() {
        return rules;
    }

    public List<IQuery> getQueries() {
        return queries;
    }

    public List<IRule> getQueryRules() {
        return queryRules;
    }

    public Set<IRule> getTgds() {
        return tgds;
    }

    public void execute(){
        distinguishQueriesAndTGDs();

        List<Set<IRule>> rewrittenQueries = new ArrayList<>();
        List<IRule> queries = this.queryRules;

        int i = 0;
        for( IRule query: queries ){
            System.out.println("rewritng for query "+(i++ +1));
            report = ReportUtils.getReport(query.getHead().iterator().next().getAtom().getPredicate().getPredicateSymbol());
            ReportUtils.setCurrentReport(report);
            report.setTGDSize(tgds.size());
            report.setQuerySize(query.getBody().size());

            Set<IRule> iRules = rewriteQuery(query);
            rewrittenQueries.add(iRules);

            this.output = rewrittenQueries;
            int atoms = 0;
            for(IRule r: iRules){
                atoms += r.getBody().size();
            }
            report.setAtoms(atoms);
        }

    }

    public void distinguishQueriesAndTGDs(){
        System.out.println("distinguishing queries and tgds...");

        Set<IRule> rules = this.rules;
        List<IQuery> queries = this.queries;
        this.tgds = RewritingUtils.getTGDs(rules, queries);
        this.queryRules = RewritingUtils.getQueries(rules, queries);
        if(Configuration.getConfiguration().isEnableCommandline())
            System.out.println("executor quries: "+this.queryRules);
        //System.out.println("executor tgds: "+this.tgds);

        if(configuration.isElimination()&&pg==null)
            pg = new PropagationGraph(this.tgds);
    }

    public Set<IRule> rewriteQuery(IRule query){
        Set<IRule> tgds = this.tgds;
        Graph graph = new Graph();

        Set<IRule> unexploredQueries = new HashSet<>();
        if(Configuration.getInstance().isElimination()) {
            long initEliminateTime = System.currentTimeMillis();
            query = CoverageUtils.eliminateQuery(query, pg);
            report.addEliminateTime(System.currentTimeMillis() - initEliminateTime);
            report.setEliminatedQuerySize(query.getBody().size());
        }
        query = RuleUtils.renaming(query);
        if(Configuration.getConfiguration().isEnableCommandline())
            System.out.println("XRewriting..."+query);

        graph.addRule(query);

        unexploredQueries.add(query);

        Set<IRule> exploredQueries = new HashSet<>();

        int time = 1;

        while(!unexploredQueries.isEmpty()){
            if(Configuration.getConfiguration().isEnableCommandline()) {
                System.out.println("doing..." + time+++"..."+unexploredQueries.size());
            }
            report.addGeneratedQueries(unexploredQueries.size());
            Set<IRule> temp = new HashSet<>(unexploredQueries);
            for(IRule q: temp){

                if (!exploredQueries.contains(q)){
                    for(IRule r: tgds){

                        final IPredicate headPred = r.getHead().iterator().next().getAtom().getPredicate();

                        long factorizeTime = System.currentTimeMillis();

                        //do factorization
                        Set<IRule> factorizedQueries = new HashSet<>();
                        factorizedQueries.add(q);
                        if ((RuleUtils.getExistentialVariables(r).size() > 0) && (q.getBody().size() > 1)
                                && RuleUtils.getBodyPredicates(q).contains(headPred)) {
                            Set<IRule> fqs = FactorizationUtils.doFactorization(q, r, exploredQueries, pg);
                            if(fqs.size()>0)
                                factorizedQueries.addAll(fqs);
                        }

                        report.addFactorizeTime(System.currentTimeMillis() - factorizeTime);

                        long rewriteTime = System.currentTimeMillis();
                        //do rewrite for factorized queries
                        for(IRule qf: factorizedQueries){
                            for(ILiteral l: qf.getBody()){
                                IRule rule;
                                if((rule = RewritingUtils.applyRewriteSingle(qf,l.getAtom(),r, pg))!=null){
                                    //System.out.println(r+" : "+qf+" : "+rule);
                                    rule = RuleUtils.renaming(rule);
                                    if(!exploredQueries.contains(rule)&&
                                            !unexploredQueries.contains(rule)){

                                        graph.addRule(q,rule);

                                        if(!Configuration.getInstance().isEnableSubsume()||!RuleUtils.isSubsumed(graph, rule,exploredQueries,unexploredQueries)){
                                            unexploredQueries.add(rule);
                                            if(Configuration.getConfiguration().isEnableCommandline()){
                                                System.out.println("rule: "+r);
                                                System.out.println("l: "+ l);
                                                System.out.println("add query: "+rule);
                                                System.out.println("__________________");
                                            }
                                        }
                                    }
                                    if(!graph.contains(q))
                                        break;
                                }
                            }
                        }
                        report.addRewriteTime(System.currentTimeMillis() - rewriteTime);
                    }
                }
                unexploredQueries.remove(q);
                exploredQueries.add(q);
            }
        }

        Set<IRule> res = new HashSet<>();
        res.add(query);
        for(IRule q: exploredQueries){
            for(ILiteral l: q.getBody()){
                if(NormalizingAuxCache.isAux(l.getAtom().getPredicate().getPredicateSymbol())){
                    continue;
                }
            }
            res.add(q);
        }
        report.setRewrittenQueries(res.size());

        return res;
    }


    public PropagationGraph getPg() {
        return pg;
    }

    public void setPg(PropagationGraph pg) {
        this.pg = pg;
    }
}
