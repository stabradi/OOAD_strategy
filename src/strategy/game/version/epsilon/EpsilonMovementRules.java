/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.version.epsilon;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.version.MovementRules;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.StrikeStrategy;

/**
 * Movement Rules implementation for an Epsilon Strategy Game
 * @author cpnota
 * @version September 24, 2013
 */
public class EpsilonMovementRules implements MovementRules {	
	MovementValidationStrategy movementValidationStrategy;
	StrikeStrategy strikeStrategy;
	
	/**
	 * Constructor for Gamma Movement Rules
	 * @param movementValidationStrategy
	 * @param strikeStrategy
	 */
	public EpsilonMovementRules(MovementValidationStrategy movementValidationStrategy, StrikeStrategy strikeStrategy){

		this.movementValidationStrategy = movementValidationStrategy;
		this.strikeStrategy=strikeStrategy;
	}

	public MoveResult move(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
					throws StrategyException{
		movementValidationStrategy.validateMove(controller, configuration, pl, from, to);

		// Check for a strike
		final PieceLocationDescriptor toPl = getPlDescriptorAtFromConfig(to, configuration);
		MoveResult moveResult;
		if((toPl != null) && (toPl.getPiece().getOwner() != pl.getPiece().getOwner())){
			moveResult = strikeStrategy.strikeMove(configuration, pl, toPl);
		}
		else{
			moveResult = normalMove(configuration, pl, to);
		}
		return moveResult;
	}

	/*
	 * Helper for move(), updates, the configuration for moves not involving strikes
	 */
	private MoveResult normalMove(Collection<PieceLocationDescriptor> configuration, PieceLocationDescriptor pl, Location to){		
		// Remove old PieceLocationDescriptor (for from) and add new one (for to)
		configuration.remove(pl);
		configuration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		return new MoveResult(MoveResultStatus.OK, null);
	}

	/**
	 * Gets PieceLocationDescriptor descriptor from only a given configuration at a given Location
	 * or null if there is none.
	 * @param location Location to look for an associated PieceLocationDescriptor
	 * @param config Configuration to search for with given Location
	 * @return A PieceLocationDescriptor with the given Location or null if there is none
	 */
	protected PieceLocationDescriptor getPlDescriptorAtFromConfig(Location location, Collection<PieceLocationDescriptor> config){
		Location plLoc = null;
		for(PieceLocationDescriptor pl: config){
			plLoc = pl.getLocation();
			if(plLoc.getCoordinate(Coordinate.X_COORDINATE) == location.getCoordinate(Coordinate.X_COORDINATE) && 
					plLoc.getCoordinate(Coordinate.Y_COORDINATE) == location.getCoordinate(Coordinate.Y_COORDINATE)){
				return pl;
			}
		}
		return null;
	}
}
