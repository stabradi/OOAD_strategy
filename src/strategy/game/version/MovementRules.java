package strategy.game.version;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.PieceLocationDescriptor;

public interface MovementRules {
	
	
	/**
	 * Validates that a move is valid, and throws an exception if not
	 * @param configuration Board configuration to validate the move in
	 * @param pl The PieceLocationDescriptor associated with the piece doing the moving.
	 * @param from The source location
	 * @param to The destination location
	 */
	public MoveResult move(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
			throws StrategyException;
}
