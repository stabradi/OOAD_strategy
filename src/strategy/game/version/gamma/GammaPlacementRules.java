package strategy.game.version.gamma;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.common.Coordinate;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.PlacementRules;

public class GammaPlacementRules implements PlacementRules {

	@Override
	public void validatePlacement(Collection<PieceLocationDescriptor> redConfiguration, Collection<PieceLocationDescriptor> blueConfiguration) throws StrategyException {
		checkNumberOfPieces(redConfiguration,blueConfiguration);
		checkPiecesOnSide(redConfiguration,blueConfiguration);
	}

	/**
	 * @param config1 first set of pieces to check for correct pieces
	 * @param config2 second set of pieces to check for correct pieces
	 * @throws StrategyException if the types of any of the pieces are wrong
	 */
	protected void checkNumberOfPieces(Collection<PieceLocationDescriptor> config1, Collection<PieceLocationDescriptor> config2) throws StrategyException{
		if(!(checkNumberOfPieces(config1)==0 && checkNumberOfPieces(config2)==0)){
			throw new StrategyException("incorrect number of pieces"+checkNumberOfPieces(config1)+"|"+checkNumberOfPieces(config2));
		}
	}
	
	/**
	 * @param config the set of pieces to check for correct pieces
	 * @return number code indicating in what way the number of pieces is wrong,
	 *  or 0 if it is not wrong, this is nicer than a boolean for debugging
	 */
	protected int checkNumberOfPieces(Collection<PieceLocationDescriptor> config){
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
	
	/**
	 * @param config1 first set of pieces to check for correct placement
	 * @param config2 second set of pieces to check for correct placement
	 * @throws StrategyException if the placements are wrong
	 */
	protected void checkPiecesOnSide(Collection<PieceLocationDescriptor> config1, Collection<PieceLocationDescriptor> config2) throws StrategyException{
		if(!(checkPiecesOnSide(PlayerColor.RED,config1)&&checkPiecesOnSide(PlayerColor.BLUE,config2))){
			throw new StrategyException("pieces not in valid positions");
		}
	}
	
	/**
	 * @param color the color of the pieces being evaluated
	 * @param config second set of pieces to check for correct placement
	 * @return indication of there being any pieces not on their own side
	 */
	protected boolean checkPiecesOnSide(PlayerColor color,Collection<PieceLocationDescriptor> config){
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
			if(pl.getPiece().getOwner() != color){
				return false;
			}
		}
		return true;
	}
}
