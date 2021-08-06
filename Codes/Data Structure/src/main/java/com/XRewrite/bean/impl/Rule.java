
package com.XRewrite.bean.impl;

import com.XRewrite.bean.ILiteral;
import com.XRewrite.bean.IRule;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Rule implements IRule, Serializable {
	
	private final Set<ILiteral> head;

	private final Set<ILiteral> body;

	Rule(final Collection<ILiteral> head, final Collection<ILiteral> body) {
		this.head = Collections.unmodifiableSet(new HashSet<ILiteral>(head));
		this.body = Collections.unmodifiableSet(new HashSet<ILiteral>(body));
	}
	
	public Set<ILiteral> getHead() {
		return head;
	}
	
	public Set<ILiteral> getBody()
	{
		return body;
	}

	public boolean isRectified() {
		// TODO Auto-generated method stub
		return false;
	}


	public int hashCode() {
		final HashCodeBuilder builder = new HashCodeBuilder(17, 37);
		for (final ILiteral l : getHead()) {
			builder.append(l);
		}
		for (final ILiteral l : getBody()) {
			builder.append(l);
		}
		return builder.toHashCode();
	}
	
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof IRule)) {
			return false;
		}
		IRule r = (IRule) o;
		return body.equals(r.getBody()) && head.equals(r.getHead());
	}
	
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		boolean first = true;
		for (final ILiteral l : head) {
			if( first )
				first = false;
			else
				buffer.append( ", " );
			buffer.append(l);
		}

		buffer.append(" :- ");

		first = true;
		for (final ILiteral l : body) {
			if( first )
				first = false;
			else
				buffer.append(", ");
			buffer.append(l);
		}
		return buffer.toString();
	}
}
