package pacman.entries.pacman;

import pacman.game.Constants;
import pacman.game.Game;

public class MonteCarloTreeSearch {


    public Constants.MOVE nextMove() {




    }

    /**
     * This method performs the roll out for the monte-carlo tree search
     * @param game Game Object copy
     * @param node Current Node
     * @return Roll out Node
     */
    private Node rollout(Game game, Node node){// node initially is root

        Node currentNode = node;

        int index = 0;
        while(index != 10){// NEED TO FIND A BETTER WAY

            if (node.isTerminalNode()) {
                return node;
            }

            Constants.MOVE move = new PacManUtils().getRandomMove();
            System.out.println("RANDOM MOVE = " + move.name());

            node = new Simulation().run(game, node, move);

            index++;
        }

        return node;
    }

    /**
     * This method performs the back propagation from the terminal state and returns
     * the move that originally got us to it.
     * @param node
     * @param move
     * @return Move that initiate the path
     */
    public Constants.MOVE backPropagate(Node node, Constants.MOVE move){
        if (node.isRootNode())
            return move;
        return backPropagate(node.parent, node.getState().getMove());
    }
}
