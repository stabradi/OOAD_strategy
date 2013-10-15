package strategy.game.version.epsilon;

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

public class EpsilonStrikeStrategy implements StrikeStrategy {

	@Override
	public MoveResult strikeMove(
			Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor attacker, PieceLocationDescriptor defender)
			throws StrategyException {
		final MoveResult result;
		final StrikeResultBeta strikeResult = combatResult(attacker.getPiece().getType(), defender.getPiece().getType());
		if(attacker.getLocation().distanceTo(defender.getLocation()) != 1){
			if(attacker.getPiece().getType() != PieceType.FIRST_LIEUTENANT){
				throw new StrategyException("Cannot strike from more than one space away!");
			}
		}
		if(strikeResult == StrikeResultBeta.DRAW){
			configuration.remove(defender);
			configuration.remove(attacker);
			result = new MoveResult(MoveResultStatus.OK, null);
		}
		else if(strikeResult == StrikeResultBeta.ATTACKER_WINS){
			configuration.remove(defender);
			normalMove(configuration, attacker, defender.getLocation());
			final PieceLocationDescriptor newAttacker = new PieceLocationDescriptor(attacker.getPiece(), defender.getLocation());
			if(defender.getPiece().getType() == PieceType.FLAG){
				if(attacker.getPiece().getOwner() == PlayerColor.BLUE){
					if(!flagsRemaining(configuration, PlayerColor.RED)){
						result = new MoveResult(MoveResultStatus.BLUE_WINS, newAttacker);
					}
					else{
						result = new MoveResult(MoveResultStatus.FLAG_CAPTURED, newAttacker);
					}
				}
				else{
					if(!flagsRemaining(configuration, PlayerColor.BLUE)){
						result = new MoveResult(MoveResultStatus.RED_WINS, newAttacker);
					}
					else{
						result = new MoveResult(MoveResultStatus.FLAG_CAPTURED, newAttacker);
					}
				}
			}
			else{
				result = new MoveResult(MoveResultStatus.OK, newAttacker);
			}
		}
		else{ // Attacker loses
			if((attacker.getPiece().getType() == PieceType.FIRST_LIEUTENANT) && (attacker.getLocation().distanceTo(defender.getLocation()) != 1)){
				configuration.remove(attacker);
				// defender doesn't move if first lieutenant is two spaces away
				result = new MoveResult(MoveResultStatus.OK, defender);
			}
			else{
				configuration.remove(defender);
				configuration.remove(attacker);
				final PieceLocationDescriptor newDefender = new PieceLocationDescriptor(defender.getPiece(), attacker.getLocation());
				configuration.add(newDefender);
				result = new MoveResult(MoveResultStatus.OK, newDefender);
			}
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
		}else if(piece==PieceType.FIRST_LIEUTENANT){
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
	
	private boolean flagsRemaining(Collection<PieceLocationDescriptor> configuration, PlayerColor color){
		boolean flags = false;
		for(PieceLocationDescriptor pl: configuration){
			if((pl.getPiece().getType() == PieceType.FLAG) && (pl.getPiece().getOwner() == color)){
				flags = true;
				break;
			}
		}
		return flags;
	}
}
