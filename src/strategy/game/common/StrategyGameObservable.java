/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package strategy.game.common;


/**
 * @author sam
 * @version 1
 */
public interface StrategyGameObservable{
	// Registers an observer
	/**
	 * Method register.
	 * @param observer StrategyGameObserver
	 */
	void register(StrategyGameObserver observer);
	// Removes an observer
	/**
	 * Method unregister.
	 * @param observer StrategyGameObserver
	 */
	void unregister(StrategyGameObserver observer);
}
