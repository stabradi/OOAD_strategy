/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package strategy.game.reporter;

import java.util.ArrayList;
import java.util.Collection;



import strategy.common.StrategyException;
import strategy.game.common.Coordinate;
import strategy.game.common.Location;
import strategy.game.common.Location2D;
import strategy.game.common.MoveResult;
import strategy.game.common.MoveResultStatus;
import strategy.game.common.PieceLocationDescriptor;
import strategy.game.common.PieceType;
import strategy.game.common.StrategyGameObserver;
import strategy.game.version.beta.BetaLocation2D;


/**
 * @author stabradi
 * @version 1
 */
public class StrategyGameReporter implements StrategyGameObserver {
	private Collection<PieceLocationDescriptor> currentConfiguration;
	private boolean redsMove;

	@Override
	public void gameStart(Collection<PieceLocationDescriptor> redConfiguration,
			Collection<PieceLocationDescriptor> blueConfiguration) {
		currentConfiguration = new ArrayList<PieceLocationDescriptor>();
		currentConfiguration.addAll(redConfiguration);
		currentConfiguration.addAll(blueConfiguration);

		redsMove = true;
	}

	@Override
	public void moveHappened(PieceType piece, Location from, Location to,
			MoveResult moveResults, StrategyException fault) {
		if(fault != null){
			System.out.print("move error: " + fault.getMessage() + "\n");
		}else{
			final PieceLocationDescriptor attacker = getPlDescriptorAt(from);
			final PieceLocationDescriptor defender = getPlDescriptorAt(to);
			currentConfiguration.remove(defender);
			currentConfiguration.remove(attacker);
			final PieceLocationDescriptor victor = moveResults.getBattleWinner();
			if(victor!=null){
				currentConfiguration.add(victor);//<--
			}else if(defender == null){
				currentConfiguration.add(new PieceLocationDescriptor(attacker.getPiece(),to));
			}
			
			if((moveResults.getStatus() == MoveResultStatus.OK) || moveResults.getStatus() == MoveResultStatus.FLAG_CAPTURED){
				System.out.print("\n=========================================\n");
				System.out.print("               " + whosMove() + "'s move\n");
				System.out.print("=========================================\n\n");
			}else if(moveResults.getStatus() == MoveResultStatus.BLUE_WINS){//<--
				System.out.print("Game over: Blue Wins\nfinal board:\n\n");
			}else if(moveResults.getStatus() == MoveResultStatus.RED_WINS){//<--
				System.out.print("Game over: Red Wins\nfinal board:\n\n");
			}else if(moveResults.getStatus() == MoveResultStatus.DRAW){
				System.out.print("Game over: Draw\nfinal board:\n\n");
			}
			
			printBoard();
		}

	}
	
	private String whosMove(){
		redsMove = !redsMove;
		if(!redsMove){
			return "Red";
		}
		return "Blue";
	}
	
	private void printBoard(){
		PieceLocationDescriptor currentDescriptor;
		String board = "+---+---+---+---+---+---+---+---+---+---+\n";
		for(int y = 0; y<10; y++){
			board += "|";
			for(int x = 0; x<10; x++){
				currentDescriptor = getPlDescriptorAt(new BetaLocation2D(new Location2D(x,y)));
				if(currentDescriptor != null){
					board += fixyfixy(currentDescriptor.getPiece().getType().getSymbol()) + "|";
				}else{
					board += "   |";
				}
			}
			board += "\n+---+---+---+---+---+---+---+---+---+---+\n";
		}
		System.out.print(board);
	}
	private String fixyfixy(String x){
		if(x.length()==1){
			return " "+x+" ";
		}else if(x.length()==2){
			return x+" ";
		}
		return x;
	}
	/*
	 * This method returns the piece on the game board that is associated with the
	 * specified location, or null if there is none
	 */
	private PieceLocationDescriptor getPlDescriptorAt(Location location){
		final PieceLocationDescriptor pl;
		pl = getPlDescriptorAtFromConfig(location, currentConfiguration);
		return pl;
	}
	
	/**
	 * Gets PieceLocationDescriptor descriptor from only a given configuration at a given Location
	 * or null if there is none.
	 * @param location Location to look for an associated PieceLocationDescriptor
	 * @param config Configuration to search for with given Location
	 * @return A PieceLocationDescriptor with the given Location or null if there is none
	 */
	private PieceLocationDescriptor getPlDescriptorAtFromConfig(Location location, Collection<PieceLocationDescriptor> config){
		Location plLoc = null;
		for(PieceLocationDescriptor pl: config){
			config.remove(null);
			plLoc = pl.getLocation();
			if(plLoc.getCoordinate(Coordinate.X_COORDINATE) == location.getCoordinate(Coordinate.X_COORDINATE) && 
					plLoc.getCoordinate(Coordinate.Y_COORDINATE) == location.getCoordinate(Coordinate.Y_COORDINATE)){
				return pl;
			}
		}
		return null;
	}

}
