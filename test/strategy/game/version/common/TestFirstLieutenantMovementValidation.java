package strategy.game.version.common;

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

public class TestFirstLieutenantMovementValidation {
	MovementValidationStrategy validator;
	
	private class TestController extends UniversalStrategyGameController{		
		TestController(Collection<PieceLocationDescriptor> configuration){
			this.currentConfiguration = configuration;
			this.gameStarted = true;
		}
	}
	
	@Before
	public void before(){
		validator = new FirstLieutenantMovementValidationStrategy();
	}
	
	@Test
	public void testNormalMove() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		PieceLocationDescriptor pl = new PieceLocationDescriptor(piece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(pl);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,1);
		
		validator.validateMove(controller, configuration, pl, from, to);
	}
	
	@Test(expected = StrategyRuntimeException.class)
	public void testDiagonalMove() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		PieceLocationDescriptor pl = new PieceLocationDescriptor(piece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(pl);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(1,1);
		
		validator.validateMove(controller, configuration, pl, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testDiagonalMoveBetaLocations() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		PieceLocationDescriptor pl = new PieceLocationDescriptor(piece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(pl);
		TestController controller = new TestController(configuration);
		
		Location from = new BetaLocation2D(new Location2D(0,0));
		Location to = new BetaLocation2D(new Location2D(1,1));
		
		validator.validateMove(controller, configuration, pl, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveTwoHorizontalNoStrike() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		PieceLocationDescriptor pl = new PieceLocationDescriptor(piece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(pl);
		TestController controller = new TestController(configuration);
		
		Location from = new BetaLocation2D(new Location2D(0,0));
		Location to = new BetaLocation2D(new Location2D(0,2));
		
		validator.validateMove(controller, configuration, pl, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testMoveTwoVerticalNoStrike() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		PieceLocationDescriptor pl = new PieceLocationDescriptor(piece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(pl);
		TestController controller = new TestController(configuration);
		
		Location from = new BetaLocation2D(new Location2D(0,0));
		Location to = new BetaLocation2D(new Location2D(2,0));
		
		validator.validateMove(controller, configuration, pl, from, to);
	}
	
	@Test
	public void testMoveTwoHorizontalStrike() throws StrategyException{
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
	
	@Test(expected = StrategyException.class)
	public void testMoveTwoHorizontalSameOwner() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		Piece piece2 = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
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
	
	@Test(expected = StrategyException.class)
	public void testMoveThreeVertical() throws StrategyException{
		Piece piece = new Piece(PieceType.FIRST_LIEUTENANT, PlayerColor.RED);
		PieceLocationDescriptor pl = new PieceLocationDescriptor(piece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(pl);
		TestController controller = new TestController(configuration);
		
		Location from = new BetaLocation2D(new Location2D(0,0));
		Location to = new BetaLocation2D(new Location2D(3,0));
		
		validator.validateMove(controller, configuration, pl, from, to);
	}
}
