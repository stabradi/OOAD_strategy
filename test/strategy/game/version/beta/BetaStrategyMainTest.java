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
	
}
