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

import java.util.ArrayList;
import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.common.StrategyGameObservable;
import strategy.game.common.StrategyGameObserver;
import strategy.game.version.beta.BetaLocation2D;

/**
 * The BetaStrategyGameController implements the game core for
 * the Beta Strategy version.
 * @author cpnota and stabradi
 * @version Oct 8, 2013
 */
public class UniversalStrategyGameController implements StrategyGameController, StrategyGameObservable {
	private final Collection<PieceLocationDescriptor> redInitialConfiguration;
	private final Collection<PieceLocationDescriptor> blueInitialConfiguration;
	private final Collection<PieceLocationDescriptor> boardInitialConfiguration;
	
	protected Collection<PieceLocationDescriptor> currentConfiguration;
	
	protected PlayerColor currentTurn; // the player whose turn it currently is
	protected boolean gameOver;
	protected boolean gameStarted;
	
	private final MovementRules movementRules;
	private final MovePreprocessStrategy movePreprocessor;
	
	private ArrayList<StrategyGameObserver> observers = new ArrayList<StrategyGameObserver>();
	
	public UniversalStrategyGameController(){
		redInitialConfiguration = null;
		blueInitialConfiguration = null;
		boardInitialConfiguration = null;
		movementRules = null;
		movePreprocessor = null;
	}

	/**
	 * @param redConfiguration Configuration of red pieces
	 * @param blueConfiguration Configuration of blue pieces
	 * @param boardConfiguration Configuration of other board pieces (ie, if lakes were implemented as pieces)
	 * @param movementRules Rules that govern the movement of the pieces
	 * @param placementRules Rules that govern the placement of the pieces
	 * @throws StrategyException if the Initial configuration is invalid
	 */
	public UniversalStrategyGameController(Collection<PieceLocationDescriptor> redConfiguration, 
			Collection<PieceLocationDescriptor> blueConfiguration, 
			Collection<PieceLocationDescriptor> boardConfiguration, 
			MovementRules movementRules, 
			PlacementRules placementRules) 
					throws StrategyException{
		this(redConfiguration, blueConfiguration, boardConfiguration, movementRules, placementRules, null);

	}
	
	/**
	 * @param redConfiguration Configuration of red pieces
	 * @param blueConfiguration Configuration of blue pieces
	 * @param boardConfiguration Configuration of other board pieces (ie, if lakes were implemented as pieces)
	 * @param movementRules Rules that govern the movement of the pieces
	 * @param placementRules Rules that govern the placement of the pieces
	 * @param movePreprocessor Preprocessor to be run first on any incoming moves
	 * @throws StrategyException if the Initial configuration is invalid
	 */
	public UniversalStrategyGameController(Collection<PieceLocationDescriptor> redConfiguration, 
			Collection<PieceLocationDescriptor> blueConfiguration, 
			Collection<PieceLocationDescriptor> boardConfiguration, 
			MovementRules movementRules, 
			PlacementRules placementRules, MovePreprocessStrategy movePreprocessor) 
					throws StrategyException{
		redInitialConfiguration = redConfiguration;
		blueInitialConfiguration = blueConfiguration;
		boardInitialConfiguration = boardConfiguration;
		currentConfiguration = null;
		this.movementRules = movementRules;
		this.movePreprocessor = movePreprocessor;
		gameOver = false;
		gameStarted = false;		
		placementRules.validatePlacement(redInitialConfiguration,blueInitialConfiguration);
	}
	
	@Override
	public void startGame() throws StrategyException{
		if(gameOver||gameStarted)throw new StrategyException("game over or started");
		currentConfiguration = new ArrayList<PieceLocationDescriptor>();
		currentConfiguration.addAll(redInitialConfiguration);
		currentConfiguration.addAll(blueInitialConfiguration);
		currentConfiguration.addAll(boardInitialConfiguration);
		
		for(StrategyGameObserver obs: observers){
			obs.gameStart(redInitialConfiguration, blueInitialConfiguration);
		}
		
		currentTurn = PlayerColor.RED;
		gameOver = false;
		gameStarted = true;
	}
	
	@Override
	public MoveResult move(PieceType piece, Location from, Location to)
			throws StrategyException {

		StrategyException exception = null;

		MoveResult moveResult = null;
		if(movePreprocessor != null){
			moveResult = movePreprocessor.preprocessMove(this, currentConfiguration, currentTurn, piece, from, to);
			if(moveResult != null){
				return moveResult;
			}
		}
		

		if (gameOver) {
			throw new StrategyException("The game is over, you cannot make a move");
		}
		if (!gameStarted) {
			throw new StrategyException("You must start the game!");
		}
		
		final Location betaFrom = new BetaLocation2D(from);
		final Location betaTo = new BetaLocation2D(to);
		final PieceLocationDescriptor fromPl = getPlDescriptorAt(betaFrom);
		
		if(fromPl == null){
			exception = new StrategyException("Cannot move piece: There is no piece on that space!");
		}
		if(currentTurn != fromPl.getPiece().getOwner()){
			exception = new StrategyException("Cannot move piece: It is not the piece owner's turn!");
		}
		if(piece != fromPl.getPiece().getType()){
			exception = new StrategyException("Cannot move piece: That piece is not at that location!");
		}
		moveResult = movementRules.move(this, currentConfiguration, fromPl, betaFrom, betaTo);
		if((moveResult.getStatus() == MoveResultStatus.RED_WINS) || (moveResult.getStatus() == MoveResultStatus.BLUE_WINS) || (moveResult.getStatus() == MoveResultStatus.DRAW)){
			gameOver = true;
		}
		else{
			nextTurn();
		}
		for(StrategyGameObserver obs: observers){
			obs.moveHappened(piece, betaFrom, betaTo, moveResult, exception);
		}
		if(exception != null){
			throw exception;
		}
		return moveResult;
	}
	
	@Override
	public Piece getPieceAt(Location location) {
		if(!gameStarted) return null;
		final PieceLocationDescriptor pl = getPlDescriptorAt(location);
		if(pl == null) return null;
		else return pl.getPiece();
	}
	
	/*
	 * This method switches the turn from red to blue or blue to red
	 * Returns the new turn
	 */
	private PlayerColor nextTurn(){
		if(currentTurn == PlayerColor.RED){
			currentTurn = PlayerColor.BLUE;
		}
		else{
			currentTurn = PlayerColor.RED;
		}
		return currentTurn;
	}
	
	/*
	 * This method returns the piece on the game board that is associated with the
	 * specified location, or null if there is none
	 */
	private PieceLocationDescriptor getPlDescriptorAt(Location location){
		final PieceLocationDescriptor pl;
		pl = getPlDescriptorAtFromConfig(location, currentConfiguration);
		return pl;
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

	@Override
	public void register(StrategyGameObserver observer) {
		observers.add(observer);
		
	}

	@Override
	public void unregister(StrategyGameObserver observer) {
		observers.remove(observer);
		
	}
}
