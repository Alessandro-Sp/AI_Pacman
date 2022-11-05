package pacman.entries.pacman;

import pacman.game.Constants.MOVE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacManUtils {

    /**
     * This method is used to get a random move
     * @return
     */
    public MOVE getRandomMove(){
        return MOVE.values()[new Random().nextInt(MOVE.values().length)];
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
}

/**
 * Tree class used to get the tree and initialise it
 */
class Tree {
    List<Node> tree = new ArrayList<>();
    Node root;

    /**
     * This method initialises the tree
     * @param node root
     */
    Tree(Node node){
        this.tree.add(node);
        this.root = node;
    }

    public List<Node> getTree() {
        return tree;
    }

    public Node getRoot() {
        return root;
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

    /**
     * This method add a child to the node
     * @param node child node
     */
    public void addChild(Node node) {
        node.setParent(this);
        this.getChildNodes().add(node);
    }

    /**
     * THis method is used to add multiple children to a node
     * @param childNodes list of children to add
     */
    public void addChildNodes(List<Node> childNodes) {
        for(Node child : childNodes){
            child.setParent(this);
        }
        this.childNodes.addAll(childNodes);
    }

    /**
     * Check to see if is a leaf node
     * @return true if no children are found
     */
    public boolean isTerminalNode(){
        return null == childNodes;
    }

    /**
     * Used to check if node is the root Node
     * @return True if parent is found
     */
    public boolean isRootNode(){
        return null == this.parent;
    }
}

/**
 * This class is used to store the state of each node
 */
class State {
    int score; //game score
    int NOfVisits; //how many time the node is been visited
    MOVE move;

    public State(int score, int NOfVisits, MOVE move) {
        this.score = score;
        this.NOfVisits = NOfVisits;
        this.move = move;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
}