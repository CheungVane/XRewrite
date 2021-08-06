
package com.XRewrite.bean.impl;


import com.XRewrite.bean.ITerm;
import com.XRewrite.bean.IVariable;

import java.io.Serializable;

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

	public String toString() {
		return name;
	}

	public String getValue() {
		return name;
	}
}
