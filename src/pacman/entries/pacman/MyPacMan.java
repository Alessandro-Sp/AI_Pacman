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
public class MyPacMan extends Controller<MOVE> {
    private MOVE myMove;

    public MOVE getMove(Game game, long timeDue) {
        //game logic

        PacManUtils utils = new PacManUtils();

        //Create Tree
        State state = new State(game.getScore(), 0, game.getPacmanLastMoveMade());
        Node root = new Node(state, null, new ArrayList<>());
        Tree tree = new Tree(root);

        Constants.GHOST closestGhost = utils.getClosestGhost(game);
        Constants.GHOST closestEdibleGhost = utils.getClosestEdibleGhost(game);

        Constants.GHOST secondClosestGhost = utils.getSecondClosestGhost(game);

        double closestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);
        double secondClosestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);


        int closestPillIndex = utils.getClosestPillIndex(game);
        int closestPowerPillIndex = utils.getClosestPowerPillIndex(game);


        //choose next move
        if (game.isJunction(game.getPacmanCurrentNodeIndex())) {

            myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPillIndex, Constants.DM.MANHATTAN);
//            myMove = new NewWay().nextMove2(game, tree);
//            myMove = getNextMove(game.copy(), tree);


            //escape from ghost
            if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance) {
                if (!game.isGhostEdible(closestGhost)) {
//                    myMove = new NewWay().nextMove2(game, tree);
                    myMove = getNextMove(game.copy(), tree);
//                    myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN).opposite();
                }
            }
            //avoid pinch
            if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance) {
                if (null != secondClosestGhost && 25 > secondClosestGhostDistance && -1 != secondClosestGhostDistance) {
                    if (!game.isGhostEdible(closestGhost) && !game.isGhostEdible(secondClosestGhost)) {
                        if (myMove == game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(secondClosestGhost), Constants.DM.MANHATTAN)) {
                            MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
                            MOVE nextPossible = possibleMoves[new Random().nextInt(possibleMoves.length)];
                            while (myMove == nextPossible) {
                                nextPossible = possibleMoves[new Random().nextInt(possibleMoves.length)];
                            }
                        }
                    }
                }
            }
        } else {
            //escape from ghost
            if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance) {
                if (!game.isGhostEdible(closestGhost) || 5 > game.getGhostEdibleTime(closestEdibleGhost)) {
                    return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);
                }
            }

            MOVE[] possibleNext = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
            myMove = possibleNext[new Random().nextInt(possibleNext.length)];
        }

        //eat ghost
        if (utils.isAnyGhostEdible(game)) {
            if (closestGhost == closestEdibleGhost) {
                double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestEdibleGhost), Constants.DM.MANHATTAN);
                if (25 > distance && 15 < game.getGhostEdibleTime(closestEdibleGhost)) {
                    return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestEdibleGhost), Constants.DM.MANHATTAN);
                }
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





