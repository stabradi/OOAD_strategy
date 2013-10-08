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
import strategy.game.version.UniversalStrategyGameController;
import strategy.game.version.beta.BetaLocation2D;
import strategy.game.version.common.ScoutMovementValidationStrategy;

public class TestScoutMovementValidation {
	ScoutMovementValidationStrategy validator;
	
	private class TestController extends UniversalStrategyGameController{		
		TestController(Collection<PieceLocationDescriptor> configuration){
			this.currentConfiguration = configuration;
			this.gameStarted = true;
		}
	}
	
	@Before
	public void before(){
		validator = new ScoutMovementValidationStrategy();
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
	public void testScoutMoveMultipleSpacesVerticallyNegativeDirection() throws StrategyException{
		Piece scoutPiece = new Piece(PieceType.SCOUT, PlayerColor.RED);
		PieceLocationDescriptor scout = new PieceLocationDescriptor(scoutPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(scout);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,5);
		Location to = new Location2D(0,3);
		
		validator.validateMove(controller, configuration, scout, from, to);
	}
	
	@Test
	public void testScoutMoveMultipleSpacesHorizontally() throws StrategyException{
		Piece scoutPiece = new Piece(PieceType.SCOUT, PlayerColor.RED);
		PieceLocationDescriptor scout = new PieceLocationDescriptor(scoutPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(scout);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(5,0);
		
		validator.validateMove(controller, configuration, scout, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testScoutMoveThroughOtherPiece() throws StrategyException{
		Piece scoutPiece = new Piece(PieceType.SCOUT, PlayerColor.RED);
		Piece otherPiece = new Piece(PieceType.MAJOR, PlayerColor.RED);
		PieceLocationDescriptor scout = new PieceLocationDescriptor(scoutPiece, new BetaLocation2D(new Location2D(0,0)));
		PieceLocationDescriptor major = new PieceLocationDescriptor(otherPiece, new BetaLocation2D(new Location2D(0,4)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(scout);
		configuration.add(major);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,5);
		
		validator.validateMove(controller, configuration, scout, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testScoutMove0Spaces() throws StrategyException{
		Piece scoutPiece = new Piece(PieceType.SCOUT, PlayerColor.RED);
		PieceLocationDescriptor scout = new PieceLocationDescriptor(scoutPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(scout);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(0,0);
		
		validator.validateMove(controller, configuration, scout, from, to);
	}
	
	@Test(expected = StrategyException.class)
	public void testScoutMoveMultipleSpacesDiagonally() throws StrategyException{
		Piece scoutPiece = new Piece(PieceType.SCOUT, PlayerColor.RED);
		PieceLocationDescriptor scout = new PieceLocationDescriptor(scoutPiece, new BetaLocation2D(new Location2D(0,0)));
		List<PieceLocationDescriptor> configuration = new ArrayList<PieceLocationDescriptor>();
		configuration.add(scout);
		TestController controller = new TestController(configuration);
		
		Location from = new Location2D(0,0);
		Location to = new Location2D(2,2);
		
		validator.validateMove(controller, configuration, scout, from, to);
	}
	
	@Test(expected = StrategyRuntimeException.class)
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
}
