package strategy.game.version.epsilon;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameFactory;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.beta.BetaLocation2D;

public class EpsilonStrategyMainTest {
	
	public List<PieceLocationDescriptor> createPlayerInitialConfiguration(PlayerColor player, Location start){
		int startingX = start.getCoordinate(Coordinate.X_COORDINATE);
		int startingY = start.getCoordinate(Coordinate.Y_COORDINATE);

		List<Piece> pieces = new ArrayList<Piece>();
		for(int i = 0; i<1; i++)pieces.add(new Piece(PieceType.MARSHAL,          player));
		for(int i = 0; i<1; i++)pieces.add(new Piece(PieceType.GENERAL,          player));
		for(int i = 0; i<2; i++)pieces.add(new Piece(PieceType.COLONEL,          player));
		for(int i = 0; i<3; i++)pieces.add(new Piece(PieceType.MAJOR,            player));
		for(int i = 0; i<4; i++)pieces.add(new Piece(PieceType.CAPTAIN,          player));
		for(int i = 0; i<2; i++)pieces.add(new Piece(PieceType.LIEUTENANT,       player));
		for(int i = 0; i<2; i++)pieces.add(new Piece(PieceType.FIRST_LIEUTENANT, player));
		for(int i = 0; i<4; i++)pieces.add(new Piece(PieceType.SERGEANT,         player));
		for(int i = 0; i<5; i++)pieces.add(new Piece(PieceType.MINER,            player));
		for(int i = 0; i<8; i++)pieces.add(new Piece(PieceType.SCOUT,            player));
		for(int i = 0; i<1; i++)pieces.add(new Piece(PieceType.SPY,              player));
		for(int i = 0; i<5; i++)pieces.add(new Piece(PieceType.BOMB,             player));
		for(int i = 0; i<2; i++)pieces.add(new Piece(PieceType.FLAG,             player));		

		List<PieceLocationDescriptor> config = new ArrayList<PieceLocationDescriptor>();
		for(int y = 0, count = 0; y < 4; y++){
			for(int x = 0; x < 10; x++, count++){
				//System.out.println("Piece: " + pieces.get(count) + " x: " + (x+startingX) + " y: " + (y+startingY));
				config.add(new PieceLocationDescriptor(pieces.get(count), new BetaLocation2D(new Location2D(x + startingX, y + startingY))));
			}
		}

		return config;
	}

	@Test
	public void testBasicGameWithObservation() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		
		stratGameFactory.makeEpsilonStrategyGame(red, blue,null);
		
		
	}
}
