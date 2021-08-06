
package com.XRewrite.bean;


import java.util.Set;

public interface IRule
{

	public Set<ILiteral> getHead();
	

	public Set<ILiteral> getBody();


	public boolean isRectified();

}
