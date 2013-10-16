package strategy.game.version.epsilon;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.StrategyGameFactory;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.common.StrategyGameObserver;
import strategy.game.reporter.StrategyGameReporter;
import strategy.game.version.beta.BetaLocation2D;

public class EpsilonStrategyMainTest {

	// Adds all pieces for one Epsilon strategy player, filling in the spaces sequentially. Has two flags but only 7 scouts (dropped one scout)

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
		
		Collection<StrategyGameObserver> reporters = new ArrayList<StrategyGameObserver>();
		reporters.add(new StrategyGameReporter());
		
		StrategyGameController controller = StrategyGameFactory.getInstance().makeEpsilonStrategyGame(red, blue,reporters);
		
		controller.startGame();
		
		Location initLocation = new Location2D(0,3);
		Location nextLocation = new Location2D(0,4); // one space in front of it
		controller.move(controller.getPieceAt(initLocation).getType(), initLocation, nextLocation);


//		return config;
	}
	
	@Test
	public void testMoveAPieceAndStrike()throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		
		StrategyGameController controller = StrategyGameFactory.getInstance().makeEpsilonStrategyGame(red, blue,null);
		
		controller.startGame();
		
		//red moves scout 1 space
		Location2D from = new Location2D(0,3);
		Location2D to = new Location2D(0,4);
		controller.move(controller.getPieceAt(from).getType(), from, to);
		
		//blue moves marshal 1 space
		from = new Location2D(0,6);
		to = new Location2D(0,5);
		controller.move(controller.getPieceAt(from).getType(), from, to);
		
		//red moves scout for strike, loses
		from = new Location2D(0,4);
		System.out.println(controller.getPieceAt(from));
		to = new Location2D(0,5);
		System.out.println(controller.getPieceAt(to));
		controller.move(controller.getPieceAt(from).getType(), from, to);
		assertEquals(new Piece(PieceType.MARSHAL, PlayerColor.BLUE), controller.getPieceAt(from));
		assertEquals(null, controller.getPieceAt(to));
	}
	
	@Test
	public void redResigns() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		
		StrategyGameController controller = StrategyGameFactory.getInstance().makeEpsilonStrategyGame(red, blue,null);
		
		controller.startGame();
		
		MoveResult result = controller.move(null, null, null);
		assertEquals(MoveResultStatus.BLUE_WINS,result.getStatus());
	}
	
	@Test
	public void blueResigns() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		
		StrategyGameController controller = StrategyGameFactory.getInstance().makeEpsilonStrategyGame(red, blue,null);
		
		controller.startGame();
		
		//red moves scout 1 space
		Location2D from = new Location2D(0,3);
		Location2D to = new Location2D(0,4);
		controller.move(controller.getPieceAt(from).getType(), from, to);
		
		// Blue resigns
		MoveResult result = controller.move(null, null, null);
		assertEquals(MoveResultStatus.RED_WINS,result.getStatus());

	}
}
