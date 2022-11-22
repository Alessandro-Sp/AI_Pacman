package pacman.entries.pacman;

import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.RandomGhosts;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Random;

public class Simulation {

    /**
     * This method runs the simulation in order to find Ms Pac-man next move
     *
     * @param game Game Object
     * @param node Node currently visited
     * @return current Node
     */
    public Node run(Game game, Node node) {

        int powerPillsScore = game.getNumberOfActivePowerPills();
        int pillsScore = game.getNumberOfActivePills();
        int ghostScore = game.getNumGhostsEaten();

        int pacmanPosition = game.getPacmanCurrentNodeIndex();

        double ghostBLINKYDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.PATH);
        double ghostPINKYDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.MANHATTAN);
        double ghostINKYDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.EUCLID);
        double ghostSUEDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.PATH);

        EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves = new EnumMap<>(Constants.GHOST.class);
        ghostMoves.put(Constants.GHOST.BLINKY,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), pacmanPosition, game.getGhostLastMoveMade(Constants.GHOST.BLINKY), Constants.DM.PATH));
        ghostMoves.put(Constants.GHOST.INKY,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), pacmanPosition, game.getGhostLastMoveMade(Constants.GHOST.INKY), Constants.DM.MANHATTAN));
        ghostMoves.put(Constants.GHOST.PINKY,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), pacmanPosition, game.getGhostLastMoveMade(Constants.GHOST.PINKY), Constants.DM.EUCLID));
        ghostMoves.put(Constants.GHOST.SUE,
                game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), pacmanPosition, game.getGhostLastMoveMade(Constants.GHOST.SUE), Constants.DM.PATH));

        game.advanceGame(node.getState().getMove(), ghostMoves);

        if (game.wasPacManEaten()) {
            node.getState().setWasPacManEaten(game.wasPacManEaten());
        }

        if (game.wasPacManEaten()) {
            return node;
        }


        node.getState().addScore(game.getScore());

        //pill count is 0
        if (game.getNumberOfActivePills() == 0) {
            node.getState().addScore(100);
        }

        //add custom score for pills eaten
//        int powerPillsEaten = powerPillsScore - game.getNumberOfActivePowerPills();
//        if (0 < powerPillsEaten) {
//            for (int i = 0; i < powerPillsEaten; i++) {
//                node.getState().addScore(10);
//            }
//        }
//        int pillsEaten = pillsScore - game.getNumberOfActivePills();
//        if (0 < pillsEaten) {
//            for (int i = 0; i < pillsEaten; i++) {
//                node.getState().addScore(10);
//            }
//        }

        int ghostEaten = ghostScore - game.getNumGhostsEaten();
        if (0 < ghostEaten) {
            for (int i = 0; i < ghostEaten; i++) {
                node.getState().addScore(10);
            }
        }

        //add custom score for game level
        int currentLevel = game.getCurrentLevel();
        if (1 == currentLevel) {
            node.getState().addScore(10);
        }

        //add custom score for lives left
//        int lives = game.getPacmanNumberOfLivesRemaining();
//        for (int i = 0; i < lives; i++) {
//            node.getState().addScore(10);
//        }

        int gameTime = game.getCurrentLevelTime();
        if (gameTime >= 3999) {
            node.getState().addScore(10);
        }

        //get ghost distance
        double ghostBLINKYDistanceScoreAfter = ghostBLINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.MANHATTAN);
        if (ghostBLINKYDistanceScoreAfter > ghostBLINKYDistanceScoreBefore) {//&& game.isGhostEdible(Constants.GHOST.BLINKY)
            node.getState().addScore(10);
        }
        double ghostPINKYDistanceScoreAfter = ghostPINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.MANHATTAN);
        if (ghostPINKYDistanceScoreAfter > ghostPINKYDistanceScoreBefore) {//&& game.isGhostEdible(Constants.GHOST.PINKY)
            node.getState().addScore(10);
        }
        double ghostINKYDistanceScoreAfter = ghostINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.MANHATTAN);
        if (ghostINKYDistanceScoreAfter > ghostINKYDistanceScoreBefore) {//&& game.isGhostEdible(Constants.GHOST.INKY)
            node.getState().addScore(10);
        }
        double ghostSUEDistanceScoreAfter = ghostSUEDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.MANHATTAN);
        if (ghostSUEDistanceScoreAfter > ghostSUEDistanceScoreBefore) {// && game.isGhostEdible(Constants.GHOST.SUE)
            node.getState().addScore(10);
        }

        return node;
    }

}