package strategy.game.version.gamma;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.StrikeStrategy;
import strategy.game.version.beta.StrikeResultBeta;

public class GammaStrikeStrategy implements StrikeStrategy {

	@Override
	public MoveResult strikeMove(
			Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor attacker, PieceLocationDescriptor defender)
			throws StrategyException {
		// TODO Auto-generated method stub
		final MoveResult result;
		final StrikeResultBeta strikeResult = combatResult(attacker.getPiece().getType(), defender.getPiece().getType());
		if(attacker.getLocation().distanceTo(defender.getLocation()) != 1){
			throw new StrategyException("Cannot strike from more than one space away!");
		}
		if(strikeResult == StrikeResultBeta.DRAW){
			configuration.remove(defender);
			configuration.remove(attacker);
			result = new MoveResult(MoveResultStatus.OK, null); // TODO Is this correct BattleWinner info for draw?
		}
		else if(strikeResult == StrikeResultBeta.ATTACKER_WINS){
			configuration.remove(defender);
			normalMove(configuration, attacker, defender.getLocation());
			if(defender.getPiece().getType() == PieceType.FLAG){
				if(attacker.getPiece().getOwner() == PlayerColor.BLUE){
					result = new MoveResult(MoveResultStatus.BLUE_WINS, attacker);
				}
				else{
					result = new MoveResult(MoveResultStatus.RED_WINS, attacker);
				}
			}
			else{
				configuration.remove(defender);
				configuration.remove(attacker);
				final PieceLocationDescriptor newAttacker = new PieceLocationDescriptor(attacker.getPiece(), defender.getLocation());
				configuration.add(newAttacker);
				result = new MoveResult(MoveResultStatus.OK, newAttacker);
			}
		}
		else{ // Attacker loses
			configuration.remove(defender);
			configuration.remove(attacker);
			final PieceLocationDescriptor newDefender = new PieceLocationDescriptor(defender.getPiece(), attacker.getLocation());
			configuration.add(newDefender);
			result = new MoveResult(MoveResultStatus.OK, newDefender);
		}
		return result;
	}
	
	/**
	 * 
	 * @param attacker the piece attacking
	 * @param defender the piece defending
	 * @return who won the combat
	 */
	protected StrikeResultBeta combatResult(PieceType attacker, PieceType defender){
		if(attacker!=PieceType.MINER && defender==PieceType.BOMB) return StrikeResultBeta.ATTACKER_LOSES;
		if(attacker==PieceType.SPY && defender==PieceType.MARSHAL) return StrikeResultBeta.ATTACKER_WINS;
		if(rank(attacker)==rank(defender)) return StrikeResultBeta.DRAW;
		if(rank(attacker)>rank(defender)) return StrikeResultBeta.ATTACKER_WINS;

		return StrikeResultBeta.ATTACKER_LOSES;
	}
	private int rank(PieceType piece){
		if(piece==PieceType.MARSHAL){
			return 12;
		}else if(piece==PieceType.GENERAL){
			return 11;
		}else if(piece==PieceType.COLONEL){
			return 10;
		}else if(piece==PieceType.MAJOR){
			return 9;
		}else if(piece==PieceType.CAPTAIN){
			return 8;
		}else if(piece==PieceType.LIEUTENANT){
			return 7;
		}else if(piece==PieceType.SERGEANT){
			return 6;
		}else if(piece==PieceType.MINER){
			return 5;
		}else if(piece==PieceType.SCOUT){
			return 4;
		}else if(piece==PieceType.SPY){
			return 3;
		}else if(piece==PieceType.BOMB){
			return 2;
		}else{ //if(piece==PieceType.FLAG){
			return 1;
		} 
		//return 0;
	}
	
	private MoveResult normalMove(Collection<PieceLocationDescriptor> configuration, PieceLocationDescriptor pl, Location to){		
		// Remove old PieceLocationDescriptor (for from) and add new one (for to)
		configuration.remove(pl);
		configuration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		return new MoveResult(MoveResultStatus.OK, null);
	}
}
