
package com.XRewrite.bean.impl;

import com.XRewrite.bean.IAtom;
import com.XRewrite.bean.IPredicate;
import com.XRewrite.bean.ITuple;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;


/**
 * <p>
 * A simple Atom implementation.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision$
 */
public class Atom implements IAtom, Serializable {

	private final IPredicate predicate;
	
	private final ITuple tuple;

	Atom(final IPredicate predicate, final ITuple tuple) {
		if (predicate == null || tuple == null) {
			throw new IllegalArgumentException("The parameters must not be null");
		}
		if (predicate.getArity() != tuple.size()) {
			throw new IllegalArgumentException("Cannot create an atom when" +
					" a tuple's arity does not match the predicate's arity.");
		}
		this.predicate = predicate;
		this.tuple = tuple;
	}

	public IPredicate getPredicate() {
		return predicate;
	}
	
	public ITuple getTuple() {
		return this.tuple;
	}

	public boolean isGround() {
		return this.tuple.isGround();
	}

	public int compareTo(IAtom o) {
		int res = 0;
		if ((res = predicate.compareTo(o.getPredicate())) != 0) {
			return res;
		}
		if ((res = this.tuple.compareTo(o.getTuple())) != 0) {
			return res;
		}
		return 0;
	}

	@Override public int hashCode() {
		return new HashCodeBuilder(29, 103).append(predicate).append(tuple).toHashCode();
	}

	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Atom)) {
			return false;
		}
		Atom a = (Atom) o;
		
		return (this.predicate.equals(a.predicate)) && 
				this.tuple.equals(a.getTuple());
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(this.predicate);
		buffer.append(this.tuple);
		
		return buffer.toString();
	}

	public boolean isBuiltin() {
		return false;
	}
}
