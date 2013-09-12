package strategy.game.version.beta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.StrategyGameFactory;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;

public class BetaStrategyMainTest {
	StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
	
	// Helper method to create a basic initial piece configuration
	public StrategyGameController createInitialController() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		
		List<PieceLocationDescriptor> redConfiguration = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blueConfiguration = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,4));

		return stratGameFactory.makeBetaStrategyGame(redConfiguration, blueConfiguration);
	}
	
	// Helper method to create a basic initial piece configuration and start game
	public StrategyGameController createAndStartGame() throws StrategyException{
		StrategyGameController game = createInitialController();
		game.startGame();
		return game;
	}
	
	// Adds all pieces for one Beta strategy player, filling in the spaces sequentially
	// Flag starts at 4,1
	public List<PieceLocationDescriptor> createPlayerInitialConfiguration(PlayerColor player, Location start){
		int startingX = start.getCoordinate(Coordinate.X_COORDINATE);
		int startingY = start.getCoordinate(Coordinate.Y_COORDINATE);
		
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece(PieceType.MARSHAL, player));
		pieces.add(new Piece(PieceType.COLONEL, player));
		pieces.add(new Piece(PieceType.COLONEL, player));
		pieces.add(new Piece(PieceType.CAPTAIN, player));
		pieces.add(new Piece(PieceType.CAPTAIN, player));
		pieces.add(new Piece(PieceType.LIEUTENANT, player));
		pieces.add(new Piece(PieceType.LIEUTENANT, player));
		pieces.add(new Piece(PieceType.LIEUTENANT, player));
		pieces.add(new Piece(PieceType.SERGEANT, player));
		pieces.add(new Piece(PieceType.SERGEANT, player));
		pieces.add(new Piece(PieceType.FLAG, player));
		pieces.add(new Piece(PieceType.SERGEANT, player));
		
		List<PieceLocationDescriptor> config = new ArrayList<PieceLocationDescriptor>();
		for(int y = 0, count = 0; y < 2; y++){
			for(int x = 0; x < 6; x++, count++){
				config.add(new PieceLocationDescriptor(pieces.get(count), new Location2D(x + startingX, y + startingY)));
			}
		}
		
		return config;
	}
	
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
		StrategyGameController game = createAndStartGame();
		game.startGame();
		Location initLocation = new Location2D(5,1);
		Location nextLocation = new Location2D(5,2); // one space in front of it
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}
	
	@Test(expected = StrategyException.class)
	public void testInvalidMoveOffBoard() throws StrategyException{
		StrategyGameController game = createAndStartGame();
		Location initLocation = new Location2D(5,0);
		Location nextLocation = new Location2D(6,0); // off the edge of the board
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}
	
	@Test(expected = StrategyException.class)
	public void testInvalidMoveCollision() throws StrategyException{
		StrategyGameController game = createAndStartGame();
		Location initLocation = new Location2D(0,0);
		Location nextLocation = new Location2D(0,1); // into piece in front of it
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}
	
	@Test(expected = StrategyException.class)
	public void testInvalidMoveTooFar() throws StrategyException{
		StrategyGameController game = createAndStartGame();
		Location initLocation = new Location2D(0,1);
		Location nextLocation = new Location2D(0,3); // two spaces in front
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}
	
	@Test(expected = StrategyException.class)
	public void testInvalidMoveWrongPlayer() throws StrategyException{
		StrategyGameController game = createAndStartGame();
		Location initLocation = new Location2D(0,4); // blue's piece
		Location nextLocation = new Location2D(0,3); // one space down
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveNoPieceAtSpace() throws StrategyException{
		StrategyGameController game = createAndStartGame();
		Location initLocation = new Location2D(0,3); // no piece there
		Location nextLocation = new Location2D(0,2);
		game.move(PieceType.SPY, initLocation, nextLocation);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveGameNotStarted() throws StrategyException{
		StrategyGameController game = createInitialController();
		Location initLocation = new Location2D(5,1);
		Location nextLocation = new Location2D(5,2); // valid move
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}
	
	@Test
	public void testMultipleValidMoves() throws StrategyException{
		StrategyGameController game = createAndStartGame();
		
		// Red move
		Location initLocation = new Location2D(5,1);
		Location nextLocation = new Location2D(5,2);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
		
		// Blue move
		initLocation = new Location2D(0,4);
		nextLocation = new Location2D(0,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
		
		// Red move into previous occupied position
		initLocation = new Location2D(5,0);
		nextLocation = new Location2D(5,1);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
		
		// Blue move same piece a second time
		initLocation = new Location2D(0,3);
		nextLocation = new Location2D(0,2);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
		
		assertNotNull(game.getPieceAt(new Location2D(0,2)));
		assertNotNull(game.getPieceAt(new Location2D(5,1)));
		assertNotNull(game.getPieceAt(new Location2D(5,2)));
		assertNull(game.getPieceAt(new Location2D(5,0)));
		assertNull(game.getPieceAt(new Location2D(0,4)));
		assertNull(game.getPieceAt(new Location2D(0,3)));
	}
	
	@Test(expected = StrategyException.class)
	public void testFlagTriesToMove() throws StrategyException{
		StrategyGameController game = createAndStartGame();
		Location initLocation = new Location2D(4,1);
		Location nextLocation = new Location2D(4,2); // valid move, but not for flag!
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}
	
	// TODO valid move, but game over
}
