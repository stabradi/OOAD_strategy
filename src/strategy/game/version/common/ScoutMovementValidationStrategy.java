package strategy.game.version.common;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.common.StrategyRuntimeException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementValidationStrategy;

public class ScoutMovementValidationStrategy implements
		MovementValidationStrategy {
	@Override
	public void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to) throws StrategyException{
		int fromx = from.getCoordinate(Coordinate.X_COORDINATE);
		int fromy = from.getCoordinate(Coordinate.Y_COORDINATE);
		int tox = to.getCoordinate(Coordinate.X_COORDINATE);
		int toy = to.getCoordinate(Coordinate.Y_COORDINATE);
		
		int sharedLineNumber; // Value of the x or y coordinate
		int fromvar; // Value of the from coordinate that the scout is moving along
		int tovar; // Value of the from coordinate that the scout is moving along
		boolean horizontal; // 0 if vertical
		
		if(pl.getPiece().getType() != PieceType.SCOUT){
			throw new StrategyRuntimeException("ScoutMovementValidationStrategy cannot validate the movement of non-scouts!");
		}
		
		if(fromx == tox){ // move vertically
			horizontal = false;
			sharedLineNumber = fromx;
			fromvar = fromy;
			tovar = toy;
		}
		else if(fromy == toy){ // move horizontally
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
}
