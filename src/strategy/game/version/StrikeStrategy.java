/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.version;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.game.common.MoveResult;
import strategy.game.common.PieceLocationDescriptor;

/**
 * StrikeStrategy is an interface that defines the handling of striking
 * for particular versions of Strategy
 * @author Chris
 * @version 10/16/2013
 */
public interface StrikeStrategy {
	/**
	 * Attempt a strike with attacker on defender in the given configuration. Updates the configuration.
	 * @param configuration
	 * @param attacker
	 * @param defender
	 * @return MoveResult containing the outcome of the move
	 * @throws StrategyException
	 */
	MoveResult strikeMove(Collection<PieceLocationDescriptor> configuration, PieceLocationDescriptor attacker, PieceLocationDescriptor defender) throws StrategyException;
}
