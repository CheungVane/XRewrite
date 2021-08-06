
package com.XRewrite.bean.impl;

import com.XRewrite.bean.ITerm;
import com.XRewrite.bean.ITuple;
import com.XRewrite.bean.IVariable;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;


public class Tuple extends AbstractList<ITerm> implements ITuple, Serializable {

	private final ITerm[] terms;
	

	Tuple(final Collection<ITerm> t){

		terms = t.toArray(new ITerm[t.size()]);
	}
	
	public int size() {
		return terms.length;
	}

	public ITerm get(final int i) {

		return terms[i];
	}

	public ITuple append(final Collection<? extends ITerm> t) {


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
