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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.beta.BetaLocation2D;
import strategy.game.version.common.CommonMovementValidationStrategy;
import strategy.game.version.common.FirstLieutenantMovementValidationStrategy;
import strategy.game.version.common.RepeatMovementValidationStrategy;
import strategy.game.version.common.ScoutMovementValidationStrategy;

/**
 * MovementValidationStrategy for Epsilon Strategy (see developer's guide)
 * @author Chris
 * @version 10/8/2013
 */
public class EpsilonMovementValidationStrategy implements
		MovementValidationStrategy {
	private final List<PieceLocationDescriptor> lakes;
	private final RepeatMovementValidationStrategy repeatMovementValidationStrategy;
	private final MovementValidationStrategy commonMovementValidationStrategy;

	public EpsilonMovementValidationStrategy(){
		final List<Location> lakestmp = new ArrayList<Location>();
		lakes = new ArrayList<PieceLocationDescriptor>();
		lakestmp.add(new BetaLocation2D(new Location2D(2,4)));
		lakestmp.add(new BetaLocation2D(new Location2D(3,4)));
		lakestmp.add(new BetaLocation2D(new Location2D(6,4)));
		lakestmp.add(new BetaLocation2D(new Location2D(7,4)));
		lakestmp.add(new BetaLocation2D(new Location2D(2,5)));
		lakestmp.add(new BetaLocation2D(new Location2D(3,5)));
		lakestmp.add(new BetaLocation2D(new Location2D(6,5)));
		lakestmp.add(new BetaLocation2D(new Location2D(7,5)));
		for(Location lake: lakestmp){
			lakes.add(new PieceLocationDescriptor(new Piece(PieceType.CHOKE_POINT, null), lake));
		}
		repeatMovementValidationStrategy = createRepeatMovementValidationStrategy();
		commonMovementValidationStrategy = new CommonMovementValidationStrategy();
	}
	
	@Override
	public void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
					throws StrategyException {
		if(!configuration.containsAll(lakes)){
			configuration.addAll(lakes);
		}
		commonMovementValidationStrategy.validateMove(controller, configuration, pl, from, to);
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
		else if(pl.getPiece().getType() == PieceType.BOMB){
			throw new StrategyException("Can't move bombs!");
		}
		else if(pl.getPiece().getType() == PieceType.SCOUT){
			createScoutMovementValidationStrategy().validateMove(controller, configuration, pl, from, to);
		}
		else if(pl.getPiece().getType()== PieceType.FIRST_LIEUTENANT){
			createFirstLieutenantMovementValidationStrategy().validateMove(controller, configuration, pl, from, to);
		}
		else{
			if(from.distanceTo(to) != 1){
				throw new StrategyException("Must move piece exactly one space orthogonally");
			}
		}
		for(PieceLocationDescriptor lake: lakes){
			if(to.equals(lake.getLocation())){
				throw new StrategyException("Cannot move into choke point!");
			}
		}
		repeatMovementValidationStrategy.validateMove(controller, configuration, pl, from, to);
	}
	
	/**
	 * Creates a ScoutMovementValidationStrategy
	 * @return a new ScoutMovementValidationStrategy
	 */
	protected ScoutMovementValidationStrategy createScoutMovementValidationStrategy(){
		return new ScoutMovementValidationStrategy();
	}
	
	/**
	 * Creates a FirstLieutenantMovementValidationStrategy
	 * @return a new FirstLieutenantMovementValidationStrategy
	 */
	protected FirstLieutenantMovementValidationStrategy createFirstLieutenantMovementValidationStrategy(){
		return new FirstLieutenantMovementValidationStrategy();
	}
	
	/**
	 * Creates a RepeatMovementValidationStrategy
	 * @return a new RepeatMovementValidationStrategy
	 */
	protected RepeatMovementValidationStrategy createRepeatMovementValidationStrategy() {
		return new RepeatMovementValidationStrategy();
	}

}
