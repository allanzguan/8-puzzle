# 8-puzzle
This Java program implements listed search algorithms to solve 8-puzzle probelm.

- Breath-first search
- Depth-first search
- Iterative deepening search
- Uniform-Cost search
- Best-first search
- A*1 (Hamming)
- A*2 (Mahattan)
- A*3 (Row Column out of order)


## Board
Board class is a state representation of a board. It contains data used to help with the Solver class to solve the 8 puzzle.


```
public class Board {
  public Board(int[][] b, int[][] g)            // Board constructor takes puzzle state and goal state.
  public int hamming()                          // (h1) Compute the Hamming distance for this state of the board.
  public int manhattan()                        // (h2) Compute the Manhattan distance for this state of the board.
  public int rowColOut()                        // (h3) Compute the number of tile that is out of row and out of column.
  public int[] goalLocation(int target)         // Given integer target will return the X and Y location in the goal board.
  public boolean isGoal()                       // To check if this state is the goal state.
  private int[][] copyPuzzle()                  // Helper function for mover() to make a copy of the board object.
  private Board mover(int x, int y)             // Helper function for getNeighbor() to swap the integer tile and the zero tile.
  public ArrayList<Board> getNeighbor()         // The successor function to generate all possible moves from the current state of board.
  public void findZero()                        // The successor function to generate all possible moves from the current state of board.
  public String toString()                      // This method will print the current board in a 3x3 format.
  public boolean equals(Object o)               // This method is used for comparing two board object by comparing the board representation.
  public int hashCode()                         // This method is used for comparing two board object by comparing the board representation.
 }
```
 
 ## Solver
 Solver class contains all the algorithm for solving the 8-puzzle.
Takes a Board object that contains the representation of the current state and the goal state, will print out a solution with the paths it took to goal state.

```
public class Solver {
  public Solver(Board b)                        // Constructor takes a board object
  public void bfs()                             // Breath-first search
  public void dfs()                             // Depth-first search
  public void iterativeDeepening()              // Iterative deepening search
  public void uniformCost()                     // Uniform-Cost search
  public void bestFirst()                       // Best-first search
  public void aStar()                           // A*1 (Hamming)
  public void aStar2()                          // A*2 (Mahattan)
  public void aStar3()                          // A*3 (Row Column out of order)
  public void printPath(Board foundGoal)        // Prints the complete path it took to solve the puzzle
 }
 ```

## Example Run
```
	public static void main(String[] args) {
		int[][] goal = {{1,2,3},{8,0,4},{7,6,5}};
		
		int[][] easy = {{1,3,4},{8,6,2},{7,0,5}};
		int[][] medium = {{2,8,1},{0,4,3},{7,6,5}};
		int[][] hard = {{5,6,7},{4,0,8},{3,2,1}};
					
		Solver solver  = new Solver(new Board(easy ,goal));
		//Solver solver  = new Solver(new Board(medium ,goal));
		//Solver solver  = new Solver(new Board(hard ,goal));	
		
		solver.bfs();
		//solver.dfs();
		//solver.iterativeDeepening();
		//solver.uniformCost();
		//solver.bestFirst();
		//solver.aStar();
		//solver.aStar2();
		//solver.aStar3();

	}
```
