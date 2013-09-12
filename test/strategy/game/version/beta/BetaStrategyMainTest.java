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
	
	@Test
	public void testGetPieceAtEmptyLocation() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		
		// Create board with no pieces
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		List<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(red, blue);
		
		assertNull("Expected location to be empty.", game.getPieceAt(new Location2D(0,0)));
	}
	
	@Test
	public void testValidMoveNoCombat() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		
		// Create board with one piece
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		List<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(0,0)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(red, blue);
		
		// Move the piece and make sure it is in the new location
		game.move(PieceType.SPY, new Location2D(0,0), new Location2D(0,1));
		assertEquals("Piece not found in new location", new Piece(PieceType.SPY, PlayerColor.BLUE), game.getPieceAt(new Location2D(0,1)));
		assertNull("Piece still at old location", game.getPieceAt(new Location2D(0,0)));
	}
}
