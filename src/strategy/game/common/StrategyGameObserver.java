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
import java.util.Collection;

import strategy.common.StrategyException;

/**
 * @author stabradi
 * @version 1
 */
public interface StrategyGameObserver {
	/**
	 * Method gameStart.
	 * @param redConfiguration Collection<PieceLocationDescriptor>
	 * @param blueConfiguration Collection<PieceLocationDescriptor>
	 */
	void gameStart(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration);
	/**
	 * Method moveHappened.
	 * @param piece PieceType
	 * @param from Location
	 * @param to Location
	 * @param moveResults MoveResult
	 * @param fault StrategyException
	 */
	void moveHappened(PieceType piece, Location from, Location to, 
			MoveResult moveResults, StrategyException fault);
}
