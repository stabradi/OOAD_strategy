/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.version.gamma;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.beta.BetaLocation2D;
import strategy.game.version.common.RepeatMovementValidationStrategy;


/**
 * Movement validation strategy for Gamma Strategy
 * @author Chris
 * @version 10/8/2013
 */
public class GammaMovementValidationStrategy implements
		MovementValidationStrategy {
	private final RepeatMovementValidationStrategy repeatMovementValidationStrategy;

	public GammaMovementValidationStrategy(){
		repeatMovementValidationStrategy = createRepeatMovementValidationStrategy();
	}
	
	@Override
	public void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
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
		if(from.distanceTo(to) != 1){
			throw new StrategyException("Must move piece exactly one space orthogonally");
		}
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		if(((xcoord == 2) || (xcoord == 3)) &&
			((ycoord == 2) || (ycoord == 3))){
				throw new StrategyException("Cannot move into lake!");
		}
		repeatMovementValidationStrategy.validateMove(controller, configuration, pl, from, to);
	}
	
	/*
	 * Verifies that a position is in fact on the board
	 */
	private boolean locationIsOnBoard(Location to){
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		return ((xcoord >= 0) && (ycoord >= 0) && (xcoord <= 5) && (ycoord <= 5));
	}
	
	/**
	 * Creates a RepeatMovementValidationStrategy
	 * @return a new RepeatMovementValidationStrategy
	 */
	protected RepeatMovementValidationStrategy createRepeatMovementValidationStrategy() {
		return new RepeatMovementValidationStrategy();
	}
}
