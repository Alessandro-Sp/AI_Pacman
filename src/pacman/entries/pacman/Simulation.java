package pacman.entries.pacman;

import pacman.controllers.examples.RandomGhosts;
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

        game.advanceGame(move, new RandomGhosts().getMove());

        node.getState().setScore(game.getScore());

        return node;
    }

}
