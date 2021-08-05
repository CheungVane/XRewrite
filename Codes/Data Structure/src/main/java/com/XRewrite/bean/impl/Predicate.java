
package com.XRewrite.bean.impl;


import com.XRewrite.bean.IPredicate;

import java.io.Serializable;

/**
 * <p>
 * This is a simple IPredicate implementation.
 * </p>
 * <p>
 * NOTE: This implementation is immutable
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Predicate implements IPredicate, Serializable {

	private final String symbol;
	
	/** A (unique) string containing the predicate name and arity. */
	private final String symbolPlusArity;

	private final int arity;
	
	Predicate(final String symbol, final int arity) {
		this.symbol = symbol;
		this.arity = arity;
		
		StringBuilder b = new StringBuilder();
		
		b.append( symbol ).append( '$' ).append( arity );
		symbolPlusArity = b.toString();
	}

	public String getPredicateSymbol() {
		return symbol;
	}

	public int getArity() {
		return arity;
	}

	public int hashCode() {
		return symbolPlusArity.hashCode();
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Predicate)) {
			return false;
		}
		Predicate p = (Predicate) o;
		return symbolPlusArity.equals(p.symbolPlusArity);
	}

	public int compareTo(IPredicate o) {
		Predicate predicate = (Predicate) o;
		return symbolPlusArity.compareTo( predicate.symbolPlusArity );
	}

	public String toString() {
		return symbol;
	}
}
