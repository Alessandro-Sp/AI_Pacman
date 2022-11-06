package pacman.entries.pacman;

import pacman.controllers.examples.Legacy;
import pacman.game.Constants;
import pacman.game.Game;

public class Simulation {

    /**
     * This method runs the simulation in order to find Ms Pac-man next move
     * @param game Game Object
     * @param node Node currently visited
     * @param move random move
     * @return current Node
     */
    public Node run(Game game, Node node, Constants.MOVE move){

        game.advanceGame(move, new Legacy().getMove());
//        game.advanceGame(move, new RandomGhosts().getMove());

        if (game.wasPacManEaten()) {
            node.getState().setWasPacManEaten(game.wasPacManEaten());
            return node;
        }

        node.getState().addScore(game.getScore());
        return node;
    }

}
