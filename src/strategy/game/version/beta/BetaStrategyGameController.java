/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.version.beta;

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

/**
 * The BetaStrategyGameController implements the game core for
 * the Beta Strategy version.
 * @author cpnota and stabradi
 * @version Sep 13, 2013
 */
public class BetaStrategyGameController implements StrategyGameController {
	final private Collection<PieceLocationDescriptor> redConfiguration;
	final private Collection<PieceLocationDescriptor> blueConfiguration;
	
	private PlayerColor currentTurn; // the player whose turn it currently is
	private boolean gameOver;
	private boolean gameStarted;

	/**
	 * @param redConfiguration Initial configuration of the red player's pieces
	 * @param blueConfiguration Initial configuration of the blue player's pieces
	 */
	public BetaStrategyGameController(Collection<PieceLocationDescriptor> redConfiguration, Collection<PieceLocationDescriptor> blueConfiguration){
		this.redConfiguration = redConfiguration;
		this.blueConfiguration = blueConfiguration;
		gameOver = false;
	}
	@Override
	public void startGame() throws StrategyException {
		if(!(checkPiecesOnSide(PlayerColor.RED,redConfiguration)&&checkPiecesOnSide(PlayerColor.BLUE,blueConfiguration))){
			throw new StrategyException("pieces not in valid positions");
		}
		if(!(checkNumberOfPieces(redConfiguration)==0 || checkNumberOfPieces(blueConfiguration)==0)){
			throw new StrategyException("incorrect number of pieces"+checkNumberOfPieces(redConfiguration)+"|"+checkNumberOfPieces(blueConfiguration));
		}
		currentTurn = PlayerColor.RED;
		gameOver = false;
		gameStarted = true;

	}
	private int checkNumberOfPieces(Collection<PieceLocationDescriptor> config){
		int flag = 1;
		int marshal = 1;
		int colonels = 2;
		int captains = 2;
		int lieutanants = 3;
		int sergeant = 3;
		
		for(PieceLocationDescriptor pl: config){
			if(pl.getPiece().getType()==PieceType.FLAG){
				if((--flag)<0)return -1;
			}else if(pl.getPiece().getType()==PieceType.MARSHAL){
				if((--marshal)<0)return -2;
			}else if(pl.getPiece().getType()==PieceType.COLONEL){
				if((--colonels)<0)return -3;
			}else if(pl.getPiece().getType()==PieceType.CAPTAIN){
				if((--captains)<0)return -4;
			}else if(pl.getPiece().getType()==PieceType.LIEUTENANT){
				if((--lieutanants)<0)return -5;
			}else if(pl.getPiece().getType()==PieceType.SERGEANT){
				if((--sergeant)<0)return -6;
			}else{
				return -1;
			}
		}
		return (flag+marshal+colonels+captains+lieutanants+sergeant);//==0;
	}
	private boolean checkPiecesOnSide(PlayerColor color,Collection<PieceLocationDescriptor> config){
		int x;
		int y;
		if(config==null)return false;
		for(PieceLocationDescriptor pl: config){
			x = pl.getLocation().getCoordinate(Coordinate.X_COORDINATE);
			y = pl.getLocation().getCoordinate(Coordinate.Y_COORDINATE);//PlayerColor.BLUE
			if((x < 0) || (x > 5)) return false;
			if((PlayerColor.BLUE == color) && ((y < 4) || (y > 5))){
				return false;
			}
			if((PlayerColor.RED == color) && ((y < 0) || (y > 1))){
				return false;
			}
		}
		return true;
	}

	// TODO Strikes
	@Override
	public MoveResult move(PieceType piece, Location from, Location to)
			throws StrategyException {
		if (gameOver) {
			throw new StrategyException("The game is over, you cannot make a move");
		}
		if (!gameStarted) {
			throw new StrategyException("You must start the game!");
		}
		
		final PieceLocationDescriptor fromPl = getPlDescriptorAt(from);
		validateMove(fromPl, from, to);
		
		// Check for a strike
		PieceLocationDescriptor toPl = getPlDescriptorAt(to);
		MoveResult moveResult;
		if((toPl != null) && (toPl.getPiece().getOwner() != currentTurn)){
			moveResult = strikeMove(fromPl, toPl);
		}
		else{
			moveResult = normalMove(fromPl, to);
		}
		nextTurn();
		return moveResult;
	}
	
	@Override
	public Piece getPieceAt(Location location) {
		final PieceLocationDescriptor pl = getPlDescriptorAt(location);
		if(pl == null) return null;
		else return pl.getPiece();
	}
	
