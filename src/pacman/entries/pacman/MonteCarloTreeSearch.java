package pacman.entries.pacman;

import pacman.game.Constants;
import pacman.game.Game;

import java.util.*;

public class MonteCarloTreeSearch {


    public Constants.MOVE nextMove(Game game, Tree tree) {
        return getBestNextNode(getNextMove(game, tree)).getState().getMove();
    }

    /**
     * This method performs the roll out for the monte-carlo tree search
     *
     * @param game Game Object copy
     * @param tree tree
     * @return Roll out Node
     */
    private Node getNextMove(Game game, Tree tree) {

        //Initial state
        Node root = tree.getRoot();
        // Expand
        root.setChildNodes(expand(root));

        long start = new Date().getTime();

        int endTime = 38;
        while (new Date().getTime() <= start + endTime) {

            //Selection
            Node nextNode = getBestNextNode(root);

            //if not end game
            if (!nextNode.getState().isWasPacManEaten()) {
                // Expand
                nextNode.setChildNodes(expand(nextNode));
            }

            //explore
            Node exploreNode = nextNode;
            if (0 < exploreNode.getChildNodes().size()){
                exploreNode = new PacManUtils().getRandomMove(game, nextNode.getChildNodes());//TODO: looks best option
//                exploreNode = nextNode.getChildNodes().get(new Random().nextInt(nextNode.getChildNodes().size()));
            }

            //rollout
            Node rolloutNode = rollout(game.copy(), exploreNode);
            //back propagate
            backPropagate(exploreNode, rolloutNode.getState().getScore());

        }

        return tree.getRoot();
    }

    /**
     * This method performs the roll out for the monte-carlo tree search
     *
     * @param game Game Object copy
     * @param node Node
     * @return Roll out Node
     */
    private Node rollout(Game game, Node node) {
        Node currentNode = node;

        while (true) {
            Node bestNode = currentNode;

            // Check if we need to stop rollout
            if (bestNode.getState().isWasPacManEaten()) {//is terminal node
                return bestNode;
            }

            // Expand
            bestNode.setChildNodes(expand(bestNode));

            //Get random child node for next move
            Node randomNode = bestNode.getChildNodes().get(new Random().nextInt(bestNode.getChildNodes().size()));

            // Run simulation
            currentNode = new Simulation().run(game, randomNode);
        }
    }

    /**
     * This method give back the Upper Confidence Bound (UCB)
     * <p>
     * The formula used is - Vi + 2 * sqrt(ln(N)/Ni)
     */
    public double UCBValue(int visits, int winScore, int NOfVisits) {
        if (0 == NOfVisits) {
            return Integer.MAX_VALUE;
        }
        return ((double) winScore / (double) NOfVisits) + 2 * Math.sqrt(Math.log(visits) / (double) NOfVisits);
    }

    /**
     * This method give back the Node child node with the best Upper Confidence Bound (UCB)
     *
     * @param node parent Node that needs exploring
     * @return node with Max UCB Value
     */
    private Node findBestUCBValueNode(Node node) {
        return Collections.max(
                node.getChildNodes(), Comparator.comparing(c -> UCBValue(node.getState().getNOfVisits(),
                        c.getState().getScore(), c.getState().getNOfVisits())));
    }

    /**
     * This method gives us the next best node with the highest UCB value
     *
     * @param rootNode root node
     * @return next best node with the highest UCB value
     */
    private Node getBestNextNode(Node rootNode) {
        Node node = rootNode;
        if (0 != node.getChildNodes().size()) {
            node = findBestUCBValueNode(node);
        }
        return node;
    }

//    /**
//     * This method is used to perform a single expansion of a parent node
//     * @param parent node that needs expansion, this will be set as parent of he expanded node
//     * @return List of Nodes, one for each direction
//     */
//    private Node expandOne(Node parent) {
//        return new Node(new State(0, 0, new PacManUtils().getRandomMove()), parent, new ArrayList<>());
//    }

    /**
     * Gives all the possible children back
     *
     * @param parent parent Node
     * @return List of children
     */
    private List<Node> expand(Node parent) {
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
     *
     * @param node Leaf node
     */
    private void backPropagate(Node node, int score) {
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
