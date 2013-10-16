/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game;

import java.util.ArrayList;
import java.util.Collection;

import strategy.common.StrategyException;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.StrategyGameObserver;
import strategy.game.version.MovePreprocessStrategy;
import strategy.game.version.MovementRules;
import strategy.game.version.PlacementRules;
import strategy.game.version.UniversalStrategyGameController;
import strategy.game.version.alpha.AlphaStrategyGameController;
import strategy.game.version.beta.BetaLocation2D;
import strategy.game.version.beta.BetaMovementRules;
import strategy.game.version.beta.BetaPlacementRules;
import strategy.game.version.common.MovementRulesImpl;
import strategy.game.version.common.ResignationPreprocessor;
import strategy.game.version.delta.DeltaMovementValidationStrategy;
import strategy.game.version.delta.DeltaPlacementRules;
import strategy.game.version.epsilon.EpsilonMovementRules;
import strategy.game.version.epsilon.EpsilonMovementValidationStrategy;
import strategy.game.version.epsilon.EpsilonPlacementRules;
import strategy.game.version.epsilon.EpsilonStrikeStrategy;
import strategy.game.version.gamma.GammaMovementValidationStrategy;
import strategy.game.version.gamma.GammaStrikeStrategy;

/**
 * <p>
 * Factory to produce various versions of the Strategy game. This is implemented
 * as a singleton.
 * </p><p>
 * NOTE: If an error occurs creating any game, that is not specified in the particular
 * factory method's documentation, the factory method should throw a 
 * StrategyRuntimeException.
 * </p>
 * 
 * @author gpollice
 * @version Sep 10, 2013
 */
public class StrategyGameFactory
{
	private final static StrategyGameFactory instance = new StrategyGameFactory();
	
	/**
	 * Default private constructor to ensure this is a singleton.
	 */
	private StrategyGameFactory()
	{
		// Intentionally left empty.
	}

	/**
	 * @return the instance
	 */
	public static StrategyGameFactory getInstance()
	{
		return instance;
	}
	
	/**
	 * Create an Alpha Strategy game.
	 * @return the created Alpha Strategy game
	 */
	public StrategyGameController makeAlphaStrategyGame()
	{
		return new AlphaStrategyGameController();
	}
	
	/**
	 * Create a new Beta Strategy game given the 
	 * @param redConfiguration the initial starting configuration for the RED pieces
	 * @param blueConfiguration the initial starting configuration for the BLUE pieces
	 * @return the Beta Strategy game instance with the initial configuration of pieces
	 * @throws StrategyException if either configuration is invalid (overlapping pieces, etc)
	 */
	public StrategyGameController makeBetaStrategyGame(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration)
		throws StrategyException
	{	
		return makeStrategyGame(redConfiguration, blueConfiguration, new BetaMovementRules(), new BetaPlacementRules(),null);
	}
	
	/**
	 * Create a new Gamma Strategy game given the 
	 * @param redConfiguration the initial starting configuration for the RED pieces
	 * @param blueConfiguration the initial starting configuration for the BLUE pieces
	 * @return the Beta Strategy game instance with the initial configuration of pieces
	 * @throws StrategyException if either configuration is invalid (overlapping pieces, etc)
	 */
	public StrategyGameController makeGammaStrategyGame(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration)
		throws StrategyException
	{	
		return makeStrategyGame(redConfiguration, blueConfiguration, new MovementRulesImpl(new GammaMovementValidationStrategy(), new GammaStrikeStrategy()), new BetaPlacementRules(),null);
	}
	
	/**
	 * Create a new Delta Strategy game given the 
	 * @param redConfiguration the initial starting configuration for the RED pieces
	 * @param blueConfiguration the initial starting configuration for the BLUE pieces
	 * @return the Delta Strategy game instance with the initial configuration of pieces
	 * @throws StrategyException if either configuration is invalid (overlapping pieces, etc)
	 */
	public StrategyGameController makeDeltaStrategyGame(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration) throws StrategyException
	{
		return makeStrategyGame(redConfiguration, blueConfiguration, new MovementRulesImpl(new DeltaMovementValidationStrategy(), new GammaStrikeStrategy()), new DeltaPlacementRules(),null);
	}
	
	/**
	 * Create a new Epsilong Strategy game given the 
	 * @param redConfiguration the initial starting configuration for the RED pieces
	 * @param blueConfiguration the initial starting configuration for the BLUE pieces
	 * @return the Delta Strategy game instance with the initial configuration of pieces
	 * @throws StrategyException if either configuration is invalid (overlapping pieces, etc)
	 */
	public StrategyGameController makeEpsilonStrategyGame(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration,
			Collection<StrategyGameObserver>observers) throws StrategyException
	{


	    return makeStrategyGame(redConfiguration, blueConfiguration, new EpsilonMovementRules(new EpsilonMovementValidationStrategy(), new EpsilonStrikeStrategy()), new EpsilonPlacementRules(), new ResignationPreprocessor(), observers);


	}
	
	private StrategyGameController makeStrategyGame(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration, MovementRules movementRules, PlacementRules placementRules, Collection<StrategyGameObserver>observers)
		throws StrategyException
	{	
		return makeStrategyGame(redConfiguration, blueConfiguration, movementRules, placementRules, null);
	}
	
	private StrategyGameController makeStrategyGame(
			Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration, MovementRules movementRules, PlacementRules placementRules, MovePreprocessStrategy movePreprocessor, Collection<StrategyGameObserver>observers)
		throws StrategyException
	{	
		if(redConfiguration == null || blueConfiguration == null) throw new StrategyException("Cannot create Beta Strategy with Null Configurations");
		final Collection<PieceLocationDescriptor> newRedConfiguration = new ArrayList<PieceLocationDescriptor>();
		final Collection<PieceLocationDescriptor> newBlueConfiguration = new ArrayList<PieceLocationDescriptor>();
		//converting the type of location so that we can use some new functionality
		for(PieceLocationDescriptor pl: redConfiguration){ 
			newRedConfiguration.add(new PieceLocationDescriptor(pl.getPiece(),new BetaLocation2D(pl.getLocation())));
		}
		for(PieceLocationDescriptor pl: blueConfiguration){ 
			newBlueConfiguration.add(new PieceLocationDescriptor(pl.getPiece(),new BetaLocation2D(pl.getLocation())));
		}

		UniversalStrategyGameController tmpController = new UniversalStrategyGameController(newRedConfiguration,newBlueConfiguration, new ArrayList<PieceLocationDescriptor>(), movementRules, placementRules, movePreprocessor);

		if(observers!=null){
			for(StrategyGameObserver obs: observers){
				tmpController.register(obs);
			}
		}
		return tmpController;
	}
}
