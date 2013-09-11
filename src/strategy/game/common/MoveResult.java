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
 * This class contains the complete results of a move.
 * 
 * @author gpollice
 * @version Sep 7, 2013
 */
public class MoveResult
{
	private final MoveResultStatus status;
	private final PieceLocationDescriptor battleWinner;
	
	/**
	 * Constructor that sets the properties.
	 * @param status the move result status
	 * @param battleWinner if there were a strike, this contains the information
	 * 		about the winner.
	 */
	public MoveResult(MoveResultStatus status, PieceLocationDescriptor battleWinner)
	{
		this.status = status;
		this.battleWinner = battleWinner;
	}

	/**
	 * @return the status
	 */
	public MoveResultStatus getStatus()
	{
		return status;
	}

	/**
	 * @return the battleWinner
	 */
	public PieceLocationDescriptor getBattleWinner()
	{
		return battleWinner;
	}
}
