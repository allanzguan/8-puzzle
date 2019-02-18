import java.util.ArrayList;
import java.util.Arrays;

/**
*	Board class is a state representation of a board. It contains data used to
*	help with the Solver class to solve the 8 puzzle.
*	
*	Board takes two arguments, the starting board and the goal board.
*	
*/
public class Board {
	
	private int[][] puzzle, goal;
	private int n, zeroX, zeroY;
	Board parent = null;
	int cost = 0;
	int runCost = 0;
	int depth = 0;
	int m = 0;
	int h = 0;
	int rc = 0;
	String direction = "";
	
	
	public Board(int[][] b, int[][] g) {
		puzzle = b;
		goal = g;
		n = goal.length;
		findZero();
		m = manhattan();
		h = hamming();
		rc = rowColOut();
	}
	
	public void setCost(int c) {
		cost = c;
	}
	
	public void setDepth(int d) {
		depth = d;
	}
	
	public void setParent(Board p) {
		parent = p;
	}
	
	private void setRunningCost(int pc) {
		runCost = pc;
	}
	
	public void setDirection(String dir) {
		direction = dir;
	}
	
	public int getHamming() {
		return h;
	}
	
	public int getManhattan() {
		return m;
	}
	
	public int getRowColOut() {
		return rc;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public Board getParent() {
		return parent;
	}
	
	public int[][] getPuzzle(){
		return puzzle;
	}
	
	public int[][] getGoal(){
		return goal;
	}
	
	public int getRunningCost() {
		return runCost;
	}
	
	public String getDirection() {
		return direction;
	}
	
	

	/*
	 * (h1) Compute the Hamming distance for this state of the board.
	 */
	public int hamming() {
		int count = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (puzzle[i][j] != 0 && puzzle[i][j] != goal[i][j]) {
					count++;
				}
			}
		}
		return count;
	}
	
	
	/*
	 *  (h2) Compute the Manhattan distance for this state of the board.
	 */
	public int manhattan() {
		int count = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (puzzle[i][j] != 0) {
					int[] loc = goalLocation(puzzle[i][j]);
					count += Math.abs(loc[0] - i) + Math.abs(loc[1] - j);
				}
			}
		}
		return count;
	}
	
	
	/*
	 * (h3) Compute the number of tile that is out of row and out of column.
	 */
	public int rowColOut() {
		int count = 0;
		for (int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(puzzle[i][j] != 0) {
					int[] loc = goalLocation(puzzle[i][j]) ;
					if(loc[0] != i) {
						count++;
					}
					if(loc[1] != j) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	
	/*
	 * Given integer target will return the X and Y location in the goal board.
	 */
	public int[] goalLocation(int target) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (goal[i][j] == target) {
					return new int[] {i,j};
				}
			}
		}
		return null;
	}
	
	
	/*
	 * To check if this state is the goal state.
	 */
	public boolean isGoal() {
		if (puzzle.length != goal.length) {
			return false;
		}
		for(int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if(puzzle[i][j] != goal[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	/*
	 * Helper function for mover() to make a copy of the board object.
	 */
	private int[][] copyPuzzle(){
		int[][] copy = new int[n][n];
		for(int i = 0; i < n; i++) {
			copy[i] = puzzle[i].clone();
		}
		return copy;
	}
	
	
	/*
	 * Helper function for getNeighbor() to swap the integer tile and the zero tile.
	 * Given the x and y for the target integer tile, it will swap with the zero tile.
	 */
	private Board mover(int x, int y) {
		int[][] temp = copyPuzzle();
		int cost = temp[x][y];

		temp[zeroX][zeroY] = temp[x][y];
		temp[x][y] = 0;

		Board newBoard = new Board(temp, goal);
		newBoard.setCost(cost);

		return newBoard;
	}
	
	
	/*
	 * The successor function to generate all possible moves from the current state of board.
	 * getNeighbor will return a list containing all the possible neighbor.
	 * 
	 * This method also keep tracks of the following:
	 * - Parent node
	 * - Depth of the current state
	 * - Sum of the cost to reach current state
	 * - Direction of the tile moved
	 */
	public ArrayList<Board> getNeighbor() {
		ArrayList<Board> neighborList = new ArrayList<Board>();
		
		if( zeroX != 0) {
			Board neighborBoard = mover(zeroX - 1, zeroY);
			neighborBoard.setParent(this);
			neighborBoard.setDepth(this.getDepth() + 1);
			neighborBoard.setRunningCost(runCost + neighborBoard.getCost());
			neighborBoard.setDirection("DOWN");
			neighborList.add(neighborBoard);
		}	
		
		if( zeroX != n-1) {
			Board neighborBoard = mover(zeroX + 1, zeroY);
			neighborBoard.setParent(this);
			neighborBoard.setDepth(this.getDepth() + 1);
			neighborBoard.setRunningCost(runCost + neighborBoard.getCost());
			neighborBoard.setDirection("UP");
			neighborList.add(neighborBoard);
		}	
		
		if( zeroY != n-1) {
			Board neighborBoard = mover(zeroX, zeroY + 1);
			neighborBoard.setParent(this);
			neighborBoard.setDepth(this.getDepth() + 1);
			neighborBoard.setRunningCost(runCost + neighborBoard.getCost());
			neighborBoard.setDirection("LEFT");
			neighborList.add(neighborBoard);
		}	
		
		if( zeroY != 0) {
			Board neighborBoard = mover(zeroX, zeroY - 1);
			neighborBoard.setParent(this);
			neighborBoard.setDepth(this.getDepth() + 1);
			neighborBoard.setRunningCost(runCost + neighborBoard.getCost());
			neighborBoard.setDirection("RIGHT");
			neighborList.add(neighborBoard);
		}	

		return neighborList;
	}
	
	
	/*
	 * This method will return the x and y index of the location for the zero tile.
	 */
	public void findZero() {
		for(int i = 0; i < n; i++) {
			for(int j=0; j < n; j++) {
				if (puzzle[i][j] == 0) {
					zeroX = i;
					zeroY = j;
					break;
				}
			}
		}
	}
	
	
	/*
	 * This method will print the current board in a 3x3 format.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if(puzzle[i][j] == 0) {
					s.append(" ");
				}
				else {
					s.append(puzzle[i][j]);
				}
				s.append(" ");
			}
			s.append("\n");
		}
		return s.toString();
	}
	

	/*
	 * This method is used for comparing two board object by comparing the board representation.
	 */
	@Override
	public boolean equals(Object o) {
		Board b = (Board) o;
		if(Arrays.deepEquals(puzzle, b.getPuzzle())) {
			return true;
		}
		return false;		
	}
	
	
	/*
	 * This method is used for comparing two board object by comparing the board representation.
	 */
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + puzzle.hashCode();
		hash = hash * 31 + goal.hashCode();
		return hash; 		
	}
	
}


