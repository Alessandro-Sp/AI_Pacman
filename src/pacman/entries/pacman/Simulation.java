package pacman.entries.pacman;

import pacman.controllers.examples.Legacy;
import pacman.controllers.examples.RandomGhosts;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Arrays;
import java.util.Random;

public class Simulation {

    /**
     * This method runs the simulation in order to find Ms Pac-man next move
     * @param game Game Object
     * @param node Node currently visited
     * @return current Node
     */
    public Node run(Game game, Node node){

        int powerPillsScore = game.getNumberOfActivePowerPills();
        int pillsScore = game.getNumberOfActivePills();
        int ghostScore = game.getNumGhostsEaten();
        int livesScore = game.getPacmanNumberOfLivesRemaining();
        double ghostBLINKYDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.MANHATTAN);
        double ghostPINKYDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.MANHATTAN);
        double ghostINKYDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.MANHATTAN);
        double ghostSUEDistanceScoreBefore = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.MANHATTAN);


        game.advanceGame(node.getState().getMove(), new Legacy().getMove());
//        game.advanceGame(node.getState().getMove(), new RandomGhosts().getMove());
//        game.advanceGameWithoutReverse(node.getState().getMove(), new Legacy().getMove());
//        game.advanceGame(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices()[new Random().nextInt(game.getNumberOfActivePills())], game.getPacmanLastMoveMade(), Constants.DM.MANHATTAN), new Legacy().getMove());


        if (game.wasPacManEaten()) {
            node.getState().setWasPacManEaten(game.wasPacManEaten());
//            return node;
        }

        //add game score
//        node.getState().addScore(game.getScore());

        //add custom score for pills eaten
        int powerPillsEaten = powerPillsScore - game.getNumberOfActivePills();
        if (0 < powerPillsEaten) {
            for (int i = 0; i < powerPillsEaten; i++) {
                node.getState().addScore(20);
            }
        }
        int pillsEaten = pillsScore - game.getNumberOfActivePowerPills();
        if (0 < pillsEaten) {
            for (int i = 0; i < pillsEaten; i++) {
                node.getState().addScore(50);
            }
        }

        //add custom score for ghosts eaten
//        int ghostEaten = ghostScore - game.getNumGhostsEaten();
//        if (0 < ghostEaten) {
//            for (int i = 0; i < ghostEaten; i++) {
//                node.getState().addScore(100);
//            }
//        }

        //add custom score for lives left
//        int lives = livesScore - game.getPacmanNumberOfLivesRemaining();
//        if (0 < lives) {
//            for (int i = 0; i < lives; i++) {
//                node.getState().addScore(10);
//            }
//        }

        if (0 != game.getGhostLairTime(Constants.GHOST.BLINKY) || 0 != game.getGhostLairTime(Constants.GHOST.PINKY) || 0 != game.getGhostLairTime(Constants.GHOST.INKY) || 0 != game.getGhostLairTime(Constants.GHOST.SUE)) {
            //get ghost distance
            double ghostBLINKYDistanceScoreAfter = ghostBLINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.MANHATTAN);
            if (ghostBLINKYDistanceScoreAfter > ghostBLINKYDistanceScoreBefore ) {//&& game.isGhostEdible(Constants.GHOST.BLINKY)
                node.getState().addScore(10);
//            System.out.println("+100");
            }
            double ghostPINKYDistanceScoreAfter = ghostPINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.MANHATTAN);
            if (ghostPINKYDistanceScoreAfter > ghostPINKYDistanceScoreBefore ) {//&& game.isGhostEdible(Constants.GHOST.PINKY)
                node.getState().addScore(10);
//            System.out.println("+100");
            }
            double ghostINKYDistanceScoreAfter = ghostINKYDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.MANHATTAN);
            if (ghostINKYDistanceScoreAfter > ghostINKYDistanceScoreBefore ) {//&& game.isGhostEdible(Constants.GHOST.INKY)
                node.getState().addScore(10);
//            System.out.println("+100");
            }
            double ghostSUEDistanceScoreAfter = ghostSUEDistanceScoreBefore - game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.MANHATTAN);
            if (ghostSUEDistanceScoreAfter > ghostSUEDistanceScoreBefore) {// && game.isGhostEdible(Constants.GHOST.SUE)
                node.getState().addScore(10);
//            System.out.println("+100");
            }
        }

//        node.getState().setNofPills(game.getNumberOfPills());
        return node;
    }

}