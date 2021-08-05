package com.XRewrite.Parser.impl;



import com.XRewrite.Parser.IParser;
import com.XRewrite.bean.*;
import com.XRewrite.bean.impl.BasicFactory;
import com.XRewrite.bean.impl.StringTerm;
import com.XRewrite.bean.impl.Variable;
import com.XRewrite.utils.LiteralUtils;
import com.XRewrite.utils.RuleUtils;
import com.XRewrite.utils.TermUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser implements IParser {
    private Set<IRule> rules = new HashSet<>();
    private List<IQuery> queries = new ArrayList<>();

    private Set<ITerm> bodyTerms;
    private boolean isQuery = false;

    public Parser(){
    }

    public void parse(List<String> program){
        System.out.println("parsing...");
        for(String line: program){
            if(!line.startsWith("%")&&!line.equals("")){
                if(line.contains("==")|| line.startsWith("/")) continue;

                // raw cleaning
                int i = 0;
                while(true){
                    if(line.contains("!Ex")) {
                        line = line.replace("!Ex" + i, "");
                        i++;
                    }
                    else
                        break;
                }
                String replace = line.replace(".","");

                if(!replace.contains("?-")) {
                    isQuery = false;
                    rules.addAll(parseRule(replace));
                } else{
                    isQuery = true;
                    queries.add(parseQuery(replace.replace("?-","")));
                }
            }
        }
    }

    public Set<IRule> parseRule(String ruleString){
        // raw cleaning
        String replace = ruleString.replace("!Ex0", "").replace(".","");

        String[] hb = replace.split(":-");
        Set<ILiteral> body = parseLiterals(hb[1].trim());
        bodyTerms = LiteralUtils.getAllTerms(body);
        Set<ILiteral> head = parseLiterals(hb[0].trim());
        bodyTerms = null;
        return RuleUtils.normalizingHead(BasicFactory.getInstance().createRule(head, body));
    }

    public Set<ILiteral> parseLiterals(String literalString){
        boolean isVarible = false;
        Set<String> literalsString = new HashSet<>();
        int index = 0;
        for(int i = 0;i<literalString.length();i++){
            char c = literalString.charAt(i);
            if(c=='(') {
                isVarible = true;
                continue;
            }
            if(c==')') {
                isVarible = false;
                continue;
            }
            if(!isVarible){
                if(c==','){
                    literalsString.add(literalString.substring(index,i));
                    index = i+1;
                }
            }
        }
        literalsString.add(literalString.substring(index));
        Set<ILiteral> literals = new HashSet<>();
        for(String l: literalsString){
            literals.add(parseLiteral(l.trim()));
        }

        return literals;
    }

    public ILiteral parseLiteral(String literalString){

        return BasicFactory.getInstance().createLiteral(true,parseAtom(literalString));
    }

    public IAtom parseAtom(String atomString){
        StringBuilder sp = new StringBuilder();
        StringBuilder st = new StringBuilder();
        List<ITerm> terms = new ArrayList<>();
        int arity = 0;

        boolean isVarible = false;
        for(int i =0;i<atomString.length();i++){
            char c = atomString.charAt(i);
            if(c==' ')
                continue;
            if(c=='(') {
                isVarible = true;
                continue;
            }
            if(!isVarible){
                sp.append(c);
            }else {
                if (c == ','||c==')') {
                    ITerm term = new Variable(st.toString());


                    if(isQuery)
                        term = new StringTerm(st.toString());


                    TermUtils.addTerm(st.toString(), term);
                    terms.add(term);
                    st.delete(0, st.length());
                    arity++;
                    continue;
                }
                st.append(c);

            }
        }

        return BasicFactory.getInstance().createAtom(parsePredicate(sp.toString(),arity),parseTuple(terms));
    }

    public IQuery parseQuery(String literalString){
        boolean isVarible = false;
        Set<String> literalsString = new HashSet<>();
        int index = 0;
        for(int i = 0;i<literalString.length();i++){
            char c = literalString.charAt(i);
            if(c=='(') {
                isVarible = true;
                continue;
            }
            if(c==')') {
                isVarible = false;
                continue;
            }
            if(!isVarible){
                if(c==','){
                    literalsString.add(literalString.substring(index,i));
                    index = i+1;
                }
            }
        }
        literalsString.add(literalString.substring(index));
        Set<ILiteral> literals = new HashSet<>();
        for(String l: literalsString){
            literals.add(parseLiteral(l.trim()));
        }

        return BasicFactory.getInstance().createQuery(literals);
    }

    public IPredicate parsePredicate(String symbol, int arity){

        return BasicFactory.getInstance().createPredicate(symbol,arity);
    }

    public ITuple parseTuple(List<ITerm> terms){

        return BasicFactory.getInstance().createTuple(terms);
    }

    public Set<IRule> getRules() {
        return rules;
    }

    public List<IQuery> getQueries() {
        return queries;
    }

}
