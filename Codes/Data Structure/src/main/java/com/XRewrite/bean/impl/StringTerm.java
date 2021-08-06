
package com.XRewrite.bean.impl;

import com.XRewrite.bean.IStringTerm;
import com.XRewrite.bean.ITerm;

import java.net.URI;

public class StringTerm implements IStringTerm {

	private String value = "";

	public StringTerm(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null || !(o instanceof IStringTerm)) {
			return 1;
		}

		IStringTerm st = (IStringTerm) o;
		return value.compareTo(st.getValue());
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof IStringTerm)) {
			return false;
		}
		IStringTerm st = (IStringTerm) o;
		return value.equals(st.getValue());
	}

	/**
	 * Simple toString() method wich only returns the holded value surrounded by
	 * &quot;'&quot;.
	 * 
	 * @return the containing String
	 */
	public String toString() {
		return "'" + value + "'";
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#string");
	}

	public String toCanonicalString() {
		return new String(value);
	}
}
