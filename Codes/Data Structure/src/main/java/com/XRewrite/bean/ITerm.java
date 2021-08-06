
package com.XRewrite.bean;


public interface ITerm extends Comparable<ITerm>{

	public boolean isGround();
	

	public Object getValue();

}
