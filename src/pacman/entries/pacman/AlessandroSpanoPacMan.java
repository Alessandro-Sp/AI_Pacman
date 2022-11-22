package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.*;


/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class AlessandroSpanoPacMan extends Controller<MOVE> {

    private MOVE myMove;

    public MOVE getMove(Game game, long timeDue) {
        //game logic

        PacManUtils utils = new PacManUtils();

        //Create Tree
        State state = new State(game.getScore(), 0, game.getPacmanLastMoveMade());
        Node root = new Node(state, null, new ArrayList<>());
        Tree tree = new Tree(root);

        Constants.GHOST closestGhost = utils.getClosestGhost(game);
        Constants.GHOST closestEdibleGhost = utils.getClosestEdibleGhost(game);

        Constants.GHOST secondClosestGhost = utils.getSecondClosestGhost(game);

        double closestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);
        double secondClosestGhostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN);


        int closestPillIndex = utils.getClosestPillIndex(game);

        //choose next move
        if (game.isJunction(game.getPacmanCurrentNodeIndex())) {

            myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPillIndex, Constants.DM.PATH);

            //escape from ghost
            if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance) {
                if (!game.isGhostEdible(closestGhost)) {
                    myMove = getNextMove(game.copy(), tree);
                }
            }
            //avoid pinch
            if (null != closestGhost && 15 > closestGhostDistance && -1 != closestGhostDistance) {// 15
                if (null != secondClosestGhost && 22 > secondClosestGhostDistance && -1 != secondClosestGhostDistance) {// 22
                    if (!game.isGhostEdible(closestGhost) && !game.isGhostEdible(secondClosestGhost)) {
                        if (myMove == game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(secondClosestGhost), Constants.DM.PATH)) {
                            myMove = getNextMove(game.copy(), tree);
                        }
                    }
                }
            }
            if (null != closestGhost && 18 > closestGhostDistance && -1 != closestGhostDistance) {//&& 1 == game.getCurrentLevel()
                if (null != secondClosestGhost && 32 > secondClosestGhostDistance && -1 != secondClosestGhostDistance) {
                    if (!game.isGhostEdible(closestGhost) && !game.isGhostEdible(secondClosestGhost)) {
                        if (myMove == game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(secondClosestGhost), Constants.DM.PATH)) {
                            MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
                            MOVE nextPossible = possibleMoves[new Random().nextInt(possibleMoves.length)];
                            while (myMove == nextPossible) {
                                nextPossible = possibleMoves[new Random().nextInt(possibleMoves.length)];
                            }
                            myMove = nextPossible;
                        }
                    }
                }
            }
        } else {
            //escape from ghost
            if (null != closestGhost && 12 > closestGhostDistance && -1 != closestGhostDistance && 0 == game.getCurrentLevel()) {// 12
                if (!game.isGhostEdible(closestGhost) || 5 > game.getGhostEdibleTime(closestEdibleGhost)) {// 15
                    return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);
                }
            }
            if (null != closestGhost && 15 > closestGhostDistance && -1 != closestGhostDistance && 1 == game.getCurrentLevel()) {// 12
                if (!game.isGhostEdible(closestGhost) || 5 > game.getGhostEdibleTime(closestEdibleGhost)) {// 15
                    return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);
                }
            }

            MOVE[] possibleNext = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
            myMove = possibleNext[new Random().nextInt(possibleNext.length)];
        }

        //eat ghost
        if (utils.isAnyGhostEdible(game)) {
            if (closestGhost == closestEdibleGhost) {
                double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestEdibleGhost), Constants.DM.PATH);
                if (35 > distance && 15 < game.getGhostEdibleTime(closestEdibleGhost)) {
                    myMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestEdibleGhost), Constants.DM.PATH);
                }
            }
        }

        //avoid going to ghost
        if (myMove == game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.MANHATTAN) && 10 > closestGhostDistance) {
            myMove.opposite();
        }

        return myMove;
    }

    /**
     * This method runs the monte-carlo tree search in order to give us the next move
     *
     * @param game Game object
     * @param tree Tree
     * @return Next move that ms Pac-man is going to do
     */
    private MOVE getNextMove(Game game, Tree tree) {
        return new MonteCarloTreeSearch().nextMove(game, tree);
    }
}

class PacManUtils {

    //ghosts
    private static final Constants.GHOST[] ghosts = {Constants.GHOST.BLINKY, Constants.GHOST.PINKY, Constants.GHOST.INKY, Constants.GHOST.SUE};

    /**
     * This method is used to get a node for a random possible move
     *
     * @param nodes list of nodes to choose from
     * @return random node form supplied list
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

    /**
     * this method checks any ghost is edible
     *
     * @param game game obj
     * @return true if any ghost is edible
     */
    public boolean isAnyGhostEdible(Game game) {
        return game.isGhostEdible(ghosts[0]) || game.isGhostEdible(ghosts[1]) || game.isGhostEdible(ghosts[2]) || game.isGhostEdible(ghosts[3]);
    }

