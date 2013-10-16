package strategy.game.version.epsilon;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
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

public class PrintTesting {


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

	
	



	@Test //(expected = StrategyException.class)
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
		
		initLocation = new Location2D(9,6);
		nextLocation = new Location2D(9,5); // one space in front of it
		controller.move(controller.getPieceAt(initLocation).getType(), initLocation, nextLocation);
		
		initLocation = new Location2D(0,4);
		nextLocation = new Location2D(1,4); // one space in front of it
		controller.move(controller.getPieceAt(initLocation).getType(), initLocation, nextLocation);
		
		initLocation = new Location2D(9,5);
		nextLocation = new Location2D(9,4); // one space in front of it
		controller.move(controller.getPieceAt(initLocation).getType(), initLocation, nextLocation);
		
	//	return config;
	}
	
	//(PieceType piece, Location from, Location to,	MoveResult moveResults, StrategyException fault)
	@Test
	public void testJustObserver() throws IOException{
		System.setOut(new PrintStream("test/output.tmp"));
		StrategyGameReporter reporter = new StrategyGameReporter();
		
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		
		reporter.gameStart(red, blue);
		
		reporter.moveHappened(null, null, null, null, new StrategyException("this is a test"));
		reporter.moveHappened(
				PieceType.MARSHAL, 
				new Location2D(0,6), 
				new Location2D(0,3), 
				new MoveResult(
						MoveResultStatus.OK, 
						new PieceLocationDescriptor(
								new Piece(PieceType.MARSHAL,
										PlayerColor.BLUE),
								new Location2D(0,3))),
				null);
		reporter.moveHappened(
				PieceType.MARSHAL, 
				new Location2D(0,6), 
				new Location2D(0,3), 
				new MoveResult(
						MoveResultStatus.BLUE_WINS, 
						new PieceLocationDescriptor(
								new Piece(PieceType.MARSHAL,
										PlayerColor.BLUE),
								new Location2D(0,3))),
				null);
		reporter.moveHappened(
				PieceType.MARSHAL, 
				new Location2D(0,6), 
				new Location2D(0,3), 
				new MoveResult(
						MoveResultStatus.RED_WINS, 
						new PieceLocationDescriptor(
								new Piece(PieceType.MARSHAL,
										PlayerColor.BLUE),
								new Location2D(0,3))),
				null);
	    //final File expected = new File("test/strategy/game/version/epsilon/redWinsReference.txt");
	    //final File output = new File("test/output.tmp");
		

	    
	    InputStream    expectedfis;
	    BufferedReader expectedbr;
	    String         expectedLine;
	    String	       totalExpected = "";

	    expectedfis = new FileInputStream("test/strategy/game/version/epsilon/redWinsReference.txt");
	    expectedbr = new BufferedReader(new InputStreamReader(expectedfis, Charset.forName("UTF-8")));
	    while ((expectedLine = expectedbr.readLine()) != null) {
	    	totalExpected += expectedLine;
	    }
	    
	    
	    
	    
	    
	    
	    InputStream    actualfis;
	    BufferedReader actualbr;
	    String         actualLine;
	    String	       totalActual= "";

	    actualfis = new FileInputStream("test/output.tmp");
	    actualbr = new BufferedReader(new InputStreamReader(actualfis, Charset.forName("UTF-8")));
	    while ((actualLine = actualbr.readLine()) != null) {
	    	totalActual += actualLine;
	    }
		assertEquals(totalExpected,totalActual);


	}
}
