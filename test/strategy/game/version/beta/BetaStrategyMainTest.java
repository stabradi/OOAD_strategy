package strategy.game.version.beta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
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
	// Order is important!
	public List<PieceLocationDescriptor> createPlayerInitialConfiguration(PlayerColor player, Location start){
		int startingX = start.getCoordinate(Coordinate.X_COORDINATE);
		int startingY = start.getCoordinate(Coordinate.Y_COORDINATE);

		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(new Piece(PieceType.MARSHAL, player));
		pieces.add(new Piece(PieceType.SERGEANT, player));
		pieces.add(new Piece(PieceType.COLONEL, player));
		pieces.add(new Piece(PieceType.CAPTAIN, player));
		pieces.add(new Piece(PieceType.CAPTAIN, player));
		pieces.add(new Piece(PieceType.LIEUTENANT, player));
		pieces.add(new Piece(PieceType.LIEUTENANT, player));
		pieces.add(new Piece(PieceType.LIEUTENANT, player));
		pieces.add(new Piece(PieceType.SERGEANT, player));
		pieces.add(new Piece(PieceType.COLONEL, player));
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
	public void testGetPieceAtLocationGameNotStarted() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.BLUE), new Location2D(0,0)));

		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);

		assertNull(game.getPieceAt(new Location2D(0,0)));
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
	
	@Test (expected = StrategyException.class)
	public void testCheckNumberOfPieces1() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.BLUE), new Location2D(0,5)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkNumberOfPieces(blue,blue);
	}
	@Test (expected = StrategyException.class)
	public void testCheckNumberOfPieces2() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.BLUE), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.BLUE), new Location2D(1,5)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkNumberOfPieces(blue,blue);
	}
	@Test (expected = StrategyException.class)
	public void testCheckNumberOfPieces3() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.BLUE), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.BLUE), new Location2D(1,5)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkNumberOfPieces(blue,blue);
	}
	@Test (expected = StrategyException.class)
	public void testCheckNumberOfPieces4() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.BLUE), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.BLUE), new Location2D(1,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.BLUE), new Location2D(2,5)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkNumberOfPieces(blue,blue);
	}
	@Test (expected = StrategyException.class)
	public void testCheckNumberOfPieces5() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.BLUE), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.BLUE), new Location2D(1,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.BLUE), new Location2D(2,5)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkNumberOfPieces(blue,blue);
	}
	@Test (expected = StrategyException.class)
	public void testCheckNumberOfPieces6() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.BLUE), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.BLUE), new Location2D(1,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.BLUE), new Location2D(2,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.BLUE), new Location2D(3,5)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkNumberOfPieces(blue,blue);
	}
	@Test (expected = StrategyException.class)
	public void testCheckNumberOfPieces7() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.BLUE), new Location2D(0,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.BLUE), new Location2D(1,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.BLUE), new Location2D(2,5)));
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.BLUE), new Location2D(3,5)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkNumberOfPieces(blue,blue);
	}

	
	@Test (expected = StrategyException.class)
	public void testCheckPiecesOnSide1() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> blue = new ArrayList<PieceLocationDescriptor>();
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.BLUE), new Location2D(0,0)));
		List<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		red.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.RED), new Location2D(90,90)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(blue, blue);
		((BetaStrategyGameController) game).checkPiecesOnSide(blue,blue);
		((BetaStrategyGameController) game).checkPiecesOnSide(red,red);
	}
	@Test (expected = StrategyException.class)
	public void testCheckPiecesOnSide2() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		red.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.RED), new Location2D(90,90)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(red, red);
		((BetaStrategyGameController) game).checkPiecesOnSide(red,red);
	}
	@Test (expected = StrategyException.class)
	public void testCheckPiecesOnSide3() throws StrategyException{
		StrategyGameFactory stratGameFactory = StrategyGameFactory.getInstance();
		List<PieceLocationDescriptor> red = new ArrayList<PieceLocationDescriptor>();
		red.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.RED), new Location2D(3,3)));
		StrategyGameController game = stratGameFactory.makeBetaStrategyGame(red, red);
		((BetaStrategyGameController) game).checkPiecesOnSide(red,red);
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

	// TODO Test return value!
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
		game.move(PieceType.SPY, initLocation, nextLocation);
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
		assertEquals(PieceType.FLAG, game.getPieceAt(initLocation).getType());
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);
	}

	// TODO Refactor this test once game ends are actually implemented
	// valid move, but game over
	@Test(expected = StrategyException.class)
	public void testValidMoveButGameOver() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, StrategyException{
		Class<BetaStrategyGameController> gameController = (Class<BetaStrategyGameController>) Class.forName("strategy.game.version.beta.BetaStrategyGameController");

		BetaStrategyGameController game = new BetaStrategyGameController(null, null);
		Field instance = gameController.getDeclaredField("gameOver");
		instance.setAccessible(true);
		instance.set(game, true); 

		game.move(PieceType.SPY, new Location2D(0,0), new Location2D(0,1));
	}

	@Test
	public void testValidStrikeDefenderWins() throws StrategyException{
		StrategyGameController game = createAndStartGame();

		// Red moves a Sergeant
		Location initLocation = new Location2D(5,1);
		Location nextLocation = new Location2D(5,2);
		Piece redSergeant = game.getPieceAt(initLocation);
		assertEquals(PieceType.SERGEANT, redSergeant.getType());
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Blue move
		initLocation = new Location2D(5,4);
		nextLocation = new Location2D(5,3);
		Piece blueLieutenant = game.getPieceAt(initLocation);
		assertEquals(PieceType.LIEUTENANT, blueLieutenant.getType());
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Red attacks
		initLocation = new Location2D(5,2);
		nextLocation = new Location2D(5,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Blue wins the strike, and moves into red's former position
		assertEquals("Winning piece did not move onto losing piece's position", blueLieutenant, game.getPieceAt(initLocation));
		assertNull("Winning piece still at original location", game.getPieceAt(nextLocation));
	}

	@Test
	public void testValidStrikeAttackerWins() throws StrategyException{
		StrategyGameController game = createAndStartGame();

		// Red moves a Colonel
		Location initLocation = new Location2D(3,1);
		Location nextLocation = new Location2D(3,2);
		Piece redColonel = game.getPieceAt(initLocation);
		assertEquals(PieceType.COLONEL, redColonel.getType());
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Blue moves a Lieutenant
		initLocation = new Location2D(5,4);
		nextLocation = new Location2D(5,3);
		Piece blueLieutenant = game.getPieceAt(initLocation);
		assertEquals(PieceType.LIEUTENANT, blueLieutenant.getType());
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Red moves again
		initLocation = new Location2D(3,2);
		nextLocation = new Location2D(3,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Blue moves next to red
		initLocation = new Location2D(5,3);
		nextLocation = new Location2D(4,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Red attacks
		initLocation = new Location2D(3,3);
		nextLocation = new Location2D(4,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Red wins the strike, and moves into blue's former position
		assertEquals("Winning piece did not move onto losing piece's position", redColonel, game.getPieceAt(nextLocation));
		assertNull("Winning piece still at original location", game.getPieceAt(initLocation));
	}

	@Test
	public void testValidStrikeDraw() throws StrategyException{
		StrategyGameController game = createAndStartGame();

		// Red moves a Lieutenant
		Location initLocation = new Location2D(1,1);
		Location nextLocation = new Location2D(1,2);
		Piece redLieutenant = game.getPieceAt(initLocation);
		assertEquals(PieceType.LIEUTENANT, redLieutenant.getType());
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Blue moves a Lieutenant
		initLocation = new Location2D(5,4);
		nextLocation = new Location2D(5,3);
		Piece blueLieutenant = game.getPieceAt(initLocation);
		assertEquals(PieceType.LIEUTENANT, blueLieutenant.getType());
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Red moves again
		initLocation = new Location2D(1,2);
		nextLocation = new Location2D(1,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Blue moves
		initLocation = new Location2D(5,3);
		nextLocation = new Location2D(4,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Red moves
		initLocation = new Location2D(1,3);
		nextLocation = new Location2D(2,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Blue moves
		initLocation = new Location2D(4,3);
		nextLocation = new Location2D(3,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Red attacks
		initLocation = new Location2D(2,3);
		nextLocation = new Location2D(3,3);
		game.move(game.getPieceAt(initLocation).getType(), initLocation, nextLocation);

		// Both pieces removed from the bored
		assertNull("Both pieces should have been removed", game.getPieceAt(initLocation));
		assertNull("Both pieces should have been removed", game.getPieceAt(nextLocation));
	}
	
	@Test
	public void testCombatOutcome() throws StrategyException{
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
		
		
		BetaStrategyGameController game = (BetaStrategyGameController) stratGameFactory.makeBetaStrategyGame(red, blue);
		game.startGame();
		assertEquals(StrikeResultBeta.DRAW,game.combatResult(PieceType.MARSHAL,PieceType.MARSHAL));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.LIEUTENANT,PieceType.SERGEANT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.COLONEL,PieceType.GENERAL));
		assertEquals(StrikeResultBeta.DRAW,game.combatResult(PieceType.LIEUTENANT,PieceType.LIEUTENANT));
		
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.MAJOR,PieceType.COLONEL));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.CAPTAIN,PieceType.MAJOR));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.LIEUTENANT,PieceType.CAPTAIN));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.SERGEANT,PieceType.LIEUTENANT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.MINER,PieceType.SERGEANT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.SCOUT,PieceType.MINER));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.SPY,PieceType.SCOUT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.BOMB,PieceType.SPY));
		
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.COLONEL,PieceType.MAJOR));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.MAJOR,PieceType.CAPTAIN));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.CAPTAIN,PieceType.LIEUTENANT));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.LIEUTENANT,PieceType.SERGEANT));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.SERGEANT,PieceType.MINER));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.MINER,PieceType.SCOUT));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.SCOUT,PieceType.SPY));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,game.combatResult(PieceType.SPY,PieceType.BOMB));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.SPY,PieceType.MARSHAL));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.MARSHAL,PieceType.SPY));
		
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.COLONEL,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.MAJOR,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.CAPTAIN,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.LIEUTENANT,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.SERGEANT,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.MINER,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.SCOUT,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.SPY,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.SPY,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,game.combatResult(PieceType.MARSHAL,PieceType.FLAG));
		
	}
}
