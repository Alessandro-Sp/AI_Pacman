package pacman.entries.pacman;

import pacman.controllers.examples.Legacy;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Random;

public class Simulation {

    /**
     * This method runs the simulation in order to find Ms Pac-man next move
     * @param game Game Object
     * @param node Node currently visited
     * @return current Node
     */
    public Node run(Game game, Node node){

        game.advanceGame(node.getState().getMove(), new Legacy().getMove());
//        game.advanceGame(node.getState().getMove(), new RandomGhosts().getMove());
//        game.advanceGame(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getPowerPillIndex(new Random().nextInt(game.getNumberOfActivePills())), Constants.DM.PATH), new Legacy().getMove());

        if (game.wasPacManEaten()) {
            node.getState().setWasPacManEaten(game.wasPacManEaten());
            return node;
        }

        node.getState().addScore(game.getScore());

        node.getState().setNofPills(game.getNumberOfPills());
        return node;
    }

}