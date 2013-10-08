package strategy.game.version.delta;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.beta.BetaLocation2D;
import strategy.game.version.delta.DeltaPlacementRules;

public class DeltaStrategyMainTest {
	
	// Adds all pieces for one Beta strategy player, filling in the spaces sequentially
	public List<PieceLocationDescriptor> createPlayerInitialConfiguration(PlayerColor player, Location start){
		int startingX = start.getCoordinate(Coordinate.X_COORDINATE);
		int startingY = start.getCoordinate(Coordinate.Y_COORDINATE);

		List<Piece> pieces = new ArrayList<Piece>();
		for(int i = 0; i<1; i++)pieces.add(new Piece(PieceType.MARSHAL, player));
		for(int i = 0; i<1; i++)pieces.add(new Piece(PieceType.GENERAL, player));
		for(int i = 0; i<2; i++)pieces.add(new Piece(PieceType.COLONEL, player));
		for(int i = 0; i<3; i++)pieces.add(new Piece(PieceType.MAJOR, player));
		for(int i = 0; i<4; i++)pieces.add(new Piece(PieceType.CAPTAIN, player));
		for(int i = 0; i<4; i++)pieces.add(new Piece(PieceType.LIEUTENANT, player));
		for(int i = 0; i<4; i++)pieces.add(new Piece(PieceType.SERGEANT, player));
		for(int i = 0; i<5; i++)pieces.add(new Piece(PieceType.MINER, player));
		for(int i = 0; i<8; i++)pieces.add(new Piece(PieceType.SCOUT, player));
		for(int i = 0; i<1; i++)pieces.add(new Piece(PieceType.SPY, player));
		for(int i = 0; i<6; i++)pieces.add(new Piece(PieceType.BOMB, player));
		pieces.add(new Piece(PieceType.FLAG, player));		

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
	public void testValidateValidPlacement() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		DeltaPlacementRules placementRules = new DeltaPlacementRules();
		assertEquals(0,placementRules.checkNumberOfPieces(red));
		assertEquals(0,placementRules.checkNumberOfPieces(blue));
		placementRules.validatePlacement(red,blue);
	}
	
	@Test (expected = StrategyException.class)
	public void testValidateInvalidPlacement() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		blue.remove(7);
		DeltaPlacementRules placementRules = new DeltaPlacementRules();
		placementRules.validatePlacement(red,blue);
	}
	
	@Test (expected = StrategyException.class)
	public void testValidateInvalidSidePlacement1() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,7));
		DeltaPlacementRules placementRules = new DeltaPlacementRules();
		placementRules.validatePlacement(red,blue);
	}
	@Test (expected = StrategyException.class)
	public void testValidateInvalidSidePlacement2() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,1));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		DeltaPlacementRules placementRules = new DeltaPlacementRules();
		placementRules.validatePlacement(red,blue);
	}
	@Test
	public void testValidateInvalidSidePlacement3() throws StrategyException{
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,1));
		List<PieceLocationDescriptor> blue = createPlayerInitialConfiguration(PlayerColor.BLUE, new Location2D(0,6));
		blue.remove(0);
		blue.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		DeltaPlacementRules placementRules = new DeltaPlacementRules();
		
		assertEquals(false,placementRules.checkPiecesOnSide(PlayerColor.BLUE,blue));
		assertEquals(false,placementRules.checkPiecesOnSide(PlayerColor.BLUE,null));
	}
	
	@Test (expected = StrategyException.class)
	public void testOverlappingPieces() throws StrategyException{
		List<PieceLocationDescriptor> config = new ArrayList<PieceLocationDescriptor>();
		config.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		config.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		DeltaPlacementRules placementRules = new DeltaPlacementRules();
		
		placementRules.checkNoOverlappingPieces(config);
	}
	@Test
	public void testCheckNumberOfPieces1(){
		List<PieceLocationDescriptor> red = createPlayerInitialConfiguration(PlayerColor.RED, new Location2D(0,0));
		DeltaPlacementRules placementRules = new DeltaPlacementRules();
		
		assertEquals(0,placementRules.checkNumberOfPieces(red));
		
		red.add(new PieceLocationDescriptor(new Piece(PieceType.BOMB, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-12,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.SPY, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-11,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.SCOUT, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-10,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.MINER, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-9,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.MAJOR, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-8,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.GENERAL, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-7,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.SERGEANT, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-6,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.LIEUTENANT, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-5,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.CAPTAIN, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-4,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.COLONEL, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-3,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.MARSHAL, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-2,placementRules.checkNumberOfPieces(red));
		
		red.remove(40);
		red.add(new PieceLocationDescriptor(new Piece(PieceType.FLAG, PlayerColor.RED), new BetaLocation2D(new Location2D(0, 6))));
		assertEquals(-1,placementRules.checkNumberOfPieces(red));
	}

}
