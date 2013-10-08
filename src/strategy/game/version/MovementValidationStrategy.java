package strategy.game.version;

import java.util.Collection;

import strategy.common.StrategyException;
import strategy.game.StrategyGameController;
import strategy.game.common.Location;
import strategy.game.common.PieceLocationDescriptor;

public interface MovementValidationStrategy {
	/**
	 * Validates that a given move is valid
	 * @param controller StrategyGameController the move is being performed from
	 * @param configuration Board configuration
	 * @param pl associated with the piece doing the moving
	 * @param from The source location
	 * @param to The destination location
	 */
	void validateMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration,
	PieceLocationDescriptor pl, Location from, Location to) throws StrategyException;
}
