package com.XRewrite.bean.impl;

import com.XRewrite.bean.ILiteral;
import com.XRewrite.bean.IQuery;
import com.XRewrite.bean.IVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Query implements IQuery {
	
	private List<ILiteral> literals = null;
	
	Query(final Collection<ILiteral> literals) {

		this.literals = Collections.unmodifiableList(new ArrayList<ILiteral>(literals));
	}

	public List<ILiteral> getLiterals() {
		return literals;
	}

	
	public int hashCode() {
		return literals.hashCode();
	}
	
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof IQuery)) {
			return false;
		}
		IQuery q = (IQuery) o;
		return literals.equals(q.getLiterals());
	}
	
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		boolean first = true;
		for (final ILiteral l : literals) {
			if( first )
				first = false;
			else
				buffer.append( ", " );
			buffer.append(l);
		}
		return buffer.toString();
	}
}
