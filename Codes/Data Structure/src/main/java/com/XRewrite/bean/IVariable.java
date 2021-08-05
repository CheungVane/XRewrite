
package com.XRewrite.bean;

/**
 * <p>
 * An interface for representing a variable term. A variable term is 
 * a term which represents a variable.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   14.11.2005 12:17:48
 */
public interface IVariable extends ITerm {
	/**
	 * Return the wrapped type.
	 */
	public String getValue();

}