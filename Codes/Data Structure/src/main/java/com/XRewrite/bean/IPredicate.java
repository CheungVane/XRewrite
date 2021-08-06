
package com.XRewrite.bean;


public interface IPredicate extends Comparable<IPredicate> {

	public String getPredicateSymbol();

	public int getArity();
}
