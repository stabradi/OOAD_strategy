/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.version.common;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.common.StrategyRuntimeException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.version.MovementValidationStrategy;

/**
 * perform basic validation on movement.
 * Prevents from moving off of a 10x10 board, 
 * and moving into a piece belonging to the same player, and move attempts with null controllers
 * @author cpnota
 * @version 10/15/2013
 */
public class CommonMovementValidationStrategy implements
		MovementValidationStrategy {

	@Override
	public void validateMove(StrategyGameController controller,
			Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
			throws StrategyException {
		if(!locationIsOnBoard(to)){
			throw new StrategyException("Cannot move piece off of board");
		}
		if((controller != null)){
			final Piece toPiece = controller.getPieceAt(to);
			if(controller.getPieceAt(to) != null){
				if(toPiece.getOwner() == pl.getPiece().getOwner()){
					throw new StrategyException("Cannot move piece into another piece belonging to the same player");
				}
			}
		}
		else{
			throw new StrategyRuntimeException("Controller cannot be null!");
		}
	}
	
	/*
	 * Verifies that a position is in fact on the board
	 */
	private boolean locationIsOnBoard(Location to){
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		return ((xcoord >= 0) && (ycoord >= 0) && (xcoord <= 9) && (ycoord <= 9));
	}
}
