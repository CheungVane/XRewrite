/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package com.XRewrite.bean.impl;

import com.XRewrite.bean.ILiteral;
import com.XRewrite.bean.IRule;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>
 * Represents a datalog rule.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Rule implements IRule, Serializable {
	
	private final Set<ILiteral> head;

	private final Set<ILiteral> body;

	Rule(final Collection<ILiteral> head, final Collection<ILiteral> body) {
		if (head == null) {
			throw new IllegalArgumentException("The head must not be null");
		}
		if (head.contains(null)) {
			throw new IllegalArgumentException("The head must not contain null");
		}
		if (body == null) {
			throw new IllegalArgumentException("The body must not be null");
		}
		if (body.contains(null)) {
			throw new IllegalArgumentException("The body must not contain null");
		}
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
