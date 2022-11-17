package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;


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
        Constants.GHOST secondClosestGhost = utils.getSecondClosestGhost(game);

        double closestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);
        double secondClosestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);

        //choose next move
        if (MOVE.NEUTRAL == game.getPacmanLastMoveMade()) {
            myMove = getNextMove(game.copy(), tree);
//            while (myMove == game.getPacmanLastMoveMade()) {
//                //call monte-carlo search
//                myMove = getNextMove(game.copy(), tree);
//            }
            return myMove;
        }


        if (-1 != closestGhostDistance) {
            //choose next move
            if (game.isJunction(game.getPacmanCurrentNodeIndex())) {
                if (!game.isGhostEdible(closestGhost)) {
                    //call monte-carlo search
                    myMove = getNextMove(game.copy(), tree);
                }
            }
        } else {
            //focus on eating pills
//            myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), utils.getClosestPillIndex(game), Constants.DM.PATH);
        }

        //escape from ghost
        if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance) {
            if (!game.isGhostEdible(closestGhost)) {
                myMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);
                if (null != secondClosestGhost && 9 > secondClosestGhostDistance)
                    if (myMove == game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(secondClosestGhost), Constants.DM.PATH)) {
                        //call monte-carlo search
                        return getNextMove(game.copy(), tree);
                    }
                return myMove;
            }
        }

        //eat ghost
        if (utils.isAnyGhostEdible(game)) {
            Constants.GHOST closestEdibleGhost = utils.getClosestEdibleGhost(game);
            return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestEdibleGhost), Constants.DM.PATH);
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





