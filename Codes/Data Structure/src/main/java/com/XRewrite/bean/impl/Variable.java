
package com.XRewrite.bean.impl;


import com.XRewrite.bean.ITerm;
import com.XRewrite.bean.IVariable;

import java.io.Serializable;

/**
 * <p>
 * Simple implementation of the IVariable.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Variable implements IVariable, Serializable {

	private String name = "";

	public Variable(final String name) {
		this.name = name;
	}

	public boolean isGround() {
		return false;
	}

	public int compareTo(ITerm o) {
		if (o == null)
			return 1;
		if(o.isGround()) return -1;
			
		Variable v = (Variable) o;
		
		return name.compareTo( v.getValue() );
	}

	public int hashCode() {
		return name.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof Variable)) {
			return false;
		}
		Variable v = (Variable) o;
		return name.equals(v.name);
	}

	/**
	 * Returns a String representation of this object. The subject of the string
	 * format is to change. An example return value might be
	 * &quot;?date&quot;
	 * 
	 * @return the String representation
	 */
	public String toString() {
		return name;
	}

	public String getValue() {
		return name;
	}
}
