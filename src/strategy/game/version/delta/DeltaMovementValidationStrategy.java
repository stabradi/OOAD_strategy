package strategy.game.version.delta;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.beta.BetaLocation2D;

public class DeltaMovementValidationStrategy implements
		MovementValidationStrategy {
	private Location redMostRecentFrom;
	private Location blueMostRecentFrom;
	private Location redMostRecentTo;
	private Location blueMostRecentTo;
	private int redRepeatCount;
	private int blueRepeatCount;

	public DeltaMovementValidationStrategy(){
		final Location redMostRecentLocation = null;
		final Location blueMostRecentLocation = null;
		final Location redMostRecentTo = null;
		final Location blueMostRecentTo = null;
		redRepeatCount = 0;
		blueRepeatCount = 0;
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

		if(pl.getPiece().getType() != PieceType.BOMB){
			throw new StrategyException("Can't move bombs!");
		}
		else if(pl.getPiece().getType() != PieceType.SCOUT){
			this.validateScoutMovement(controller, configuration, pl, from, to);
		}
		else{
			if(from.distanceTo(to) != 1){
				throw new StrategyException("Must move piece exactly one space orthogonally");
			}

		}
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		if(((xcoord == 2) || (xcoord == 3)) &&
			((ycoord == 4) || (ycoord == 5))){
				throw new StrategyException("Cannot move into lake!");
		}
		if(((xcoord == 6) || (xcoord == 7)) &&
			((ycoord == 4) || (ycoord == 5))){
				throw new StrategyException("Cannot move into lake!");
		}
		validateAndUpdateRepeats(pl, from, to);
	}
	
	void validateScoutMovement(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to) throws StrategyException{
		int fromx = from.getCoordinate(Coordinate.X_COORDINATE);
		int fromy = from.getCoordinate(Coordinate.Y_COORDINATE);
		int tox = to.getCoordinate(Coordinate.X_COORDINATE);
		int toy = to.getCoordinate(Coordinate.Y_COORDINATE);
		
		int sharedLineNumber; // Value of the x or y coordinate
		int fromvar; // Value of the from coordinate that the scout is moving along
		int tovar; // Value of the from coordinate that the scout is moving along
		boolean horizontal; // 0 if vertical
		
		if(fromx == tox){ // move vertically
			horizontal = false;
			sharedLineNumber = fromx;
			fromvar = fromy;
			tovar = toy;
		}
		else if(fromy != toy){ // move horizontally
			horizontal = true;
			sharedLineNumber = fromy;
			fromvar = fromx;
			tovar = tox;
		}
		else{
			throw new StrategyException("Scout can only move in straight lines!");
		}
		
		// Determine whether the scout is moving in the positive or negative direction
		int sign;
		if(fromvar < tovar){
			sign = 1;
		}
		else if(fromvar > tovar){
			sign = -1;
		}
		else{
			throw new StrategyException("Scout cannot move 0 spaces!");
		}
		// Move along each space and make sure it is not occupied
		for(int i = fromvar + sign; (tovar-i)*sign != 0; i+=sign){ // increment in correct direction until distance between two locations is zero, then break
			Location2D checkLocation;
			if(horizontal){
				checkLocation = new Location2D(i, sharedLineNumber);
			}
			else{
				checkLocation = new Location2D(sharedLineNumber, i);
			}
			if(controller.getPieceAt(checkLocation) != null){
				throw new StrategyException("Scout cannot move through pieces!");
			}
		}
	}
	
	private void validateAndUpdateRepeats(PieceLocationDescriptor pl, Location from, Location to) throws StrategyException{
		Location mostRecentFrom;
		Location mostRecentTo;
		int repeatCount;
		if(pl.getPiece().getOwner() == PlayerColor.RED){
			mostRecentFrom = redMostRecentFrom;
			mostRecentTo = redMostRecentTo;
			repeatCount = redRepeatCount;
		}
		else{ // blue
			mostRecentFrom = blueMostRecentFrom;
			mostRecentTo = blueMostRecentTo;
			repeatCount = blueRepeatCount;
		}
		
		if(to.equals(mostRecentFrom) && from.equals(mostRecentTo)){ // opposite of current move
			if(repeatCount >= 1){ // Already moved back once, now repeating the movement, which is illegal
				throw new StrategyException("Error: Move repetition rule violation");
			}
			else{
				repeatCount++;
			}
		}
		else{ // No repetition yet
			repeatCount = 0;
		}
		// put back stuff
		if(pl.getPiece().getOwner() == PlayerColor.RED){
			redRepeatCount = repeatCount;
			redMostRecentFrom = new BetaLocation2D(from);
			redMostRecentTo = new BetaLocation2D(to);
		}
		else{ // blue
			blueRepeatCount = repeatCount;
			blueMostRecentFrom = new BetaLocation2D(from);
			blueMostRecentTo = new BetaLocation2D(to);
		}
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
