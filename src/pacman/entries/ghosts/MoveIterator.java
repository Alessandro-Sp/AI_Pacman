package pacman.entries.ghosts;

import static pacman.game.Constants.NUM_GHOSTS;

import java.util.Iterator;
import java.util.NoSuchElementException;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

	/*
	 * An iterator over the valid moves each ghost has
	 * Returns the next set of valid moves.
	 */
public class MoveIterator implements Iterator<MOVE[]>
{
	private MOVE[][] options;
	private int[] curr;
	private long totalPerms;
	private long counter = 0;
	
	public MoveIterator(Game game) {
		options = new MOVE[NUM_GHOSTS][];
		MOVE[] nothing = { MOVE.NEUTRAL };
				
		for (GHOST g: GHOST.values()) {
			int gi = g.ordinal();
			options[gi] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(g), game.getGhostLastMoveMade(g));
			if (options[gi].length == 0) //This only happens if we are in the lair
				options[gi] = nothing;
		}
		totalPerms = 1;
		curr = new int[options.length];
		
		for (int i=0; i<options.length; i++) {
			totalPerms *= options[i].length;
			curr[i] = 0;
		}
	}
	
	public boolean hasNext() {
	    return counter < totalPerms;
	} 
	
	//abstract remove() specified by Iterator is not implemented
	public void remove() {}
	
	public MOVE[] next() {
		counter++;
	    if (counter > totalPerms) {
	        throw new NoSuchElementException();
	    } 
	    if (counter > 1) {
	    	int i = 0;
	    	boolean done = false;
	    	while (!done)
	    		if (++curr[i] < options[i].length)
	    			done = true;
	    		else
	    			curr[i++] = 0;
	    }
	    MOVE[] result = new MOVE[options.length];
	    for (int i=0; i<result.length; i++)
	    	result[i] = options[i][curr[i]];
	    return result;
	}
}