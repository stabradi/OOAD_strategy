package strategy.game.version.beta;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.common.StrategyRuntimeException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;

public class BetaStrategyGameController implements StrategyGameController {
	private Collection<PieceLocationDescriptor> redConfiguration;
	private Collection<PieceLocationDescriptor> blueConfiguration;
	
	private PlayerColor currentTurn; // the player whose turn it currently is
	private boolean gameOver;
	private boolean gameStarted;

	public BetaStrategyGameController(Collection<PieceLocationDescriptor> redConfiguration, Collection<PieceLocationDescriptor> blueConfiguration) throws StrategyException{
		this.redConfiguration = redConfiguration;
		this.blueConfiguration = blueConfiguration;
		gameOver = false;
	}

	@Override
	public void startGame() throws StrategyException {
		currentTurn = PlayerColor.RED;
		gameOver = false;
		gameStarted = true;
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
		
		PieceLocationDescriptor pl = getPlDescriptorAt(from);
		
		// Check that move is valid
		if(pl == null){
			throw new StrategyException("Cannot move piece: There is no piece on that space!");
		}
		if(currentTurn != pl.getPiece().getOwner()){
			throw new StrategyException("Cannot move piece: It is not the piece owner's turn!");
		}
		if(!locationIsOnBoard(to)){
			throw new StrategyException("Cannot move piece off of board");
		}
		if(getPieceAt(to) != null){
			throw new StrategyException("Cannot move piece into another piece (strikes not yet implemented)");
		}
		if(from.distanceTo(to) != 1){
			throw new StrategyException("Must move piece exactly one space orthogonally");
		}
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
		
		// Select appropriate player's configuration
		Collection<PieceLocationDescriptor> playerConfiguration;
		if(currentTurn == PlayerColor.BLUE){
			playerConfiguration = blueConfiguration;
		}
		else if(currentTurn == PlayerColor.RED){
			playerConfiguration = redConfiguration;
		}
		else{
			throw new StrategyRuntimeException("Could not make move because the current player turn could not be determined, or was an in an invalid state.");
		}
		
		// Remove old PieceLocationDescriptor (for from) and add new one (for to)
		playerConfiguration.remove(pl);
		playerConfiguration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		
		nextTurn();

		return new MoveResult(MoveResultStatus.OK, null);
	}
	
	@Override
	public Piece getPieceAt(Location location) {
		PieceLocationDescriptor pl = getPlDescriptorAt(location);
		if(pl == null) return null;
		else return pl.getPiece();
	}
	
	/*
	 * This method switches the turn from red to blue or blue to red
	 * Returns the new turn
	 */
	private PlayerColor nextTurn(){
		if(currentTurn == PlayerColor.BLUE){
			currentTurn = PlayerColor.RED;
		}
		else if(currentTurn == PlayerColor.RED){
			currentTurn = PlayerColor.BLUE;
		}
		else{
			throw new StrategyRuntimeException("Could not switch player turns. Unexpected current turn. Game may have not have been started, or game has finished.");
		}
		return currentTurn;
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
	
	/*
	 * Helper for getPlDescriptorAt. Gets pl descriptor from only a given configuration,
	 * or null if there is none.
	 */
	private PieceLocationDescriptor getPlDescriptorAtFromConfig(Location location, Collection<PieceLocationDescriptor> config){
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
	 * TODO VERIFY THIS
	 */
	private boolean locationIsOnBoard(Location to){
		int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		return ((xcoord >= 0) && (ycoord >= 0) && (xcoord <= 5) && (ycoord <= 5));
	}
}
