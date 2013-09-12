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

/**
 * The BetaStrategyGameController implements the game core for
 * the Beta Strategy version.
 * @author cpnota and stabradi
 * @version Sep 13, 2013
 */
public class BetaStrategyGameController implements StrategyGameController {
	final private Collection<PieceLocationDescriptor> redInitialConfiguration;
	final private Collection<PieceLocationDescriptor> blueInitialConfiguration;
	private Collection<PieceLocationDescriptor> currentConfiguration;
	
	private PlayerColor currentTurn; // the player whose turn it currently is
	private boolean gameOver;
	private boolean gameStarted;
	private int moveCounter;

	/**
	 * @param redConfiguration Initial configuration of the red player's pieces
	 * @param blueConfiguration Initial configuration of the blue player's pieces
	 */
	public BetaStrategyGameController(Collection<PieceLocationDescriptor> redConfiguration, Collection<PieceLocationDescriptor> blueConfiguration){
		redInitialConfiguration = redConfiguration;
		blueInitialConfiguration = blueConfiguration;
		currentConfiguration = null;
		gameOver = false;
	}
	@Override
	public void startGame() throws StrategyException {
		checkNumberOfPieces(redInitialConfiguration,blueInitialConfiguration);
		checkPiecesOnSide(redInitialConfiguration,blueInitialConfiguration);
		
		currentConfiguration = new ArrayList<PieceLocationDescriptor>();
		currentConfiguration.addAll(redInitialConfiguration);
		currentConfiguration.addAll(blueInitialConfiguration);
		
		currentTurn = PlayerColor.RED;
		gameOver = false;
		gameStarted = true;
		moveCounter = 0;

	}
	protected void checkNumberOfPieces(Collection<PieceLocationDescriptor> config1, Collection<PieceLocationDescriptor> config2) throws StrategyException{
		if(!(checkNumberOfPieces(config1)==0 || checkNumberOfPieces(config2)==0)){
			throw new StrategyException("incorrect number of pieces"+checkNumberOfPieces(config1)+"|"+checkNumberOfPieces(config2));
		}
	}
	protected int checkNumberOfPieces(Collection<PieceLocationDescriptor> config){
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
	
	protected void checkPiecesOnSide(Collection<PieceLocationDescriptor> config1, Collection<PieceLocationDescriptor> config2) throws StrategyException{
		if(!(checkPiecesOnSide(PlayerColor.RED,config1)&&checkPiecesOnSide(PlayerColor.BLUE,config2))){
			throw new StrategyException("pieces not in valid positions");
		}
	}
	
	protected boolean checkPiecesOnSide(PlayerColor color,Collection<PieceLocationDescriptor> config){
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
		
		final Location betaFrom = new BetaLocation2D(from);
		final Location betaTo = new BetaLocation2D(to);
		
		final PieceLocationDescriptor fromPl = getPlDescriptorAt(betaFrom);
		validateMove(fromPl, betaFrom, betaTo);
		
		// Check for a strike
		final PieceLocationDescriptor toPl = getPlDescriptorAt(betaTo);
		MoveResult moveResult;
		if((toPl != null) && (toPl.getPiece().getOwner() != currentTurn)){
			moveResult = strikeMove(fromPl, toPl);
		}
		else{
			moveResult = normalMove(fromPl, betaTo);
		}
		nextTurn();
		if((moveCounter >= 12) && moveResult.getStatus() == MoveResultStatus.OK){
			moveResult = new MoveResult(MoveResultStatus.DRAW, moveResult.getBattleWinner());
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
	 * Helper for move(), updates the configurations for moves involving strikes
	 */
	private MoveResult strikeMove(PieceLocationDescriptor attacker, PieceLocationDescriptor defender){
		final MoveResult result;
		final StrikeResultBeta strikeResult = combatResult(attacker.getPiece().getType(), defender.getPiece().getType());
		if(strikeResult == StrikeResultBeta.DRAW){
			currentConfiguration.remove(defender);
			currentConfiguration.remove(attacker);
			result = new MoveResult(MoveResultStatus.OK, null); // TODO Is this correct BattleWinner info for draw?
		}
		else if(strikeResult == StrikeResultBeta.ATTACKER_WINS){
			currentConfiguration.remove(defender);
			normalMove(attacker, defender.getLocation());
			if(defender.getPiece().getType() == PieceType.FLAG){
				if(attacker.getPiece().getOwner() == PlayerColor.BLUE){
					result = new MoveResult(MoveResultStatus.BLUE_WINS, attacker);
				}
				else{
					result = new MoveResult(MoveResultStatus.RED_WINS, attacker);
				}
			}
			else{
				result = new MoveResult(MoveResultStatus.OK, attacker);
			}
		}
		else{ // Attacker loses
			currentConfiguration.remove(attacker);
			normalMove(defender, attacker.getLocation());
			result = new MoveResult(MoveResultStatus.OK, defender);
		}
		return result;
	}
	
	/*
	 * Helper for move(), updates, the configuration for moves not involving strikes
	 */
	private MoveResult normalMove(PieceLocationDescriptor pl, Location to){		
		// Remove old PieceLocationDescriptor (for from) and add new one (for to)
		currentConfiguration.remove(pl);
		currentConfiguration.add(new PieceLocationDescriptor(pl.getPiece(), to));
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
		moveCounter++;
		return currentTurn;
	}
	
	protected StrikeResultBeta combatResult(PieceType attacker, PieceType defender){
		if(attacker!=PieceType.MINER && defender==PieceType.BOMB) return StrikeResultBeta.ATTACKER_LOSES;
		if(attacker==PieceType.SPY && defender==PieceType.MARSHAL) return StrikeResultBeta.ATTACKER_WINS;
		if(rank(attacker)==rank(defender)) return StrikeResultBeta.DRAW;
		if(rank(attacker)>rank(defender)) return StrikeResultBeta.ATTACKER_WINS;
		
		return StrikeResultBeta.ATTACKER_LOSES;
	}
	private int rank(PieceType piece){
		if(piece==PieceType.MARSHAL){
			return 12;
		}else if(piece==PieceType.GENERAL){
			return 11;
		}else if(piece==PieceType.COLONEL){
			return 10;
		}else if(piece==PieceType.MAJOR){
			return 9;
		}else if(piece==PieceType.CAPTAIN){
			return 8;
		}else if(piece==PieceType.LIEUTENANT){
			return 7;
		}else if(piece==PieceType.SERGEANT){
			return 6;
		}else if(piece==PieceType.MINER){
			return 5;
		}else if(piece==PieceType.SCOUT){
			return 4;
		}else if(piece==PieceType.SPY){
			return 3;
		}else if(piece==PieceType.BOMB){
			return 2;
		}else if(piece==PieceType.FLAG){
			return 1;
		} 
		return 0;
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
		if(from.distanceTo(to) != 1){
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

	/*
	 * Verifies that a position is in fact on the board
	 */
	private boolean locationIsOnBoard(Location to){
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		return ((xcoord >= 0) && (ycoord >= 0) && (xcoord <= 5) && (ycoord <= 5));
	}
}
