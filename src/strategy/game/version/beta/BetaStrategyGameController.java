package strategy.game.version.beta;

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
		PieceLocationDescriptor pl = getPlDescriptorAt(from);
		if(pl.getPiece().getOwner() == PlayerColor.BLUE){
			blueConfiguration.remove(pl);
			blueConfiguration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		}
		else{
			redConfiguration.remove(pl);
			redConfiguration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		}
		return new MoveResult(MoveResultStatus.OK, null);
	}
	
	@Override
	public Piece getPieceAt(Location location) {
		PieceLocationDescriptor pl = getPlDescriptorAt(location);
		if(pl == null) return null;
		else return pl.getPiece();
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

}
