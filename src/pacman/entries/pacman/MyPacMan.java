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
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove = MOVE.LEFT;
	
	public MOVE getMove(Game game, long timeDue)
	{
		//game logic

		if (game.isGhostEdible(Constants.GHOST.BLINKY)){
			return myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.PATH);
		}
		if (game.isGhostEdible(Constants.GHOST.PINKY)){
			return myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.PATH);
		}
		if (game.isGhostEdible(Constants.GHOST.INKY)){
			return myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.PATH);
		}
		if (game.isGhostEdible(Constants.GHOST.SUE)){
			return myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.PATH);
		}

		if (20 > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.PATH)) {
			return myMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.PATH);
		}
		if (20 > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.PATH)) {
			return myMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.PATH);
		}
		if (20 > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.PATH)) {
			return myMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.PATH);
		}
		if (20 > game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.PATH)) {
			return myMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.PATH);
		}

		//Create Tree
		State state = new State(game.getScore(), 0, game.getPacmanLastMoveMade());
		Node root = new Node(state, null, new ArrayList<>());
		Tree tree = new Tree(root);

		if (game.isJunction(game.getPacmanCurrentNodeIndex()) || MOVE.NEUTRAL == game.getPacmanLastMoveMade()) {
			//call monte-carlo search
			myMove = getNextMove(game.copy(), tree);
		}

		return myMove;
	}

	/**
	 * This method runs the monte-carlo tree search in order to give us the next move
	 * @param game Game object
	 * @param tree Tree
	 * @return Next move that ms Pac-man is going to do
	 */
	private MOVE getNextMove(Game game, Tree tree){
		return new MonteCarloTreeSearch().nextMove(game, tree);
	}

}





