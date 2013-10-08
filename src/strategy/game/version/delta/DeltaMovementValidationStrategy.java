package strategy.game.version.delta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import strategy.common.StrategyException;
import strategy.common.StrategyRuntimeException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.beta.BetaLocation2D;
import strategy.game.version.common.RepeatMovementValidationStrategy;
import strategy.game.version.common.ScoutMovementValidationStrategy;

public class DeltaMovementValidationStrategy implements
		MovementValidationStrategy {
	private List<PieceLocationDescriptor> lakes;
	private RepeatMovementValidationStrategy repeatMovementValidationStrategy;

	public DeltaMovementValidationStrategy(){
		List<Location> lakestmp = new ArrayList<Location>();
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
		else{
			throw new StrategyRuntimeException("Controller cannot be null!");
		}
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
		if(pl.getPiece().getType() == PieceType.BOMB){
			throw new StrategyException("Can't move bombs!");
		}
		if(pl.getPiece().getType() == PieceType.SCOUT){
			configuration.addAll(lakes);
			createScoutMovementValidationStrategy().validateMove(controller, configuration, pl, from, to);
			configuration.removeAll(lakes);
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
	
	/*
	 * Verifies that a position is in fact on the board
	 */
	private boolean locationIsOnBoard(Location to){
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		return ((xcoord >= 0) && (ycoord >= 0) && (xcoord <= 9) && (ycoord <= 9));
	}
	
	/**
	 * Creates a ScoutMovementValidationStrategy
	 * @return a new ScoutMovementValidationStrategy
	 */
	protected ScoutMovementValidationStrategy createScoutMovementValidationStrategy(){
		return new ScoutMovementValidationStrategy();
	}
	
	/**
	 * Creates a RepeatMovementValidationStrategy
	 * @return a new RepeatMovementValidationStrategy
	 */
	protected RepeatMovementValidationStrategy createRepeatMovementValidationStrategy() {
		return new RepeatMovementValidationStrategy();
	}

}
