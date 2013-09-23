/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.common;

import strategy.common.StrategyRuntimeException;

/**
 * Description
 * @author gpollice
 * @version Sep 7, 2013
 */
public class Location2D implements Location
{
	private final int xCoordinate;
	private final int yCoordinate;
	
	/**
	 * Constructor for Location2D.
	 * @param xCoordinate int
	 * @param yCoordinate int
	 */
	public Location2D(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	
	@Override
	public int getCoordinate(Coordinate coordinate)
	{
		return coordinate == Coordinate.X_COORDINATE ? xCoordinate
				: yCoordinate;
	}

	@Override
	public int distanceTo(Location otherLocation)
	{
		final int otherX =
				otherLocation.getCoordinate(Coordinate.X_COORDINATE);
		final int otherY =
				otherLocation.getCoordinate(Coordinate.Y_COORDINATE);
		if (xCoordinate != otherX && yCoordinate != otherY) {
			throw new StrategyRuntimeException(
					"Coordinates are not on same row or column");
		}
		return xCoordinate == otherX ? Math.abs(yCoordinate-otherY) : Math.abs(xCoordinate - otherX);
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (!(other instanceof Location2D)) return false;
		final Location2D that = (Location2D)other;
		return (xCoordinate == that.xCoordinate &&
				yCoordinate == that.yCoordinate);
	}
	
	@Override
	public int hashCode()
	{
		return (xCoordinate + yCoordinate + 1) * (xCoordinate + 1) * (yCoordinate + 1);
	}
	
	@Override
	public String toString()
	{
		return "(" + xCoordinate + ',' + yCoordinate + ')';
	}
}
