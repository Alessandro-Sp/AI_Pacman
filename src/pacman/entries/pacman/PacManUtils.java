package pacman.entries.pacman;

import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.*;

public class PacManUtils {

    private static final Constants.GHOST[] ghosts = {Constants.GHOST.BLINKY, Constants.GHOST.PINKY, Constants.GHOST.INKY, Constants.GHOST.SUE};

    /**
     * This method is used to get a node
     * @param nodes list of nodes to choose from
     * @return random noe form supplied list
     */
    public Node getRandomMove(Game game, List<Node> nodes) {
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());

        List<Node> possibleNodes = new ArrayList<>();

        for (Node node : nodes) {
            for (MOVE move : moves) {
                if (move == node.getState().getMove()) {
                    possibleNodes.add(node);
                }
            }
        }
        return possibleNodes.get(new Random().nextInt(possibleNodes.size()));
    }

    public Node getBestChild(Node rootNode) {
        return Collections.max(rootNode.getChildNodes(), Comparator.comparing(c -> c.getState().getNOfVisits()));
    }

    public boolean isAnyGhostEdible(Game game) {
        return game.isGhostEdible(ghosts[0]) || game.isGhostEdible(ghosts[1]) || game.isGhostEdible(ghosts[2]) || game.isGhostEdible(ghosts[3]);
    }

    public int getClosestPillIndex(Game game) {
        double shortestDistance = 0;
        int closestPillIndex = 0;

        for (int pill : game.getActivePillsIndices()) {
            double pillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, Constants.DM.PATH);
            if (-1 != pillDistance) {
                if (0 == closestPillIndex || pillDistance < shortestDistance) {
                    closestPillIndex = pill;
                    shortestDistance = pillDistance;
                }
            }
        }

        return closestPillIndex;
    }

    public int getClosestPowerPillIndex(Game game) {
        double shortestDistance = 0;
        int closestPowerPillIndex = 0;

        for (int pill : game.getActivePowerPillsIndices()) {
            double pillDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), pill, Constants.DM.PATH);
            if (-1 != pillDistance) {
                if (0 == closestPowerPillIndex || pillDistance < shortestDistance) {
                    closestPowerPillIndex = pill;
                    shortestDistance = pillDistance;
                }
            }
        }

        return closestPowerPillIndex;
    }

    public Constants.GHOST getClosestGhost(Game game) {
        double shortestDistance = 0;
        Constants.GHOST closestGhost = null;

        for (Constants.GHOST ghost : ghosts) {
            double ghostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.MANHATTAN);
            if (-1 != ghostDistance) {
                if (null == closestGhost || ghostDistance < shortestDistance) {
                    closestGhost = ghost;
                    shortestDistance = ghostDistance;
                }
            }
        }

        return closestGhost;
    }

    public Constants.GHOST getSecondClosestGhost(Game game) {
        Map<Constants.GHOST, Double> distanceMap = new HashMap<>();
        List<Double> distance = new ArrayList<>();

        for (Constants.GHOST ghost : ghosts) {
            double ghostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.MANHATTAN);
            distanceMap.put(ghost, ghostDistance);
            distance.add(ghostDistance);
        }

        Collections.sort(distance);

        Constants.GHOST ghost = null;

        for (Map.Entry<Constants.GHOST, Double> entry : distanceMap.entrySet()){
            if (distance.get(1).equals(entry.getValue())) {
                ghost = entry.getKey();
            }
        }
        return ghost;
    }

    public Constants.GHOST getClosestEdibleGhost(Game game) {
        double shortestDistance = 0;
        Constants.GHOST closestGhost = null;

        for (Constants.GHOST ghost : ghosts) {
            if (game.isGhostEdible(ghost)) {
                double ghostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.MANHATTAN);
                if (null == closestGhost || ghostDistance < shortestDistance) {
                    closestGhost = ghost;
                    shortestDistance = ghostDistance;
                }
            }
        }

        return closestGhost;
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