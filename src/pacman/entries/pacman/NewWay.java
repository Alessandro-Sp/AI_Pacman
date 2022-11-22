//package pacman.entries.pacman;
//
//import pacman.game.Constants;
//import pacman.game.Game;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class NewWay {
//
//    public Constants.MOVE nextMove2(Game game, Tree tree) {
//        return new PacManUtils().getBestChild(getNextMove(game, tree)).getState().getMove();
//    }
//
//    /**
//     * This method performs the search for the best path to follow
//     *
//     * @param game Game Object copy
//     * @param tree tree
//     * @return Roll out Node
//     */
//    private Node getNextMove(Game game, Tree tree) {
//
//        //Initial state
//        Node root = tree.getRoot();
//        // Expand
//        root.setChildNodes(expand2(root));
//
//        long start = new Date().getTime();
//
//        //Selection
//        Node nextNode = root;
//
//        int endTime = 38;
//        int index = 0;
//        while (new Date().getTime() <= start + endTime) {
////        while (200 != index) {
//
//
//            //if not end game
//            if (!nextNode.getState().isWasPacManEaten()) {
//                // Expand
//                nextNode.setChildNodes(expand2(nextNode));
//            }
//
//            //explore
//            Node exploreNode = nextNode;
//            for (int i = 0; i < exploreNode.getChildNodes().size(); i++){
//                Node childNode = exploreNode.getChildNodes().get(i);
//
//                // Run simulation
//                Node simulatedNode = new Simulation().run(game, childNode);
//
//                //back propagate
//                backPropagate2(simulatedNode, simulatedNode.getState().getScore());
//            }
//            //updating next node
//            if (0 != nextNode.getChildNodes().size())
//                nextNode = new PacManUtils().getBestChild(nextNode);
//
//            index++;
//        }
//        return tree.getRoot();
//    }
//
//    /**
//     * Gives all the possible children back
//     *
//     * @param parent parent Node
//     * @return List of children
//     */
//    private List<Node> expand2(Node parent) {
//        List<Node> nodes = new ArrayList<>();
//        nodes.add(new Node(new State(0, 0, Constants.MOVE.UP), parent, new ArrayList<>()));
//        nodes.add(new Node(new State(0, 0, Constants.MOVE.RIGHT), parent, new ArrayList<>()));
//        nodes.add(new Node(new State(0, 0, Constants.MOVE.DOWN), parent, new ArrayList<>()));
//        nodes.add(new Node(new State(0, 0, Constants.MOVE.LEFT), parent, new ArrayList<>()));
//        return nodes;
//    }
//
//    /**
//     * This method performs the back propagation from the terminal state and returns
//     * the move that originally got us to it.
//     *
//     * @param node Leaf node
//     */
//    private void backPropagate2(Node node, int score) {
//        if (node.isRootNode()) {
//            node.getState().addVisit();
//            node.getState().addScore(score);
//            return;
//        }
//        node.getState().addVisit();
//        node.getState().addScore(score);
//        backPropagate2(node.parent, node.getState().getScore());
//    }
//}
