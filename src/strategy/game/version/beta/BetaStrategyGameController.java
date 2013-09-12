package strategy.game.version.beta;

import java.util.Collection;

import strategy.common.PlayerColor;
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
				
		this.redConfiguration = redConfiguration;
		this.blueConfiguration = blueConfiguration;
	}

	
	@Override
	public void startGame() throws StrategyException {
		if(!(checkPiecesOnSide(PlayerColor.RED,redConfiguration)&&checkPiecesOnSide(PlayerColor.BLUE,blueConfiguration))){
			throw new StrategyException("pieces not in valid positions");
		}
		if(!(checkNumberOfPieces(redConfiguration)==0 || checkNumberOfPieces(blueConfiguration)==0)){
			throw new StrategyException("incorrect number of pieces"+checkNumberOfPieces(redConfiguration)+"|"+checkNumberOfPieces(blueConfiguration));
		}
		

	}
	private int checkNumberOfPieces(Collection<PieceLocationDescriptor> config){
		int flag = 1;
		int marshal = 1;
		int colonels = 2;
		int captains = 2;
		int lieutanants = 3;
		int sergeant = 3;
		
		for(PieceLocationDescriptor pl: config){
			if(pl.getPiece().getType()==PieceType.FLAG){
				if((--flag)<0)return -1;
			}else if(pl.getPiece().getType()==PieceType.MARSHAL){
				if((--marshal)<0)return -2;
			}else if(pl.getPiece().getType()==PieceType.COLONEL){
				if((--colonels)<0)return -3;
			}else if(pl.getPiece().getType()==PieceType.CAPTAIN){
				if((--captains)<0)return -4;
			}else if(pl.getPiece().getType()==PieceType.LIEUTENANT){
				if((--lieutanants)<0)return -5;
			}else if(pl.getPiece().getType()==PieceType.SERGEANT){
				if((--sergeant)<0)return -6;
			}else{
				return -1;
			}
		}
		return (flag+marshal+colonels+captains+lieutanants+sergeant);//==0;
	}
	private boolean checkPiecesOnSide(PlayerColor color,Collection<PieceLocationDescriptor> config){
		int x;
		int y;
		if(config==null)return false;
		for(PieceLocationDescriptor pl: config){
			x = pl.getLocation().getCoordinate(Coordinate.X_COORDINATE);
			y = pl.getLocation().getCoordinate(Coordinate.Y_COORDINATE);//PlayerColor.BLUE
			if((x < 0) || (x > 5)) return false;
			if((PlayerColor.BLUE == color) && ((y < 4) || (y > 5))){
				return false;
			}
			if((PlayerColor.RED == color) && ((y < 0) || (y > 1))){
				return false;
			}
		}
		return true;
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
