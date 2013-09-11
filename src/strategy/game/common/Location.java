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
 * The Location interface must be implemented by the specific Location 
 * implementations for any Strategy variant. There are implicit assumptions
 * that all coordinates that describe a location are represented as integers and
 * that the distance between two locations is also an integer value.
 * @author gpollice
 * @version Aug 31, 2013
 */
public interface Location
{
	/**
	 * Get the coordinate specified. The expectation is that a runtime exception
	 * will be thrown if a coordinate is requested that is not used for the particular
	 * game.
	 * @param coordinate the requested coordinate
	 * @return the value of the specified coordinate
	 */
	int getCoordinate(Coordinate coordinate);
	
	/**
	 * Return the distance to another location
	 * @param otherLocation the location to get the distance from this location
	 * @return the distance to the other location
	 */
	int distanceTo(Location otherLocation);
}