    /**
     * This method finds the closest pill from pacman current position
     *
     * @param game game obj
     * @return closest pill
     */
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

    /**
     * This method finds the closest ghost from pacman current position
     *
     * @param game game obj
     * @return closest ghosts
     */
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

    /**
     * This method finds the second closest edible ghost from pacman current position
     *
     * @param game game obj
     * @return second closest edible ghosts
     */
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

        for (Map.Entry<Constants.GHOST, Double> entry : distanceMap.entrySet()) {
            if (distance.get(1).equals(entry.getValue())) {
                ghost = entry.getKey();
            }
        }
        return ghost;
    }

    /**
     * This method finds the closest edible ghost from pacman current position
     *
     * @param game game obj
     * @return closest edible ghosts
     */
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

class MonteCarloTreeSearch {

    /**
     * this method returns the next move that pac man will take
     *
     * @param game game obj
     * @param tree tree
     * @return next move
     */
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

        int endTime = 35;
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
            if (0 < exploreNode.getChildNodes().size()) {
                exploreNode = new PacManUtils().getRandomMove(game, nextNode.getChildNodes());
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
        return ((double) winScore / (double) NOfVisits) + 1.42 * Math.sqrt(Math.log(visits) / (double) NOfVisits);
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

class Simulation {

    /**
     * This method runs the simulation in order to find Ms Pac-man next move
     *
     * @param game Game Object
     * @param node Node currently visited
     * @return current Node
     */
    public Node run(Game game, Node node) {

        //pacman position
        int pacmanPosition = game.getPacmanCurrentNodeIndex();

        //ghosts
        Constants.GHOST BLINKY = Constants.GHOST.BLINKY;
        Constants.GHOST PINKY = Constants.GHOST.BLINKY;
        Constants.GHOST INKY = Constants.GHOST.BLINKY;
        Constants.GHOST SUE = Constants.GHOST.BLINKY;

        //ghosts distance
        double ghostBLINKYDistanceScoreBefore = game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(BLINKY), Constants.DM.PATH);
        double ghostPINKYDistanceScoreBefore = game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(PINKY), Constants.DM.MANHATTAN);
        double ghostINKYDistanceScoreBefore = game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(INKY), Constants.DM.EUCLID);
        double ghostSUEDistanceScoreBefore = game.getDistance(pacmanPosition, game.getGhostCurrentNodeIndex(SUE), Constants.DM.PATH);

        //next ghosts' move
        EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves = new EnumMap<>(Constants.GHOST.class);
        ghostMoves.put(BLINKY,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(BLINKY), pacmanPosition, game.getGhostLastMoveMade(BLINKY), Constants.DM.PATH));
        ghostMoves.put(INKY,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(INKY), pacmanPosition, game.getGhostLastMoveMade(INKY), Constants.DM.MANHATTAN));
        ghostMoves.put(PINKY,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(PINKY), pacmanPosition, game.getGhostLastMoveMade(PINKY), Constants.DM.EUCLID));
        ghostMoves.put(SUE,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(SUE), pacmanPosition, game.getGhostLastMoveMade(SUE), Constants.DM.PATH));

        //advance game
        game.advanceGame(node.getState().getMove(), ghostMoves);

        //assign flag to node
        if (game.wasPacManEaten()) {
            node.getState().setWasPacManEaten(game.wasPacManEaten());
        }

        //no points for path where pacman was eaten
        if (game.wasPacManEaten()) {
            return node;
        }

        //award score
        node.getState().addScore(game.getScore());

        //reward survival
        int gameTime = game.getCurrentLevelTime();
        if (gameTime >= 3999) {
            node.getState().addScore(10);
        }

        //get ghost distance
        double ghostBLINKYDistanceScoreAfter = ghostBLINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(BLINKY), Constants.DM.PATH);
        if (ghostBLINKYDistanceScoreAfter > ghostBLINKYDistanceScoreBefore) {
            node.getState().addScore(100);
        }
        double ghostPINKYDistanceScoreAfter = ghostPINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(PINKY), Constants.DM.MANHATTAN);
        if (ghostPINKYDistanceScoreAfter > ghostPINKYDistanceScoreBefore) {
            node.getState().addScore(100);
        }
        double ghostINKYDistanceScoreAfter = ghostINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(INKY), Constants.DM.EUCLID);
        if (ghostINKYDistanceScoreAfter > ghostINKYDistanceScoreBefore) {
            node.getState().addScore(100);
        }
        double ghostSUEDistanceScoreAfter = ghostSUEDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(SUE), Constants.DM.PATH);
        if (ghostSUEDistanceScoreAfter > ghostSUEDistanceScoreBefore) {
            node.getState().addScore(100);
        }

        return node;
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

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Node> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * Used to check if node is the root Node
     *
     * @return True if parent is found
     */
    public boolean isRootNode() {
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





