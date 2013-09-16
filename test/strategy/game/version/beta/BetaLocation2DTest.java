package strategy.game.version.beta;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import strategy.common.StrategyException;
import strategy.game.common.Location;
import strategy.game.common.Location2D;

public class BetaLocation2DTest {
	@Test
	public void testDistanceTo() throws StrategyException{
		Location loc_0_0 = new BetaLocation2D(new Location2D(0,0));
		Location loc_0_1 = new BetaLocation2D(new Location2D(0,1));
		Location loc_1_0 = new BetaLocation2D(new Location2D(1,0));
		Location loc_1_1 = new BetaLocation2D(new Location2D(1,1));
		Location loc_10_0 = new BetaLocation2D(new Location2D(10,0));
		Location loc_neg5_neg6 = new BetaLocation2D(new Location2D(-5,-6));

		assertEquals(0, loc_0_0.distanceTo(loc_0_0));
		assertEquals(1, loc_0_0.distanceTo(loc_0_1));
		assertEquals(1, loc_0_0.distanceTo(loc_1_0));
		assertEquals(2, loc_0_0.distanceTo(loc_1_1));
		assertEquals(11, loc_0_1.distanceTo(loc_10_0));
		assertEquals(12, loc_0_1.distanceTo(loc_neg5_neg6));
		assertEquals(12, loc_neg5_neg6.distanceTo(loc_0_1));
		assertEquals(21, loc_neg5_neg6.distanceTo(loc_10_0));
	}
}
