package hu.nye.jatekelmelet.connect4.controller;

import hu.nye.jatekelmelet.connect4.model.Board;
import hu.nye.jatekelmelet.connect4.model.Player;
import lombok.Getter;

@Getter
public class Game {
	
	private final Board board;
	private final Player p1;
	private final Player p2;
	
	public Game(Board board, Player p1, Player p2){
		if(p1.getSymbol().equals(p2.getSymbol())) throw new IllegalArgumentException("Both Players cannot have the same symbol");
		
		this.board = board;
		this.p1 = p1;
		this.p2 = p2;
	}

	public Player play(){
		Player currPlayer;
		Player currOpponent;
		
		int i = 0;
		while(!board.isBoardFull()){

			int r = i % 2;
			currPlayer = (r == 0) ? p1 : p2;
			currOpponent = (r == 0) ? p2 : p1;
			i++;

			int col = move(currPlayer, currOpponent);

			board.addSymbol(col, currPlayer.getSymbol());

			System.out.println(board);

			String win = board.getConnect4(p1, p2);
			Player winner = getWinner(win);
			if(winner != null){
				return winner;
			}
		}
		
		return null;
	}

	public int move(Player player, Player opponent){

		int col;

		switch(player.getType()){
			case "human":
				col = Search.humanMove(board, player);
				break;
			case "heuristic":
				col = Search.nextBestMove(board, player, opponent);
				break;
			case "minimax":
				col = Search.minimax(board, player, opponent);
				break;
			default:
				throw new IllegalArgumentException("Invalid Player Type");
		}

		return col;
	}

	private Player getWinner(String winningSymbol){
		if(winningSymbol.equals(p1.getSymbol())){
			return p1;
		}
		if(winningSymbol.equals(p2.getSymbol())){
			return p2;
		}
		return null;
	}

}
