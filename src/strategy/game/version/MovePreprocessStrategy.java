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

import strategy.common.PlayerColor;
import strategy.game.StrategyGameController;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;

/**
 * Interface for preprocessing a move to see if a MoveResult can be immediately determined
 * @author Chris
 * @version 10/15/2013
 */
public interface MovePreprocessStrategy {
	/**
	 * Preprocess a move to see if a MoveResult can be immediately determined
	 * @param controller
	 * @param configuration
	 * @param turn
	 * @param piece
	 * @param from
	 * @param to
	 * @return MoveResult if it can be determined, or null if not.
	 */
	MoveResult preprocessMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration, PlayerColor turn, 
			PieceType piece, Location from, Location to);
}
