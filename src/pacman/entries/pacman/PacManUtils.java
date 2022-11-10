package pacman.entries.pacman;

import pacman.game.Constants.MOVE;

import java.util.*;

public class PacManUtils {

    /**
     * This method is used to get a random move
     *
     * @return random move
     */
    public MOVE getRandomMove() {
        MOVE move = MOVE.values()[new Random().nextInt(MOVE.values().length)];
        while (MOVE.NEUTRAL == move) {
            move = MOVE.values()[new Random().nextInt(MOVE.values().length)];
        }
        return move;
    }

    public Node getBestChild(Node rootNode) {
        return Collections.max(rootNode.getChildNodes(), Comparator.comparing(c -> c.getState().getNOfVisits()));
        //get UCB values
//        Map<Node, Double> UCBValues = new HashMap<>();
//        for (Node node : rootNode.getChildNodes()) {
//            UCBValues.put(node, new MonteCarloTreeSearch().UCBValue(rootNode.getState().getScore(), node.getState().getScore(), node.getState().getNOfVisits()));
//        }
//
//        Node bestChild = null;
//        Double maxUCB = 0.0;
//        int pills = 0;
//
//        for (Map.Entry<Node, Double> entry : UCBValues.entrySet()) {
//            Node next = entry.getKey();
//            if (null == bestChild || (entry.getValue() > maxUCB && next.getState().getNofPills() < pills)) {
//                maxUCB = entry.getValue();
//                pills = next.getState().getNofPills();
//                bestChild = next;
//            }
//        }
//
//        return bestChild;
    }
}

/**
 * Tree class used to get the tree and initialise it
 */
class Tree {
    List<Node> tree = new ArrayList<>();
    Node root;

    /**
     * This method initialises the tree
     *
     * @param node root
     */
    Tree(Node node) {
        this.tree.add(node);
        this.root = node;
    }

    public List<Node> getTree() {
        return tree;
    }

    public Node getRoot() {
        return root;
    }

    public void addNode(Node parent, Node node) {
        parent.addChild(node);
    }
}

/**
 * This class hold the node information
 */
class Node {
    State state;
    Node parent;
    List<Node> childNodes;

    public Node(State state, Node parent, List<Node> childNodes) {
        this.state = state;
        this.parent = parent;
        this.childNodes = childNodes;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Node> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * This method add a child to the node
     *
     * @param node child node
     */
    public void addChild(Node node) {
        node.setParent(this);
        this.getChildNodes().add(node);
    }

    /**
     * Used to check if node is the root Node
     *
     * @return True if parent is found
     */
    public boolean isRootNode() {
        return null == this.parent;
    }

    /**
     * Used to check if node is a terminal node
     *
     * @param node Node obj
     * @return True if no children are found
     */
    public boolean isLeafNode(Node node) {
        return null == node.getChildNodes();
    }
}

/**
 * This class is used to store the state of each node
 */
class State {
    int score; //game score
    int NOfVisits; //how many time the node is been visited
    MOVE move;
    boolean wasPacManEaten;

    int NofPills;

    public State(int score, int NOfVisits, MOVE move) {
        this.score = score;
        this.NOfVisits = NOfVisits;
        this.move = move;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getNOfVisits() {
        return NOfVisits;
    }

    public void addVisit() {
        this.NOfVisits++;
    }

    public MOVE getMove() {
        return move;
    }

    public void setMove(MOVE move) {
        this.move = move;
    }

    public boolean isWasPacManEaten() {
        return wasPacManEaten;
    }

    public void setWasPacManEaten(boolean wasPacManEaten) {
        this.wasPacManEaten = wasPacManEaten;
    }

    public int getNofPills() {
        return NofPills;
    }

    public void setNofPills(int nofPills) {
        NofPills = nofPills;
    }
}