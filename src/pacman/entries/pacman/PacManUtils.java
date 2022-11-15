package pacman.entries.pacman;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.*;

public class PacManUtils {

    /**
     * This method is used to get a node
     * @param nodes list of nodes to choose from
     * @return random noe form supplied list
     */
    public Node getRandomMove(Game game, List<Node> nodes) {
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());

        Node nextMove = null;

        for (Node node : nodes) {
            for (MOVE move : moves) {
                if (move == node.getState().getMove()) {
                    nextMove = node;
                    break;
                }
            }
        }
        return nextMove;
    }

    public Node getBestChild(Node rootNode) {
        return Collections.max(rootNode.getChildNodes(), Comparator.comparing(c -> c.getState().getNOfVisits()));
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

    public void subtractScore(int score) {
        this.score -= score;
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