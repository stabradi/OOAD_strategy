package strategy.game.version.common;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.beta.BetaLocation2D;

public class RepeatMovementValidationStrategy implements
		MovementValidationStrategy {
	private Location redMostRecentFrom;
	private Location blueMostRecentFrom;
	private Location redMostRecentTo;
	private Location blueMostRecentTo;
	private int redRepeatCount;
	private int blueRepeatCount;
	
	public RepeatMovementValidationStrategy(){
		redRepeatCount = 0;
		blueRepeatCount = 0;
	}

	@Override
	public void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to) throws StrategyException{
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
}
