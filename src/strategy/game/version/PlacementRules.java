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
import strategy.game.common.PieceLocationDescriptor;

/**
 * PlacementRules is an interface that can verify the initial
 * placement of pieces for a Strategy Game.
 * @author cpnota
 * @version September 24, 2013
 */
public interface PlacementRules {
	/**
	 * Validates the initial placement of pieces for a strategy game
	 * @param redConfiguration the configuration of the red pieces
	 * @param blueConfiguration the configuration of the blue pieces
	 * @throws StrategyException if the configuration is invalid
	 */
	void validatePlacement(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration)
			throws StrategyException;
}
