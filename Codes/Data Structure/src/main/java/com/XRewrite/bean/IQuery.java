
package com.XRewrite.bean;

import java.util.List;


/**
 * A query is a rule without the head.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   19.11.2005 15:35:44
 */

public interface IQuery {
    
	public List<ILiteral> getLiterals();
	
}
