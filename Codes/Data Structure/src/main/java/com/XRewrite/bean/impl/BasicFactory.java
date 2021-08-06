
package com.XRewrite.bean.impl;

import com.XRewrite.bean.*;

import java.util.*;


public class BasicFactory implements IBasicFactory {

	private static final IBasicFactory FACTORY = new BasicFactory();

	private BasicFactory() {
	}

	public IAtom createAtom(IPredicate p, ITuple tuple) {
		return new Atom(p, tuple);
	}

	public ILiteral createLiteral(boolean isPositive, IAtom atom) {
		return new Literal(isPositive, atom);
	}

	public ILiteral createLiteral(boolean positive, IPredicate p,
			ITuple tuple) {
		return createLiteral(positive, createAtom(p, tuple));
	}

	public IPredicate createPredicate(String symbol, int arity) {
		return new Predicate(symbol, arity);
	}

	public IQuery createQuery(ILiteral... literals) {
		return createQuery(Arrays.asList(literals));
	}

	public IQuery createQuery(List<ILiteral> literals) {
		return new Query(literals);
	}

	@Override
	public IQuery createQuery(Set<ILiteral> literals) {
		return new Query(literals);
	}

	public IRule createRule(Collection<ILiteral> head, Collection<ILiteral> body) {
		return new Rule(head, body); 
	}

	@Override
	public IRule createRule(IRule rule) {
		return createRule(new HashSet<>(rule.getHead()),new HashSet<>(rule.getBody()));
	}

	public ITuple createTuple(ITerm... terms) {
		return createTuple(Arrays.asList(terms));
	}
	
	public ITuple createTuple(List<ITerm> terms) {
		return new Tuple(terms);
	}

	public IAtom createAtom(final IAtom a) {
		return createAtom(a.getPredicate(), createTuple(a.getTuple()));
	}

	public ILiteral createLiteral(final ILiteral l) {
		return createLiteral(l.isPositive(), createAtom(l.getAtom()));
	}

	public static IBasicFactory getInstance() {
		return FACTORY;
	}
}
