package strategy.game.version.gamma;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.common.StrategyRuntimeException;
import strategy.game.StrategyGameController;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.Piece;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovementRules;
import strategy.game.version.beta.StrikeResultBeta;

public class GammaMovementRules implements MovementRules {
	int moveCounter;

	public GammaMovementRules(){
		moveCounter = 1;
	}

	public MoveResult move(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
					throws StrategyException{
		validateMove(controller, configuration, pl, from, to);

		// Check for a strike
		final PieceLocationDescriptor toPl = getPlDescriptorAtFromConfig(to, configuration);
		MoveResult moveResult;
		if((toPl != null) && (toPl.getPiece().getOwner() != pl.getPiece().getOwner())){
			moveResult = strikeMove(configuration, pl, toPl);
		}
		else{
			moveResult = normalMove(configuration, pl, to);
		}
		if((moveCounter >= 12) && moveResult.getStatus() == MoveResultStatus.OK){
			moveResult = new MoveResult(MoveResultStatus.DRAW, moveResult.getBattleWinner());
		}
		moveCounter++;
		return moveResult;
	}

	public void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
					throws StrategyException {
		if(!locationIsOnBoard(to)){
			throw new StrategyException("Cannot move piece off of board");
		}
		Piece toPiece = controller.getPieceAt(to);
		if(controller.getPieceAt(to) != null){
			if(toPiece.getOwner() == pl.getPiece().getOwner()){
				throw new StrategyException("Cannot move piece into another piece belonging to the same player");
			}
		}
		if(from.distanceTo(to) != 1){
			throw new StrategyException("Must move piece exactly one space orthogonally");
		}
		if(pl.getPiece().getType() == PieceType.FLAG){
			throw new StrategyException("Cannot move the flag!");
		}
		int xcoord = to.getCoordinate(Coordinate.X_COORDINATE);
		int ycoord = to.getCoordinate(Coordinate.Y_COORDINATE);
		if(((xcoord == 2) || (xcoord == 3)) &&
			((ycoord == 2) || (ycoord == 3))){
				System.out.println("coords: " + xcoord+ " " + ycoord);
				throw new StrategyException("Cannot move into lake!");
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
	private MoveResult normalMove(Collection<PieceLocationDescriptor> configuration, PieceLocationDescriptor pl, Location to){		
		// Remove old PieceLocationDescriptor (for from) and add new one (for to)
		configuration.remove(pl);
		configuration.add(new PieceLocationDescriptor(pl.getPiece(), to));
		return new MoveResult(MoveResultStatus.OK, null);
	}

	/*
	 * Helper for move(), updates the configurations for moves involving strikes
	 */
	private MoveResult strikeMove(Collection<PieceLocationDescriptor> configuration, PieceLocationDescriptor attacker, PieceLocationDescriptor defender){
		final MoveResult result;
		final StrikeResultBeta strikeResult = combatResult(attacker.getPiece().getType(), defender.getPiece().getType());
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
				PieceLocationDescriptor newAttacker = new PieceLocationDescriptor(attacker.getPiece(), defender.getLocation());
				configuration.add(newAttacker);
				result = new MoveResult(MoveResultStatus.OK, newAttacker);
			}
		}
		else{ // Attacker loses
			configuration.remove(defender);
			configuration.remove(attacker);
			PieceLocationDescriptor newDefender = new PieceLocationDescriptor(defender.getPiece(), attacker.getLocation());
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

	/**
	 * Gets PieceLocationDescriptor descriptor from only a given configuration at a given Location
	 * or null if there is none.
	 * @param location Location to look for an associated PieceLocationDescriptor
	 * @param config Configuration to search for with given Location
	 * @return A PieceLocationDescriptor with the given Location or null if there is none
	 */
	protected PieceLocationDescriptor getPlDescriptorAtFromConfig(Location location, Collection<PieceLocationDescriptor> config){
		Location plLoc = null;
		for(PieceLocationDescriptor pl: config){
			plLoc = pl.getLocation();
			if(plLoc.getCoordinate(Coordinate.X_COORDINATE) == location.getCoordinate(Coordinate.X_COORDINATE) && 
					plLoc.getCoordinate(Coordinate.Y_COORDINATE) == location.getCoordinate(Coordinate.Y_COORDINATE)){
				return pl;
			}
		}
		return null;
	}
}
