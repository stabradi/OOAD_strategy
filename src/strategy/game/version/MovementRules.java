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
import strategy.game.StrategyGameController;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.PieceLocationDescriptor;

/**
 * The MovementRules is the interface which knows all of the rules
 * for a given type of Strategy Game. It must be capable of
 * updating a configuration for a given rule set, including
 * all rules for striking.
 * @author cpnota
 * @version September 24, 2013
 */
public interface MovementRules {
	
	/**
	 * Validates that a move is valid, and throws an exception if not
	 * @param controller Game controller that the movement is happening in
	 * @param configuration Board configuration to validate the move in
	 * @param pl The PieceLocationDescriptor associated with the piece doing the moving.
	 * @param from The source location
	 * @param to The destination location
	 * @return the result of the Move
	 * @throws StrategyException if the attempted move is invalid for any reason
	 */
	MoveResult move(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
			throws StrategyException;
}
