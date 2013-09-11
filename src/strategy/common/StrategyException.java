/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.common;

/**
 * This is the exception that is used for any declared exception in the Strategy software.
 * @author gpollice
 * @version Aug 31, 2013
 */
public class StrategyException extends Exception
{
	/**
	 * Create a message with a message describing the problem.
	 * @param message describes the problem
	 */
	public StrategyException(String message)
	{
		super(message);
	}

	/**
	 * This is an exception that was caused by some other exception that
	 * was caught during the execution of the application.
	 * @param message describes the problem
	 * @param cause the exception that caused this exception to be thrown.
	 */
	public StrategyException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
