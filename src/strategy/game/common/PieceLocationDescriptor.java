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
 * This class is used to identify pieces and their associated locations. These
 * are used for setting up initial game configurations. They may be used for 
 * other purposes by various game implementations, but the primary purpose is for
 * describing initial configurations.
 * 
 * @author gpollice
 * @version Sep 7, 2013
 */
public class PieceLocationDescriptor
{
	private final Piece piece;
	private final Location location;
	
	/**
	 * Construct a PieceLocationDescriptor where we do not care about the
	 * player who owns the piece.
	 * @param piece the piece at the location
	 * @param location the location of the piece
	 */
	public PieceLocationDescriptor(Piece piece, Location location)
	{
		this.piece = piece;
		this.location = location;
	}

	/**
	 * @return the location
	 */
	public Location getLocation()
	{
		return location;
	}

	/**
	 * @return the piece
	 */
	public Piece getPiece()
	{
		return piece;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (!(other instanceof PieceLocationDescriptor)) return false;
		final PieceLocationDescriptor that = (PieceLocationDescriptor)other;
		return (piece.equals(that.piece) && location.equals(that.location));
	}
	
	@Override
	public int hashCode()
	{
		return piece.hashCode() * location.hashCode();
	}
}
