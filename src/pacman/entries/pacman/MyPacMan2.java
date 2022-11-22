package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan2 extends Controller<MOVE> {
    private MOVE myMove;

    public MOVE getMove(Game game, long timeDue) {
        //game logic

        PacManUtils utils = new PacManUtils();

        //Create Tree
        State state = new State(game.getScore(), 0, game.getPacmanLastMoveMade());
        Node root = new Node(state, null, new ArrayList<>());
        Tree tree = new Tree(root);

        //closest Ghost
        Constants.GHOST closestGhost = utils.getClosestGhost(game);
        Constants.GHOST closestEdibleGhost = utils.getClosestEdibleGhost(game);
        double closestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);

        //second closest Ghost
        Constants.GHOST secondClosestGhost = utils.getSecondClosestGhost(game);
        double secondClosestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);

        //choose next move
        if (game.isJunction(game.getPacmanCurrentNodeIndex())) {
            myMove = getNextMove(game.copy(), tree);

        } else {//if not a junction
            //keep going straight
            MOVE[] possibleNext = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
            myMove = possibleNext[0];
        }

        //escape from ghost
        if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance && 0 == game.getCurrentLevel()) {// 12
            if (!game.isGhostEdible(closestGhost) || 15 > game.getGhostEdibleTime(closestEdibleGhost)) {// 15
                myMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);
            }
        }


        return myMove;
    }

    /**
     * This method runs the monte-carlo tree search in order to give us the next move
     *
     * @param game Game object
     * @param tree Tree
     * @return Next move that ms Pac-man is going to do
     */
    private MOVE getNextMove(Game game, Tree tree) {
        return new MonteCarloTreeSearch().nextMove(game, tree);
    }

}





