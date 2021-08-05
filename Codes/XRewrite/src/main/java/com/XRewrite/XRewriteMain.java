package com.XRewrite;

import com.XRewrite.Parser.IParser;
import com.XRewrite.Parser.impl.Parser;
import com.XRewrite.PropagationGraph.PropagationGraph;
import com.XRewrite.bean.*;
import com.XRewrite.utils.ResetManager;


import java.util.*;

public class XRewriteMain {
    //private Map<IPredicate, IRelation> facts;
    private Set<IRule> rules;
    private List<IQuery> queries;
    private String output;
    private XRewriteExecutor executor;
    private IParser parser;
    private Configuration configuration;
    private PropagationGraph pg = null;


    public XRewriteMain(List<String> program) {
        try {
            ResetManager.clean();
            parser = new Parser();
            parser.parse(program);
            this.rules = parser.getRules();
            //System.out.println(parser.getRules());
            this.queries = parser.getQueries();

            this.configuration = Configuration.getInstance();

            StringBuilder output = new StringBuilder();


        } catch (Exception e) {
            output = e.toString();
            e.printStackTrace();
        }
    }

    public void execute(){
        executor = new XRewriteExecutor(this.rules, this.queries, this.pg);
        executor.execute();
        this.output = executor.getOutput().toString();
    }


    public List<Set<IRule>> getOutput() {
        /*for(Set<IRule> set: this.executor.getOutput()){
            for(IRule r:set){
                System.out.println(r);
            }
        }*/
        return new ArrayList<>(this.executor.getOutput());

    }

    /*
    public Map<IPredicate, IRelation> getFacts() {
        return facts;
    }

     */

    public Set<IRule> getRules() {
        return rules;
    }

    public List<IQuery> getQueries() {
        return queries;
    }

    public XRewriteExecutor getExecutor() {
        return executor;
    }

    public IParser getParser() {
        return parser;
    }

    public void setRules(Set<IRule> rules) {
        this.rules = rules;
    }

    public void setQueries(List<IQuery> queries) {
        this.queries = queries;
    }
}
