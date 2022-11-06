package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	
	public MOVE getMove(Game game, long timeDue)
	{
		//game logic

		//Create Tree
		State state = new State(game.getScore(), 0, game.getPacmanLastMoveMade());
		Node root = new Node(state, null, new ArrayList<>());
		Tree tree = new Tree(root);

		//call monte-carlo search
		myMove = getNextMove(game.copy(), tree);

		System.out.println(myMove);
//		Game simulation = game.copy();
//		game.advanceGame(new PacManUtils().getRandomMove(), new RandomGhosts().getMove());

//		System.out.println(Arrays.toString(game.getPillIndices()));
//		System.out.println(game.isPillStillAvailable(1));
//		System.out.println(game.getGhostCurrentNodeIndex(BLINKY));
//		System.out.println(game.getGhostEdibleTime(BLINKY));
//		System.out.println(game.getNextMoveTowardsTarget(1,2, PATH));
//		System.out.println(game.getNextMoveAwayFromTarget(1,3, PATH));

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





