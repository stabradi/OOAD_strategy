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
 * This enumeration defines the various piece types that are used in
 * the versions of Strategy.
 * @author gpollice
 * @version Aug 31, 2013
 */
public enum PieceType
{
	MARSHAL("Marshal", "mar"),
	GENERAL("General", "gen"),
	COLONEL("Colonel", "col"),
	MAJOR("Major", "maj"),
	CAPTAIN("Captain", "cpt"),
	LIEUTENANT("Lieutenant", "lt"),
	SERGEANT("Sergeant", "sgt"),
	MINER("Miner", "mnr"),
	SCOUT("Scout", "sct"),
	SPY("Spy", "spy"),
	BOMB("Bomb", "b"),
	FLAG("Flag", "f");
	
	private final String printableName;
	private final String symbol;
	
	/**
	 * The constructor for each enumerable item sets up the state so that
	 * the symbol for each item and the printable name are set up.
	 * 
	 * @param printableName the value returned from toString
	 * @param symbol a one character string that can be used when printing the board.
	 */
	private PieceType(String printableName, String symbol)
	{
		this.printableName = printableName;
		this.symbol = symbol;
	}

	/**
	 * @return the printableName
	 */
	public String getPrintableName()
	{
		return printableName;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol()
	{
		return symbol;
	}
	
	@Override
	public String toString()
	{
		return printableName;
	}
}
