package strategy.game.version.beta;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.StrategyGameFactory;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;

public class BetaStrategyMainTest {
	
	@Test
	public void testMakeBetaStrategyGame() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		stratGameFactory.makeBetaStrategyGame(null, null);
	}
	@Test
	public void testGetPieceAtLocation() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(0,0)));
		
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		
		assertEquals(blue.get(0).getPiece(), game.getPieceAt(new Location2D(0,0)));
	}
	@Test (expected = StrategyException.class)
	public void testStartGameValidPosition() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(3,3)));
		
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		game.startGame();
	}

	@Test (expected = StrategyException.class)
	public void testStartGameValidlayout() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(5,0)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(5,1)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(5,2)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(5,3)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(5,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(5,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(4,0)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(4,1)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(4,2)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(4,3)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(4,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(4,5)));
		
		List<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(0,0)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(0,1)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(0,2)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(0,3)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(0,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(1,0)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(1,1)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(1,2)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(1,3)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(1,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new Location2D(1,5)));
		
		
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, red);
		game.startGame();
	}
	@Test
	public void testStartGameValidPieces() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.BLUE), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.BLUE), new Location2D(1,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.BLUE), new Location2D(2,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.BLUE), new Location2D(3,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.BLUE), new Location2D(4,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.BLUE), new Location2D(5,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.BLUE), new Location2D(0,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.BLUE), new Location2D(1,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.BLUE), new Location2D(2,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.BLUE), new Location2D(3,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.BLUE), new Location2D(4,4)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.BLUE), new Location2D(5,4)));
		
		List<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		red.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.RED), new Location2D(0,0)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.RED), new Location2D(1,0)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.RED), new Location2D(2,0)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.RED), new Location2D(3,0)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.RED), new Location2D(4,0)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.RED), new Location2D(5,0)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.RED), new Location2D(0,1)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.RED), new Location2D(1,1)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.RED), new Location2D(2,1)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.RED), new Location2D(3,1)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.RED), new Location2D(4,1)));
		red.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.RED), new Location2D(5,1)));
		
		
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(red, blue);
		game.startGame();
	}
	
}
