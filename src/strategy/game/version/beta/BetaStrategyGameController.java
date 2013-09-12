package strategy.game.version.beta;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;

public class BetaStrategyGameController implements StrategyGameController {
	private Collection<PieceLocationDescriptor> redConfiguration;
	private Collection<PieceLocationDescriptor> blueConfiguration;

	public BetaStrategyGameController(Collection<PieceLocationDescriptor> redConfiguration, Collection<PieceLocationDescriptor> blueConfiguration) throws StrategyException{
		//checkValidInitialConfiguration(redConfiguration, blueConfiguration);
		this.redConfiguration = redConfiguration;
		this.blueConfiguration = blueConfiguration;
	}
	
	/*
	 * Puts pieces in a given config into a table, makes sure there are no overlaps, and returns the table
	 * Also checks that all pieces are placeable within the table
	 */
	/*
	private void checkValidInitialConfiguration(Collection<PieceLocationDescriptor> redConfiguration, Collection<PieceLocationDescriptor> blueConfiguration) throws StrategyException{
		Location loc = null;
		for(PieceLocationDescriptor pl: redConfiguration){
			try{
				loc = pl.getLocation();
				int xcoord = loc.getCoordinate(Coordinate.X_COORDINATE);
				int ycoord = loc.getCoordinate(Coordinate.Y_COORDINATE);
				if(pieceTable[xcoord][ycoord] == null){
					pieceTable[xcoord][ycoord] = pl.getPiece();
				}
				else{
					throw new StrategyException("Overlapping pieces.");
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				throw new StrategyException("Invalid piece location.");
			}
		}
		
		return pieceTable;
	}
	*/
	
	@Override
	public void startGame() throws StrategyException {
		// TODO Auto-generated method stub

	}

	@Override
	public MoveResult move(PieceType piece, Location from, Location to)
			throws StrategyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Piece getPieceAt(Location location) {
		// TODO Auto-generated method stub
		Location plLoc = null;
		for(PieceLocationDescriptor pl: redConfiguration){
			plLoc = pl.getLocation();
			if(plLoc.getCoordinate(Coordinate.X_COORDINATE) == location.getCoordinate(Coordinate.X_COORDINATE) && 
					plLoc.getCoordinate(Coordinate.Y_COORDINATE) == location.getCoordinate(Coordinate.Y_COORDINATE)){
				return pl.getPiece();
			}
		}
		for(PieceLocationDescriptor pl: blueConfiguration){
			plLoc = pl.getLocation();
			if(plLoc.getCoordinate(Coordinate.X_COORDINATE) == location.getCoordinate(Coordinate.X_COORDINATE) && 
					plLoc.getCoordinate(Coordinate.Y_COORDINATE) == location.getCoordinate(Coordinate.Y_COORDINATE)){
				return pl.getPiece();
			}
		}
		return null;
	}

}
