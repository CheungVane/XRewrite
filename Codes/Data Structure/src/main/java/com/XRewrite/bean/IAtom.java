
package com.XRewrite.bean;

public interface IAtom extends Comparable<IAtom> {


	public IPredicate getPredicate();


	public ITuple getTuple();


	public boolean isGround();


	public boolean isBuiltin();
}
