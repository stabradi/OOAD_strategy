package strategy.game.version.common;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.version.MovementValidationStrategy;

public class FirstLieutenantMovementValidationStrategy implements
		MovementValidationStrategy {

	@Override
	public void validateMove(StrategyGameController controller,
			Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
			throws StrategyException {
		if(from.distanceTo(to) == 2){
			if((from.getCoordinate(Coordinate.X_COORDINATE) == to.getCoordinate(Coordinate.X_COORDINATE)) ||
					(from.getCoordinate(Coordinate.Y_COORDINATE) == to.getCoordinate(Coordinate.Y_COORDINATE))){
				Piece toPiece = controller.getPieceAt(to);
				if((toPiece == null) || (toPiece.getOwner() == pl.getPiece().getOwner())){
					throw new StrategyException("First Lieutenants can only move two spaces if they are striking!");
				}
			}
			else{
				throw new StrategyException("First Lieutenants can only move or strike in a straight line!");
			}
		} else if (from.distanceTo(to) != 1){
			throw new StrategyException("First Lieutenants can only move one space, or two spaces if they are striking!");
		}
	}
}
