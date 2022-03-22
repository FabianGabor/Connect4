package hu.nye.jatekelmelet.connect4.controller;

import hu.nye.jatekelmelet.connect4.model.Player;
import lombok.Getter;

@Getter
public class Match {

	private final Game game;
	
	public Match(Game game){
		this.game = game;
	}
	
	public void play(){
		Player winner = game.play();
		int totalMoves = game.getBoard().getNumMoves();

		if (winner != null) {
			System.out.println(winner.getName() + " won in " + totalMoves + " moves!");
		} else {
			System.out.println("Tie Game.");
		}
	}

	public String toString(){
		String s = "------Match Results------\n";
		s += game.getP1();
		s += game.getP2();

		return s;
	}
}
