
package com.XRewrite.bean;

/**
 * <p>
 * A literal (a subgoal) is either an atomic formula or a negated atomic
 * formula:
 * </p>
 * <p>
 * p(A1,...An) or not p(A1,...An)
 * </p>
 * <p>
 * This interface is used to promote modularity of the inference engine.
 * </p>
 * <p>
 * $Id: ILiteral.java,v 1.6 2007-10-30 10:35:40 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.11.2005 17:20:27
 */
public interface ILiteral extends Comparable<ILiteral> {
	/**
	 * <p>
	 * Checks whether the literal is a positive atomic formula or a negated
	 * atomic formula.
	 * </p>
	 * 
	 * @return True if the literal is a positive atomic formula; false
	 *         otherwise.
	 */
	public boolean isPositive();

	/**
	 * <p>
	 * Returns the atom of this literal.
	 * <p/>
	 * 
	 * @return The atom.
	 */
	public IAtom getAtom();
}
