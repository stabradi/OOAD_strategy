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


/**
 * Description
 * @author gpollice
 * @version Sep 7, 2013
 */
public class Location2D implements Location
{
	private final int xCoordinate;
	private final int yCoordinate;
	
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

	/**
	 * Returns the Manhattan distance between two locations
	 * @param otherLocation the location to get the distance from this location
	 * @return the distance to the other location
	 */
	@Override
	public int distanceTo(Location otherLocation)
	{
		int xDistance = Math.abs(xCoordinate - otherLocation.getCoordinate(Coordinate.X_COORDINATE));
		int yDistance = Math.abs(yCoordinate - otherLocation.getCoordinate(Coordinate.Y_COORDINATE));
		return xDistance + yDistance;
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
