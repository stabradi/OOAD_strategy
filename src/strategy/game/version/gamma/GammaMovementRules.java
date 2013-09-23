package strategy.game.version.gamma;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementRules;
import strategy.game.version.StrikeRules;
import strategy.game.version.beta.StrikeResultBeta;

public class GammaMovementRules implements MovementRules {
	public MoveResult move(Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
			throws StrategyException{
		/*
		movementRules.validateMove(this, currentConfiguration, fromPl, betaFrom, betaTo);
		
		// Check for a strike
		final PieceLocationDescriptor toPl = getPlDescriptorAt(betaTo);
		MoveResult moveResult;
		if(movementRules.isStrike(currentConfiguration, fromPl, toPl)){
			moveResult = movementRulesRules.strikeMove(currentConfiguration, fromPl, toPl);
		}
		else{
			moveResult = movementRules.normalMove(fromPl, betaTo);
		}
		if((moveCounter >= 12) && moveResult.getStatus() == MoveResultStatus.OK){
			moveResult = new MoveResult(MoveResultStatus.DRAW, moveResult.getBattleWinner());
		}
		*/
		return null;
	}
	
	public void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
			throws StrategyException {
		if(!locationIsOnBoard(to)){
			throw new StrategyException("Cannot move piece off of board");
		}
		if((controller.getPieceAt(to) != null) && (controller.getPieceAt(to).getOwner() == pl.getPiece().getOwner())){
			throw new StrategyException("Cannot move piece into another piece belonging to the same player");
		}
		if(from.distanceTo(to) != 1){
			throw new StrategyException("Must move piece exactly one space orthogonally");
		}
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
	}

	/*
	 * Verifies that a position is in fact on the board
	 */
	private boolean locationIsOnBoard(Location to){
		final int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		final int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		return ((xcoord >= 0) && (ycoord >= 0) && (xcoord <= 5) && (ycoord <= 5));
	}
	
	
	/*
	 * Helper for move(), updates, the configuration for moves not involving strikes
	 */
	private MoveResult normalMove(PieceLocationDescriptor pl, Location to){		
		// Remove old PieceLocationDescriptor (for from) and add new one (for to)
		currentConfiguration.remove(pl);
		currentConfiguration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		return new MoveResult(MoveResultStatus.OK, null);
	}
	
	public class GammaStrikeRules implements StrikeRules {
		@Override
		public MoveResult strikeMove(
				Collection<PieceLocationDescriptor> configuration,
				PieceLocationDescriptor fromPl, PieceLocationDescriptor toPl) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isStrike(Collection<PieceLocationDescriptor> configuration,
				PieceLocationDescriptor fromPl, PieceLocationDescriptor toPl) {
			// TODO Auto-generated method stub
			return false;
		}

		/*
		 * Helper for move(), updates the configurations for moves involving strikes
		 */
		private MoveResult strikeMove(PieceLocationDescriptor attacker, PieceLocationDescriptor defender){
			final MoveResult result;
			final StrikeResultBeta strikeResult = combatResult(attacker.getPiece().getType(), defender.getPiece().getType());
			if(strikeResult == StrikeResultBeta.DRAW){
				currentConfiguration.remove(defender);
				currentConfiguration.remove(attacker);
				result = new MoveResult(MoveResultStatus.OK, null); // TODO Is this correct BattleWinner info for draw?
			}
			else if(strikeResult == StrikeResultBeta.ATTACKER_WINS){
				currentConfiguration.remove(defender);
				normalMove(attacker, defender.getLocation());
				if(defender.getPiece().getType() == PieceType.FLAG){
					if(attacker.getPiece().getOwner() == PlayerColor.BLUE){
						result = new MoveResult(MoveResultStatus.BLUE_WINS, attacker);
					}
					else{
						result = new MoveResult(MoveResultStatus.RED_WINS, attacker);
					}
				}
				else{
					result = new MoveResult(MoveResultStatus.OK, attacker);
				}
			}
			else{ // Attacker loses
				currentConfiguration.remove(attacker);
				normalMove(defender, attacker.getLocation());
				result = new MoveResult(MoveResultStatus.OK, defender);
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
	}
}
