
package com.XRewrite.bean.impl;

import com.XRewrite.bean.ITerm;
import com.XRewrite.bean.ITuple;
import com.XRewrite.bean.IVariable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;


/**
 * <p>
 * A simple tuple implementation. This implementation is thread-safe.
 * </p>
 * <p>
 * $Id: Tuple.java,v 1.20 2007-11-07 16:14:44 nathaliest Exp $
 * </p>
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.20 $
 */
public class Tuple extends AbstractList<ITerm> implements ITuple, Serializable {

	/** The terms stored in this tuple. */
	private final ITerm[] terms;
	
	/**
	 * Creates a tuple defined by the list of terms.
	 * 
	 * @param t list of terms that create a tuple
	 * @throws NullPointerException if terms is <code>null</code>
	 */
	Tuple(final Collection<ITerm> t){
		if (t == null) {
			throw new NullPointerException("Input argument must not be null");
		}
		terms = t.toArray(new ITerm[t.size()]);
	}
	
	public int size() {
		return terms.length;
	}

	public ITerm get(final int i) {
		if (i < 0) {
			throw new IllegalArgumentException("The index must be positive, but was " + i);
		}
		if (i >= terms.length) {
			throw new IllegalArgumentException(
					"The index must not be greater or equal to the size (" + 
					size() + "), but was " + i);
		}
		return terms[i];
	}

	public ITuple append(final Collection<? extends ITerm> t) {
		if (t == null) {
			throw new IllegalArgumentException("The term list must not be null");
		}

		if (t.isEmpty()) {
			return this;
		}

		final List<ITerm> res = new LinkedList<ITerm>(this);
		for (final ITerm term : t) {
			res.add(term);
		}
		return new Tuple(res);
	}

	public boolean isGround() {
		for (final ITerm t : terms){
			if(!t.isGround()) {
				return false;
			}
		}
		return true;
	}

	public String toString(){
		if (terms.length <= 0) {
			return "()";
		}
		final StringBuilder buffer = new StringBuilder();
		buffer.append('(');
		boolean first = true;
		for (final ITerm t : terms) {
			if( first )
				first = false;
			else
				buffer.append(", ");
			buffer.append(t);
		}
		buffer.append(')');
		return buffer.toString();
	}

	public int compareTo(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("Cannot compare with null");
		}
		
		int res = 0;
		for (int i = 0; i < Math.min(terms.length, t.size()); i++) {
			if ((res = terms[i].compareTo(t.get(i))) != 0) {
				return res;
			}
		}
		return terms.length - t.size();
	}

	@Override public int hashCode() {
		final HashCodeBuilder builder = new HashCodeBuilder(53, 149);
		for (final ITerm t : terms) {
			builder.append(t);
		}
		return builder.toHashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof Tuple)) {
			return false;
		}
		return super.equals(o);
	}

	public Set<IVariable> getVariables() {
		final Set<IVariable> variables = new HashSet<IVariable>();
		for (final ITerm term : terms) {
			if(term instanceof IVariable) {
				variables.add((IVariable) term);
			}

		}
		return variables;
	}


	public List<IVariable> getAllVariables() {
		final List<IVariable> variables = new ArrayList<IVariable>();
		for (final ITerm term : terms) {
			if (term instanceof IVariable) {
				variables.add((IVariable) term);
			}
		}
		return variables;
	}
	

}
