package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Arrays;


/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE> {
    private MOVE myMove = MOVE.LEFT;

    public MOVE getMove(Game game, long timeDue) {
        //game logic

        PacManUtils utils = new PacManUtils();

        //Create Tree
        State state = new State(game.getScore(), 0, game.getPacmanLastMoveMade());
        Node root = new Node(state, null, new ArrayList<>());
        Tree tree = new Tree(root);

        Constants.GHOST closestGhost = utils.getClosestGhost(game);

        double closestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);


        int closestPillIndex = utils.getClosestPillIndex(game);
        int closestPowerPillIndex = utils.getClosestPowerPillIndex(game);


        //choose next move
        if (game.isJunction(game.getPacmanCurrentNodeIndex())) {

            myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPillIndex, Constants.DM.PATH);


            //escape from ghost
            if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance) {
                if (!game.isGhostEdible(closestGhost)) {
                    myMove = getNextMove(game.copy(), tree);
                }
            }
            if (2 == game.getCurrentLevel()){
                if (null != closestGhost && 20 > closestGhostDistance && -1 != closestGhostDistance) {
                    if (!game.isGhostEdible(closestGhost)) {
                        myMove = getNextMove(game.copy(), tree);
                    }
                }
            }
        } else {
            //escape from ghost
            if (null != closestGhost && 9 > closestGhostDistance && -1 != closestGhostDistance) {
                if (!game.isGhostEdible(closestGhost)) {
                    return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);
                }
            }

            //focus on eating pills
            MOVE[] possibleNext = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
            for (MOVE move : possibleNext) {
                if (move != game.getPacmanLastMoveMade()) {
                    myMove = move;
                }
            }
        }

        //eat ghost
        if (utils.isAnyGhostEdible(game)) {
            Constants.GHOST closestEdibleGhost = utils.getClosestEdibleGhost(game);
            if (closestGhost == closestEdibleGhost) {
                double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestEdibleGhost), Constants.DM.PATH);
                if (25 > distance) {
                    return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestEdibleGhost), Constants.DM.PATH);
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





