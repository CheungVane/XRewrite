
package com.XRewrite.bean;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IBasicFactory {

	public IAtom createAtom(IPredicate p, ITuple tuple);


	public IAtom createAtom(final IAtom a);

	public ILiteral createLiteral(boolean isPositive, IAtom atom);
	public ILiteral createLiteral(boolean isPositive, IPredicate predicate,
			ITuple tuple);


	public ILiteral createLiteral(final ILiteral l);
	
	public IPredicate createPredicate(String symbol, int arity);
	
	public IQuery createQuery(ILiteral... literals);
	public IQuery createQuery(List<ILiteral> literals);
	public IQuery createQuery(Set<ILiteral> literals);


	public IRule createRule(Collection<ILiteral> head, Collection<ILiteral> body);
	public IRule createRule(IRule rule);
	public ITuple createTuple(ITerm... terms);
	public ITuple createTuple(List<ITerm> terms);
}
