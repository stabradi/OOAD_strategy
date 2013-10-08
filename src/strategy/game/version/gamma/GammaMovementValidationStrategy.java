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

public class GammaMovementValidationStrategy implements
		MovementValidationStrategy {
	private Location redMostRecentFrom;
	private Location blueMostRecentFrom;
	private Location redMostRecentTo;
	private Location blueMostRecentTo;
	private int redRepeatCount;
	private int blueRepeatCount;

	public GammaMovementValidationStrategy(){
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
		validateAndUpdateRepeats(pl, from, to);
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