	/*
	 * Helper for move(), updates the configurations for moves involving strikes
	 */
	private MoveResult strikeMove(PieceLocationDescriptor attacker, PieceLocationDescriptor defender){
		StrikeResultBeta result = combatResult(attacker.getPiece().getType(), defender.getPiece().getType());
		if(result == StrikeResultBeta.DRAW){
			if(attacker.getPiece().getOwner() == PlayerColor.RED){
				redConfiguration.remove(attacker);
				blueConfiguration.remove(defender);
			}
			else{
				redConfiguration.remove(defender);
				blueConfiguration.remove(attacker);
			}
			return new MoveResult(MoveResultStatus.OK, null); // TODO Is this correct BattleWinner info for draw?
		}
		else if(result == StrikeResultBeta.ATTACKER_WINS){
			Collection<PieceLocationDescriptor> loseConfig;
			if(attacker.getPiece().getOwner() == PlayerColor.RED){
				loseConfig = blueConfiguration;
			}
			else{
				loseConfig = redConfiguration;
			}
			loseConfig.remove(defender);
			normalMove(attacker, defender.getLocation());
			return new MoveResult(MoveResultStatus.OK, attacker);
			
		}
		else{ //if(result == StrikeResultBeta.ATTACKER_LOSES){
			Collection<PieceLocationDescriptor> loseConfig;
			if(attacker.getPiece().getOwner() == PlayerColor.RED){
				loseConfig = redConfiguration;
			}
			else{
				loseConfig = blueConfiguration;
			}
			loseConfig.remove(attacker);
			normalMove(defender, attacker.getLocation());
			return new MoveResult(MoveResultStatus.OK, defender);
		}
	}
	
	/*
	 * Helper for move(), updates, the configuration for moves not involving strikes
	 */
	private MoveResult normalMove(PieceLocationDescriptor pl, Location to){
		Collection<PieceLocationDescriptor> playerConfiguration;
		if(pl.getPiece().getOwner() == PlayerColor.BLUE){
			playerConfiguration = blueConfiguration;
		}
		else{
			playerConfiguration = redConfiguration;
		}
		
		// Remove old PieceLocationDescriptor (for from) and add new one (for to)
		playerConfiguration.remove(pl);
		playerConfiguration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		
		return new MoveResult(MoveResultStatus.OK, null);
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
	
	protected StrikeResultBeta combatResult(PieceType attacker, PieceType defender){
		return null;
	}
	
	/*
	 * This method determines whether or not a move is valid, and throws an exception if not
	 */
	private void validateMove(PieceLocationDescriptor pl, Location from, Location to) throws StrategyException{
		if(pl == null){
			throw new StrategyException("Cannot move piece: There is no piece on that space!");
		}
		if(currentTurn != pl.getPiece().getOwner()){
			throw new StrategyException("Cannot move piece: It is not the piece owner's turn!");
		}
		if(!locationIsOnBoard(to)){
			throw new StrategyException("Cannot move piece off of board");
		}
		if((getPieceAt(to) != null) && (getPieceAt(to).getOwner() == currentTurn)){
			throw new StrategyException("Cannot move piece into another piece belonging to the same player");
		}
		if(calculateDistance(from, to) != 1){
			throw new StrategyException("Must move piece exactly one space orthogonally");
		}
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
	}
	
	/*
	 * This method returns the piece on the game board that is associated with the
	 * specified location, or null if there is none
	 */
	private PieceLocationDescriptor getPlDescriptorAt(Location location){
		PieceLocationDescriptor pl;
		pl = getPlDescriptorAtFromConfig(location, redConfiguration);
		if(pl == null){
			pl = getPlDescriptorAtFromConfig(location, blueConfiguration);
		}
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
	
	/**
	 * Returns the Manhattan distance between two Locations with X and Y coordinates
	 * @param from The first Location
	 * @param to the second location
	 * @return the Manhattan distance between from and to
	 */
	protected int calculateDistance(Location from, Location to){
		final int xDistance = Math.abs(to.getCoordinate(Coordinate.X_COORDINATE) - from.getCoordinate(Coordinate.X_COORDINATE));
		final int yDistance = Math.abs(to.getCoordinate(Coordinate.Y_COORDINATE) - from.getCoordinate(Coordinate.Y_COORDINATE));
		return xDistance + yDistance;
	}

	/*
	 * Verifies that a position is in fact on the board
	 */
	private boolean locationIsOnBoard(Location to){
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		return ((xcoord >= 0) && (ycoord >= 0) && (xcoord <= 5) && (ycoord <= 5));
	}
}
