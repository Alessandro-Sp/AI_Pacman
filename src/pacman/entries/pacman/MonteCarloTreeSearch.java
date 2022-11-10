package pacman.entries.pacman;

import pacman.game.Constants;
import pacman.game.Game;

import java.util.*;

public class MonteCarloTreeSearch {


    public Constants.MOVE nextMove(Game game, Tree tree) {
        return new PacManUtils().getBestChild(getNextBestMove(game, tree)).getState().getMove();
    }

    /**
     * This method performs the roll out for the monte-carlo tree search
     * @param game Game Object copy
     * @param tree tree
     * @return Roll out Node
     */
    private Node getNextBestMove(Game game, Tree tree){

        //Initial state
        Node root = tree.getRoot();
        // Expand
        root.setChildNodes(expand(root));

        while (true) {
            Node nextNode = getBestNextNode(root);

            //check if node has not been visited
            if (0 == nextNode.getState().getNOfVisits()) {

                //rollout
                Node rolloutNode = rollout(game, nextNode);

                //back propagate
                backPropagate(rolloutNode, rolloutNode.getState().getScore());
            } else {
                return root;
            }
        }
    }

    /**
     * This method performs the roll out for the monte-carlo tree search
     * @param game Game Object copy
     * @param node Node
     * @return Roll out Node
     */
    private Node rollout(Game game, Node node) {
        Node currentNode = node;

        int index = 0;
        while (!game.gameOver() || index == 5) {//TODO: Find better way
            Node bestNode = currentNode;

            // Check if we need to stop rollout
            if (bestNode.getState().isWasPacManEaten()) {//is terminal node - bestNode.getState().isWasPacManEaten() || null == bestNode.getChildNodes()
                return bestNode;
            }

            // Expand
            bestNode.setChildNodes(expand(bestNode));
            //Get random child node for next move
            Node randomNode = bestNode.getChildNodes().get(new Random().nextInt(bestNode.getChildNodes().size()));

            // Run simulation
            currentNode = new Simulation().run(game, randomNode);

            index++;
        }

        return currentNode;
    }

    /**
     * This method give back the Upper Confidence Bound (UCB)
     *
     * The formula used is - Vi + 2 * sqrt(ln(N)/Ni)
     */
    public double UCBValue(int visits, int winScore, int NOfVisits){
        if (0 == NOfVisits){
            return Integer.MAX_VALUE; // NEED TO CHECK WHAT THIS WILL BE
        }
        return ((double)winScore / (double)NOfVisits) + 2 * Math.sqrt(Math.log(visits) / (double)NOfVisits);
    }

    /**
     * This method give back the Node child node with the best Upper Confidence Bound (UCB)
     * @param node parent Node that needs exploring
     * @return node with Max UCB Value
     */
    private Node findBestUCBValueNode(Node node){
        return Collections.max(
                node.getChildNodes(), Comparator.comparing(c -> UCBValue(node.getState().getNOfVisits(),
                        c.getState().getScore(), c.getState().getNOfVisits())));
    }

    /**
     * This method gives us the next best node with the highest UCB value
     * @param rootNode root node
     * @return next best node with the highest UCB value
     */
    private Node getBestNextNode(Node rootNode){
        Node node = rootNode;
        while (0 != node.getChildNodes().size()) {
            node = findBestUCBValueNode(node);
        }
        return node;
    }

    /**
     * This method is used to perform a single expansion of a parent node
     * @param parent node that needs expansion, this will be set as parent of he expanded node
     * @return List of Nodes, one for each direction
     */
    private Node expandOne(Node parent) {
        return new Node(new State(0, 0, new PacManUtils().getRandomMove()), parent, new ArrayList<>());
    }

    private List<Node> expand(Node parent) {//TODO: add comment
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new State(0, 0, Constants.MOVE.UP), parent, new ArrayList<>()));
        nodes.add(new Node(new State(0, 0, Constants.MOVE.RIGHT), parent, new ArrayList<>()));
        nodes.add(new Node(new State(0, 0, Constants.MOVE.DOWN), parent, new ArrayList<>()));
        nodes.add(new Node(new State(0, 0, Constants.MOVE.LEFT), parent, new ArrayList<>()));
        return nodes;
    }

    /**
     * This method performs the back propagation from the terminal state and returns
     * the move that originally got us to it.
     * @param node Leaf node
     * @return Move that initiate the path
     */
//    private void backPropagate(Node node, int score){
//        Node nextNode = null;
//        while (null != nextNode) {
//            nextNode.getState().addVisit();
//            nextNode.getState().addScore(score);
//            nextNode = nextNode.getParent();
//        }
//    }
    private void backPropagate(Node node, int score){
        if (node.isRootNode()) {
            node.getState().addVisit();
            node.getState().addScore(score);
            return;
        }
        node.getState().addVisit();
        node.getState().addScore(score);
        backPropagate(node.parent, node.getState().getScore());
    }
}
