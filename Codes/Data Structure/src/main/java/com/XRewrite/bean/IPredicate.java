
package com.XRewrite.bean;

/**
 * <p>
 * A predicate is either a relation or the boolean-valued function that amounts
 * to the characteristic function or the indicator function of such a relation.
 * </p>
 * <p>
 * A predicate is characterized by a predicate symbol and an arity of the
 * predicate.
 * </p>
 * <p>
 * $Id: IPredicate.java,v 1.7 2007-07-25 08:16:56 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.7 $
 */

public interface IPredicate extends Comparable<IPredicate> {
	/**
	 * <p>
	 * Returns the predicate symbol. <p/>
	 * 
	 * @return The predicate symbol.
	 */
	public String getPredicateSymbol();

	/**
	 * <p>
	 * Returns the arity of the predicate. <p/>
	 * 
	 * @return The arity.
	 */
	public int getArity();
}
