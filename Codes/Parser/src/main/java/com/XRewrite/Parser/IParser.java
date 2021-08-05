package com.XRewrite.Parser;

import com.XRewrite.bean.*;

import java.util.List;
import java.util.Set;

public interface IParser {

    public void parse(List<String> program);
    public Set<IRule> parseRule(String ruleString);
    public Set<ILiteral> parseLiterals(String literalString);
    public ILiteral parseLiteral(String literalString);
    public IAtom parseAtom(String atomString);
    public IQuery parseQuery(String literalString);
    public IPredicate parsePredicate(String symbol, int arity);
    public ITuple parseTuple(List<ITerm> terms);

    public Set<IRule> getRules();
    public List<IQuery> getQueries();


}
