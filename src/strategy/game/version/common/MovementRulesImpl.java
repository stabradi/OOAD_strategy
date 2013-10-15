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

import java.util.ArrayList;
import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementRules;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.StrikeStrategy;
import strategy.game.version.gamma.GammaStrikeStrategy;

/**
 * Movement Rules implementation for a Gamma Strategy Game
 * @author cpnota
 * @version September 24, 2013
 */
public class MovementRulesImpl implements MovementRules {	
	MovementValidationStrategy movementValidationStrategy;
	StrikeStrategy strikeStrategy;
	
	/**
	 * Constructor for Gamma Movement Rules
	 * @param movementValidationStrategy
	 * @param strikeStrategy
	 */
	public MovementRulesImpl(MovementValidationStrategy movementValidationStrategy, StrikeStrategy strikeStrategy){

		this.movementValidationStrategy = movementValidationStrategy;
		this.strikeStrategy=strikeStrategy;
	}
	
	/**
	 * Constructor for Gamma Movement Rules
	 * @param movementValidationStrategy
	 */
	public MovementRulesImpl(MovementValidationStrategy movementValidationStrategy){

		this.movementValidationStrategy = movementValidationStrategy;
		this.strikeStrategy = new GammaStrikeStrategy();
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
		
		final Collection<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		final Collection<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		for(PieceLocationDescriptor plSplitting: configuration){
			if(plSplitting.getPiece().getOwner() == PlayerColor.RED)red.add(plSplitting);
			if(plSplitting.getPiece().getOwner() == PlayerColor.BLUE)blue.add(plSplitting);
		}
		final boolean canRedNotMove = hasNoMovablePieces(red);
		final boolean canBlueNotMove = hasNoMovablePieces(blue);
		if(canRedNotMove&&canBlueNotMove){
			moveResult = new MoveResult(MoveResultStatus.DRAW, moveResult.getBattleWinner());
		}else if(canRedNotMove){
			moveResult = new MoveResult(MoveResultStatus.BLUE_WINS, moveResult.getBattleWinner());
		}else if(canBlueNotMove){
			moveResult = new MoveResult(MoveResultStatus.RED_WINS, moveResult.getBattleWinner());
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
	
	/**
	 * Determines whether or not a given configuration contains any moveable pieces.
	 * @param config The configuration to search
	 * @return a boolean of whether or not there are movable pieces
	 */
	public boolean hasNoMovablePieces(Collection<PieceLocationDescriptor> config){
		boolean movablePieces = false;
		for(PieceLocationDescriptor pl: config){
			movablePieces = movablePieces || (( pl.getPiece().getType() != PieceType.FLAG) && (pl.getPiece().getType() != PieceType.BOMB));
		}
		return !movablePieces;
	}
}
