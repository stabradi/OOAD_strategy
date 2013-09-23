package strategy.game.common;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.StrategyGameController;

public interface MovementRules {
	
	
	/**
	 * Validates that a move is valid, and throws an exception if not
	 * @param configuration Board configuration to validate the move in
	 * @param pl The PieceLocationDescriptor associated with the piece doing the moving.
	 * @param from The source location
	 * @param to The destination location
	 */
	public void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
			PieceLocationDescriptor pl, Location from, Location to)
			throws StrategyException;
}
