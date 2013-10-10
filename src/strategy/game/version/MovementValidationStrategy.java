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
import strategy.game.common.PieceLocationDescriptor;

/**
 * MovementValidationStrategy validates that a given move is valid based on specific rules
 * @author cpnota and stabradi
 * @version Sep 13, 2013
 */
public interface MovementValidationStrategy {
	/**
	 * Validates that a given move is valid
	 * @param controller StrategyGameController the move is being performed from
	 * @param configuration Board configuration
	 * @param pl associated with the piece doing the moving
	 * @param from The source location
	 * @param to The destination location
	 * @throws StrategyException if the move is not valid
	 */
	void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
	PieceLocationDescriptor pl, Location from, Location to) throws StrategyException;
}
