/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.version.common;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.game.StrategyGameController;
import strategy.game.common.Location;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.MovePreprocessStrategy;

/** MovePreprocessor to handle resignation.
 * If a player enters null parameters, the opposite player wins.
 * @author Chris
 * @version 10/15/2013
 */
public class ResignationPreprocessor implements MovePreprocessStrategy {

	@Override
	public MoveResult preprocessMove(StrategyGameController controller, Collection<PieceLocationDescriptor> configuration, PlayerColor turn, 
			PieceType piece, Location from, Location to){
		MoveResult result = null;
		if((piece == null) && (from == null) && (to == null)){
			if(turn == PlayerColor.RED){
				result = new MoveResult(MoveResultStatus.BLUE_WINS, null);
			}
			else{
				result = new MoveResult(MoveResultStatus.RED_WINS, null);
			}
		}
		return result;
	}

}
