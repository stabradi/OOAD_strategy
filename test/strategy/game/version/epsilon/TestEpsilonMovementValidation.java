package strategy.game.version.epsilon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.common.StrategyRuntimeException;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementValidationStrategy;
import strategy.game.version.UniversalStrategyGameController;
import strategy.game.version.beta.BetaLocation2D;

public class TestEpsilonMovementValidation {
	MovementValidationStrategy validator;
	
	private class TestController extends UniversalStrategyGameController{		
		TestController(Collection<PieceLocationDescriptor> configuration){
			this.currentConfiguration = configuration;
			this.gameStarted = true;
		}
	}
	
	@Before
	public void before(){
		validator = new EpsilonMovementValidationStrategy();
	}
	
	@Test
	public void testScoutMoveMultipleSpacesVertically() throws StrategyException{
		Piece scoutPiece = new Piece(PieceType.SCOUT, PlayerColor.RED);
		PieceLocationDescriptor scout = new PieceLocationDescriptor(scoutPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(scout);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,5);
		
		validator.validateMove(controller, configuration, scout, from, to);
	}
	
	@Test
	public void testFLMoveTwoHorizontalStrike() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		Piece piece2 = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.BLUE);
		PieceLocationDescriptor pl = new PieceLocationDescriptor(piece, new BetaLocation2D(new Location2D(0,0)));
		PieceLocationDescriptor pl2 = new PieceLocationDescriptor(piece2, new BetaLocation2D(new Location2D(0,2)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(pl);
		configuration.add(pl2);
		TestController controller = new TestController(configuration);
		
		Location from = new BetaLocation2D(new Location2D(0,0));
		Location to = new BetaLocation2D(new Location2D(0,2));
		
		validator.validateMove(controller, configuration, pl, from, to);
	}
	
	@Test
	public void testValidMove() throws StrategyException{
		Piece majorPiece = new Piece(PieceType.MAJOR, PlayerColor.RED);
		PieceLocationDescriptor major = new PieceLocationDescriptor(majorPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(major);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,1);
		
		validator.validateMove(controller, configuration, major, from, to);
	}

	@Test(expected = StrategyException.class)
	public void testOtherPieceMoveMultipleSpaces() throws StrategyException{
		Piece nonScoutPiece = new Piece(PieceType.MAJOR, PlayerColor.RED);
		PieceLocationDescriptor major = new PieceLocationDescriptor(nonScoutPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(major);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,5);
		
		validator.validateMove(controller, configuration, major, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveBomb() throws StrategyException{
		Piece bombPiece = new Piece(PieceType.BOMB, PlayerColor.RED);
		PieceLocationDescriptor bomb = new PieceLocationDescriptor(bombPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(bomb);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,1);
		
		validator.validateMove(controller, configuration, bomb, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveFlag() throws StrategyException{
		Piece flagPiece = new Piece(PieceType.FLAG, PlayerColor.RED);
		PieceLocationDescriptor flag = new PieceLocationDescriptor(flagPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(flag);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,1);
		
		validator.validateMove(controller, configuration, flag, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveIntoLake() throws StrategyException{
		Piece nonScoutPiece = new Piece(PieceType.MAJOR, PlayerColor.RED);
		PieceLocationDescriptor major = new PieceLocationDescriptor(nonScoutPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(major);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(2,3);
		Location to = new Location2D(2,4);
		
		validator.validateMove(controller, configuration, major, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveOtherPiece() throws StrategyException{
		Piece piece1 = new Piece(PieceType.MAJOR, PlayerColor.RED);
		Piece piece2 = new Piece(PieceType.MAJOR, PlayerColor.RED);
		PieceLocationDescriptor major = new PieceLocationDescriptor(piece1, new BetaLocation2D(new Location2D(0,0)));
		PieceLocationDescriptor major2 = new PieceLocationDescriptor(piece2, new BetaLocation2D(new Location2D(0,1)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(major);
		configuration.add(major2);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,1);
		
		validator.validateMove(controller, configuration, major, from, to);
	}
	
	@Test(expected = StrategyRuntimeException.class)
	public void testControllerCannotBeNull() throws StrategyException{
		Piece majorPiece = new Piece(PieceType.MAJOR, PlayerColor.RED);
		PieceLocationDescriptor major = new PieceLocationDescriptor(majorPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(major);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,1);
		
		validator.validateMove(null, configuration, major, from, to);
	}
}
