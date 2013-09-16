/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package strategy.game.version.beta;

import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;

/**@version 9/15/2013
 * @author Chris Nota
 */
public class BetaLocation2D extends Location2D {
	private final int xCoordinate;
	private final int yCoordinate;
	
	/**
	 * Constructor that sets the properties.
	 * @param loc the Location to use
	 */
	public BetaLocation2D(Location loc){
		super(loc.getCoordinate(Coordinate.X_COORDINATE), loc.getCoordinate(Coordinate.Y_COORDINATE));
		xCoordinate = loc.getCoordinate(Coordinate.X_COORDINATE);
		yCoordinate = loc.getCoordinate(Coordinate.Y_COORDINATE);
	}
	
	@Override
	public int distanceTo(Location otherLocation){
		final int xDistance = Math.abs(otherLocation.getCoordinate(Coordinate.X_COORDINATE) - xCoordinate);
		final int yDistance = Math.abs(otherLocation.getCoordinate(Coordinate.Y_COORDINATE) - yCoordinate);
		return xDistance + yDistance;
	}
}
