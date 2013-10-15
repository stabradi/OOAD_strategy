package strategy.game.version.epsilon;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.StrikeStrategy;
import strategy.game.version.beta.BetaLocation2D;
import strategy.game.version.beta.StrikeResultBeta;

public class EpsilonStrikeStrategyTest {
	StrikeStrategy striker;
	Collection<PieceLocationDescriptor> configuration;
	
	@Before
	public void before(){
		striker = new EpsilonStrikeStrategy();
		configuration = createInitialConfiguration();
	}
	
	public Collection<PieceLocationDescriptor> createInitialConfiguration(){
		Collection<PieceLocationDescriptor> configuration1 = new ArrayList<PieceLocationDescriptor>();
		configuration1.add(makePld(PieceType.FLAG, PlayerColor.RED, 0, 0));
		configuration1.add(makePld(PieceType.FLAG, PlayerColor.RED, 0, 1));
		configuration1.add(makePld(PieceType.FLAG, PlayerColor.BLUE, 9, 9));
		configuration1.add(makePld(PieceType.FLAG, PlayerColor.BLUE, 9, 8));
		return configuration1;
	}
	
	public PieceLocationDescriptor makePld(PieceType type, PlayerColor color, int x, int y){
		Location loc = new BetaLocation2D(new Location2D(x, y));
		return new PieceLocationDescriptor(new Piece(type, color), loc);
	}
	
