/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package strategy.game.version.epsilon;

import java.util.Collection;

import strategy.common.PlayerColor;
import strategy.common.StrategyException;
import strategy.game.common.Coordinate;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.version.delta.DeltaPlacementRules;

/**
 * Placement rules for delta strategy (see developer's guide)
 * @author stabradi
 * @version 10/8/2013
 */
public class EpsilonPlacementRules extends DeltaPlacementRules { 
	@Override
	/**
	 * @param config the set of pieces to check for correct pieces
	
	 * @return number code indicating in what way the number of pieces is wrong,
	 *  or 0 if it is not wrong, this is nicer than a boolean for debugging */
	protected int checkNumberOfPieces(Collection<PieceLocationDescriptor> config){
		int marshal = 1;
		int general = 1;//
		int colonels = 2;
		int majors = 3;//
		int captains = 4;
		int lieutanants = 2;
		int firstLieutenants = 2;
		int sergeant = 4;
		int miners = 5;//
		int scouts = 8;//
		int spys = 1;//
		int bombs = 6;//
		int flag = 2;
		
		for(PieceLocationDescriptor pl: config){
			if(pl.getPiece().getType()==PieceType.FLAG){
				if((--flag)<0)return -1;
			}else if(pl.getPiece().getType()==PieceType.MARSHAL){
				if((--marshal)<0)return -2;
			}else if(pl.getPiece().getType()==PieceType.COLONEL){
				if((--colonels)<0)return -3;
			}else if(pl.getPiece().getType()==PieceType.CAPTAIN){
				if((--captains)<0)return -4;
			}else if(pl.getPiece().getType()==PieceType.LIEUTENANT){
				if((--lieutanants)<0)return -5;
			}else if(pl.getPiece().getType()==PieceType.SERGEANT){
				if((--sergeant)<0)return -6;
			}else if(pl.getPiece().getType()==PieceType.GENERAL){
				if((--general)<0)return -7;
			}else if(pl.getPiece().getType()==PieceType.MAJOR){
				if((--majors)<0)return -8;
			}else if(pl.getPiece().getType()==PieceType.MINER){
				if((--miners)<0)return -9;
			}else if(pl.getPiece().getType()==PieceType.SCOUT){
				if((--scouts)<0)return -10;
			}else if(pl.getPiece().getType()==PieceType.SPY){
				if((--spys)<0)return -11;
			}else if(pl.getPiece().getType()==PieceType.BOMB){
				if((--bombs)<0)return -12;
			}else if(pl.getPiece().getType()==PieceType.FIRST_LIEUTENANT){
				if((--lieutanants)<0)return -13;
			}else{
				return -1;
			}
		}
		if(flag!=0){
			return -1;
		}
		return (marshal+general+colonels+majors+captains+lieutanants+firstLieutenants+sergeant+miners+scouts+spys+bombs+flag-1);//==0;
	}
}
