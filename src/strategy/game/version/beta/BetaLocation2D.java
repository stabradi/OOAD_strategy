package strategy.game.version.beta;

import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;

public class BetaLocation2D extends Location2D {
	private final int xCoordinate;
	private final int yCoordinate;
	
	public BetaLocation2D(Location loc){
		super(loc.getCoordinate(Coordinate.X_COORDINATE), loc.getCoordinate(Coordinate.Y_COORDINATE));
		xCoordinate = loc.getCoordinate(Coordinate.X_COORDINATE);
		yCoordinate = loc.getCoordinate(Coordinate.Y_COORDINATE);
	}
	
	@Override
	public int distanceTo(Location otherLocation){
		final int xDistance = Math.abs(otherLocation.getCoordinate(Coordinate.X_COORDINATE) - xCoordinate);
		final int yDistance = Math.abs(otherLocation.getCoordinate(Coordinate.Y_COORDINATE) - yCoordinate);
		return xDistance + yDistance;
	}
}
