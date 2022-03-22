package hu.nye.jatekelmelet.connect4;

import hu.nye.jatekelmelet.connect4.controller.Game;
import hu.nye.jatekelmelet.connect4.controller.Match;
import hu.nye.jatekelmelet.connect4.model.Board;
import hu.nye.jatekelmelet.connect4.model.Color;
import hu.nye.jatekelmelet.connect4.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Connect4 implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Connect4.class);

    @Override
    public void run(String... args) {
        LOG.info("STARTING ConnectFour");

        int boardRowSize = 6;
        int boardColumnSize = 7;

        //Player pHuman = new Player("Human Player", Color.BLACK, "human");
        Player pMinimax = new Player("Minimax Player",Color.BLACK, "minimax");
        Player oMinimax = new Player("Minimax Opponent", Color.WHITE, "minimax");

        Board connect4Board = new Board(boardRowSize, boardColumnSize);

        Game game;
        Match match;

        game = new Game(connect4Board, pMinimax, oMinimax);
        match = new Match(game);
        match.play();
        System.out.println(match);

        LOG.info("GAME OVER");
    }
}
