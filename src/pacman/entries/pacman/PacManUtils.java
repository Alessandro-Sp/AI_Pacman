package pacman.entries.pacman;

import pacman.game.Constants.MOVE;

import java.util.*;

public class PacManUtils {

    /**
     * This method is used to get a random move
     * @return random move
     */
    public MOVE getRandomMove(){
        MOVE move = MOVE.values()[new Random().nextInt(MOVE.values().length)];
        while (MOVE.NEUTRAL == move){
            move = MOVE.values()[new Random().nextInt(MOVE.values().length)];
        }
        return move;
    }

    public Node getChildNodeWithMaxVisit(Node rootNode) {
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

    public void addNode(Node parent, Node node){
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
     * @param node child node
     */
    public void addChild(Node node) {
        node.setParent(this);
        this.getChildNodes().add(node);
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
    boolean wasPacManEaten;

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
}