	@Test
	public void testAttackerWins() throws StrategyException{
		Collection<PieceLocationDescriptor> newconfiguration = new ArrayList<PieceLocationDescriptor>();
		for(PieceLocationDescriptor pl: configuration){
			newconfiguration.add(pl);
		}
		PieceLocationDescriptor attacker = makePld(PieceType.MARSHAL, PlayerColor.RED, 4,5);
		PieceLocationDescriptor defender = makePld(PieceType.MINER, PlayerColor.BLUE, 5,5);
		configuration.add(attacker);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.MARSHAL, PlayerColor.RED, 5,5),result.getBattleWinner());
		assertEquals(MoveResultStatus.OK,result.getStatus());
		
		newconfiguration.add(result.getBattleWinner());
		assertEquals(newconfiguration, configuration);
	}
	
	@Test
	public void testDefenderWins() throws StrategyException{
		Collection<PieceLocationDescriptor> newconfiguration = new ArrayList<PieceLocationDescriptor>();
		for(PieceLocationDescriptor pl: configuration){
			newconfiguration.add(pl);
		}
		PieceLocationDescriptor attacker = makePld(PieceType.MINER, PlayerColor.RED, 4,5);
		PieceLocationDescriptor defender = makePld(PieceType.MARSHAL, PlayerColor.BLUE, 5,5);
		configuration.add(attacker);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.MARSHAL, PlayerColor.BLUE, 4,5),result.getBattleWinner());
		assertEquals(MoveResultStatus.OK,result.getStatus());
		
		newconfiguration.add(result.getBattleWinner());
		assertEquals(newconfiguration, configuration);
	}
	
	@Test
	public void testDraw() throws StrategyException{
		Collection<PieceLocationDescriptor> newconfiguration = new ArrayList<PieceLocationDescriptor>();
		for(PieceLocationDescriptor pl: configuration){
			newconfiguration.add(pl);
		}
		PieceLocationDescriptor attacker = makePld(PieceType.MARSHAL, PlayerColor.RED, 4,5);
		PieceLocationDescriptor defender = makePld(PieceType.MARSHAL, PlayerColor.BLUE, 5,5);
		configuration.add(attacker);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(null,result.getBattleWinner());
		assertEquals(MoveResultStatus.OK,result.getStatus());
		
		assertEquals(newconfiguration, configuration);
	}
	
	@Test
	public void testFirstLieutenantAttacksWins() throws StrategyException{
		Collection<PieceLocationDescriptor> newconfiguration = new ArrayList<PieceLocationDescriptor>();
		for(PieceLocationDescriptor pl: configuration){
			newconfiguration.add(pl);
		}
		PieceLocationDescriptor attacker = makePld(PieceType.FIRST_LIEUTENANT, PlayerColor.RED, 4,5);
		PieceLocationDescriptor defender = makePld(PieceType.MINER, PlayerColor.BLUE, 6,5);
		configuration.add(attacker);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.FIRST_LIEUTENANT, PlayerColor.RED, 6,5),result.getBattleWinner());
		assertEquals(MoveResultStatus.OK,result.getStatus());
		
		newconfiguration.add(result.getBattleWinner());
		assertEquals(newconfiguration, configuration);
	}
	
	@Test
	public void testFirstLieutenantAttacksLoses() throws StrategyException{
		Collection<PieceLocationDescriptor> newconfiguration = new ArrayList<PieceLocationDescriptor>();
		for(PieceLocationDescriptor pl: configuration){
			newconfiguration.add(pl);
		}
		PieceLocationDescriptor attacker = makePld(PieceType.FIRST_LIEUTENANT, PlayerColor.RED, 4,5);
		PieceLocationDescriptor defender = makePld(PieceType.MARSHAL, PlayerColor.BLUE, 6,5);
		configuration.add(attacker);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(defender, result.getBattleWinner());
		assertEquals(MoveResultStatus.OK,result.getStatus());
		
		newconfiguration.add(defender);
		assertEquals(newconfiguration, configuration);
	}
	
	@Test
	public void testFirstLieutenantAttacksLosesFromOneSpaceAway() throws StrategyException{
		Collection<PieceLocationDescriptor> newconfiguration = new ArrayList<PieceLocationDescriptor>();
		for(PieceLocationDescriptor pl: configuration){
			newconfiguration.add(pl);
		}
		PieceLocationDescriptor attacker = makePld(PieceType.FIRST_LIEUTENANT, PlayerColor.RED, 4,5);
		PieceLocationDescriptor defender = makePld(PieceType.MARSHAL, PlayerColor.BLUE, 5,5);
		configuration.add(attacker);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.MARSHAL, PlayerColor.BLUE, 4,5), result.getBattleWinner());
		assertEquals(MoveResultStatus.OK,result.getStatus());
		
		newconfiguration.add(result.getBattleWinner());
		assertEquals(newconfiguration, configuration);
	}
	
	@Test
	public void testCaptureLastBlueFlag() throws StrategyException{
		configuration = new ArrayList<PieceLocationDescriptor>();
		PieceLocationDescriptor attacker = makePld(PieceType.MARSHAL, PlayerColor.RED, 4,5);
		PieceLocationDescriptor other = makePld(PieceType.FLAG, PlayerColor.RED, 0,0);
		PieceLocationDescriptor defender = makePld(PieceType.FLAG, PlayerColor.BLUE, 5,5);
		configuration.add(attacker);
		configuration.add(other);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.MARSHAL, PlayerColor.RED, 5,5), result.getBattleWinner());
		assertEquals(MoveResultStatus.RED_WINS,result.getStatus());
	}
	
	@Test
	public void testCaptureFirstBlueFlag() throws StrategyException{
		configuration = new ArrayList<PieceLocationDescriptor>();
		PieceLocationDescriptor attacker = makePld(PieceType.MARSHAL, PlayerColor.RED, 4,5);
		PieceLocationDescriptor other = makePld(PieceType.FLAG, PlayerColor.RED, 0,0);
		PieceLocationDescriptor other2 = makePld(PieceType.FLAG, PlayerColor.BLUE, 0,1);
		PieceLocationDescriptor defender = makePld(PieceType.FLAG, PlayerColor.BLUE, 5,5);
		configuration.add(attacker);
		configuration.add(other);
		configuration.add(other2);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.MARSHAL, PlayerColor.RED, 5,5), result.getBattleWinner());
		assertEquals(MoveResultStatus.FLAG_CAPTURED,result.getStatus());
	}
	
	@Test
	public void testCaptureLastRedFlag() throws StrategyException{
		configuration = new ArrayList<PieceLocationDescriptor>();
		PieceLocationDescriptor attacker = makePld(PieceType.MARSHAL, PlayerColor.BLUE, 4,5);
		PieceLocationDescriptor other = makePld(PieceType.FLAG, PlayerColor.BLUE, 0,0);
		PieceLocationDescriptor defender = makePld(PieceType.FLAG, PlayerColor.RED, 5,5);
		configuration.add(attacker);
		configuration.add(other);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.MARSHAL, PlayerColor.BLUE, 5,5), result.getBattleWinner());
		assertEquals(MoveResultStatus.BLUE_WINS,result.getStatus());
	}
	
	@Test
	public void testCaptureFirstRedFlag() throws StrategyException{
		configuration = new ArrayList<PieceLocationDescriptor>();
		PieceLocationDescriptor attacker = makePld(PieceType.MARSHAL, PlayerColor.BLUE, 4,5);
		PieceLocationDescriptor other = makePld(PieceType.FLAG, PlayerColor.BLUE, 0,0);
		PieceLocationDescriptor other2 = makePld(PieceType.FLAG, PlayerColor.RED, 0,1);
		PieceLocationDescriptor defender = makePld(PieceType.FLAG, PlayerColor.RED, 5,5);
		configuration.add(attacker);
		configuration.add(other);
		configuration.add(other2);
		configuration.add(defender);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
		assertEquals(makePld(PieceType.MARSHAL, PlayerColor.BLUE, 5,5), result.getBattleWinner());
		assertEquals(MoveResultStatus.FLAG_CAPTURED,result.getStatus());
	}
	
	@Test(expected = StrategyException.class)
	public void testPieceTriesToAttackLikeFL() throws StrategyException{
		PieceLocationDescriptor attacker = makePld(PieceType.MARSHAL, PlayerColor.RED, 4,5);
		PieceLocationDescriptor defender = makePld(PieceType.MINER, PlayerColor.BLUE, 6,5);
		
		MoveResult result = striker.strikeMove(configuration, attacker, defender);
	}
	
	@Test
	public void testCombatOutcome() throws StrategyException{
		//GammaMovementRules movementRules = new GammaMovementRules(new GammaMovementValidationStrategy());
		EpsilonStrikeStrategy movementRules = new EpsilonStrikeStrategy();
		assertEquals(StrikeResultBeta.DRAW,movementRules.combatResult(PieceType.MARSHAL,PieceType.MARSHAL));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,movementRules.combatResult(PieceType.LIEUTENANT,PieceType.SERGEANT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,movementRules.combatResult(PieceType.COLONEL,PieceType.GENERAL));
		assertEquals(StrikeResultBeta.DRAW,movementRules.combatResult(PieceType.LIEUTENANT,PieceType.LIEUTENANT));
		assertEquals(StrikeResultBeta.DRAW,movementRules.combatResult(PieceType.FIRST_LIEUTENANT,PieceType.LIEUTENANT));

		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.MAJOR,PieceType.COLONEL));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.CAPTAIN,PieceType.MAJOR));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.LIEUTENANT,PieceType.CAPTAIN));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.SERGEANT,PieceType.LIEUTENANT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.MINER,PieceType.SERGEANT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.SCOUT,PieceType.MINER));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.SPY,PieceType.SCOUT));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.BOMB,PieceType.SPY));

		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.COLONEL,PieceType.MAJOR));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.MAJOR,PieceType.CAPTAIN));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.CAPTAIN,PieceType.LIEUTENANT));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.LIEUTENANT,PieceType.SERGEANT));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.SERGEANT,PieceType.MINER));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.MINER,PieceType.SCOUT));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.SCOUT,PieceType.SPY));
		assertEquals(StrikeResultBeta.ATTACKER_LOSES,
				movementRules.combatResult(PieceType.SPY,PieceType.BOMB));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.SPY,PieceType.MARSHAL));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.MARSHAL,PieceType.SPY));

		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.COLONEL,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.MAJOR,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.CAPTAIN,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.LIEUTENANT,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.SERGEANT,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.MINER,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.SCOUT,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.SPY,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS,
				movementRules.combatResult(PieceType.SPY,PieceType.FLAG));
		assertEquals(StrikeResultBeta.ATTACKER_WINS, 
				movementRules.combatResult(PieceType.MARSHAL,PieceType.FLAG));

	}
}
