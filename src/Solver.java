import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

/*
 * 
 * Solver class contains all the algorithm for solving the 8-puzzle.
 * Takes a Board object that contains the representation of the current state
 * and the goal state, will print out a solution with the paths it took to goal state.
 * 
 */
public class Solver {
	private int[][] goal, board;
	private int n;
	Board current;
	
	public Solver(Board b) {
		goal = b.getGoal();
		board = b.getPuzzle();
		n = goal.length;
		current = b;
	}
	

	/*
	 * Breath-first algorithm
	 */
	public void bfs() {
		LinkedList<Board> stack = new LinkedList<Board>();
		LinkedList<Board> explored = new LinkedList<Board>();
		int maxQueue = 0;
		int dequeue = 0;
	
		while(! current.isGoal()) {
			ArrayList<Board> neighbor = current.getNeighbor();
			
			for(Board n : neighbor) {
				if(explored.contains(n)) {
					continue;
				}
				stack.add(n);
			}
			
			explored.add(current);
			current = stack.poll();
			
			dequeue++;
			
			if(stack.size() > maxQueue) {
				maxQueue = stack.size();
			}
		}
		
		printPath(current);
		System.out.println("Dequeue size: " + dequeue);
		System.out.println("Max Queue size: " + maxQueue);
	}
	

	/*
	 * Depth-first algorithm
	 */
	public void dfs() {		
		LinkedList<Board> stack = new LinkedList<Board>();
		LinkedList<Board> explored = new LinkedList<Board>();
		int maxQueue = 0;
		int dequeue = 0;

		while (! current.isGoal()) {
			ArrayList<Board> neighbor = current.getNeighbor();
			
			for(Board n : neighbor) {
				if(explored.contains(n)) {
					continue;
				}
				stack.push(n);
			}
			
			explored.push(current);
			current = stack.poll();
			dequeue++;
			
			if(stack.size() > maxQueue) {
				maxQueue = stack.size();
			}

		}		
		
		printPath(current);	
		System.out.println("Dequeue size: " + dequeue);
		System.out.println("Max Queue size: " + maxQueue);
	}
	
	/*
	 * Iterative deepening algorithm
	 */
	public void iterativeDeepening() {		
		LinkedList<Board> stack = new LinkedList<Board>();
		LinkedList<Board> explored = new LinkedList<Board>();
		int depth = 0;
		int maxQueue = 0;
		int dequeue = 0;
		Board root = current;
		
		for (int i = 0; i < Integer.MAX_VALUE ; i ++) {
			if(current.isGoal()) {
				break;
			}
			stack.clear();
			explored.clear();
			current = root;
			stack.push(current);	
						
			while ( !current.isGoal() ) {
				if(stack.isEmpty()) {
					break;
				}				
				current = stack.poll();
				explored.push(current);			
				ArrayList<Board> neighbor = current.getNeighbor();	
				
				for(Board n : neighbor) {						
					
					if (n.getDepth() <= depth && !explored.contains(n)) {
						stack.push(n);
					}
				}

				if(stack.size() > maxQueue) {
					maxQueue = stack.size();
				}
				dequeue++;
			}			
			depth ++;
		}
				
		printPath(current);	
		System.out.println("Dequeue size: " + dequeue);
		System.out.println("Max Queue size: " + maxQueue);
	}
	
	/*
	 * Uniform-cost algorithm
	 */
	public void uniformCost() {		
		PriorityQueue<Board> queue = new PriorityQueue<Board> (10, (a,b) -> a.getRunningCost() - b.getRunningCost());
		LinkedList<Board> explored = new LinkedList<Board>();
		int maxQueue = 0;
		int dequeue = 0;
		
		while( !current.isGoal() ) {
			ArrayList<Board> neighbor = current.getNeighbor();
			
			for (Board n : neighbor) {
				if( !explored.contains(n) && !queue.contains(n) ) {
					queue.add(n);
				}
				else if ( queue.contains(n) ) {
					Object[] arr = queue.toArray();
					for (Object o : arr) {
						Board existing = (Board)o;
						if(existing.equals(n)) {
							if(existing.getRunningCost() > n.getRunningCost() ) {
								queue.remove(existing);
								queue.add(n);
							}
						}
					}
				}
				else if ( explored.contains(n) ) {
					continue;
				}
			}
			if(queue.size() > maxQueue) {
				maxQueue = queue.size();
			}
			explored.add(current);
			current = queue.poll();
			dequeue++;
		}

		printPath(current);
		System.out.println("Dequeue size: " + dequeue);
		System.out.println("Max Queue size: " + maxQueue);
	}
	
	/*
	 * Best-first algorithm
	 * Heuristic: Number of tiles out of place
	 */
	public void bestFirst() {
		PriorityQueue<Board> queue = new PriorityQueue<Board> (10, (a,b) -> a.getHamming() - b.getHamming());
		LinkedList<Board> explored = new LinkedList<Board>();
		int maxQueue = 0;
		int dequeue = 0;
		
		while( !current.isGoal() ) {
			ArrayList<Board> neighbor = current.getNeighbor();
			
			for (Board n : neighbor) {
				if( !explored.contains(n) && !queue.contains(n) ) {
					queue.add(n);
				}
				else if ( queue.contains(n) ) {
					Object[] arr = queue.toArray();
					for (Object o : arr) {
						Board existing = (Board)o;
						if(existing.equals(n)) {
							if(existing.getHamming() > n.getHamming() ) {
								queue.remove(existing);
								queue.add(n);
							}
						}
					}
				}
				else if ( explored.contains(n) ) {
					continue;
				}
			}
			if(queue.size() > maxQueue) {
				maxQueue = queue.size();
			}
			explored.add(current);
			current = queue.poll();
			dequeue++;
		}
		
		printPath(current);
		System.out.println("Max Queue size: " + maxQueue);
		System.out.println("Dequeue size: " + dequeue);	
	}
	
	/*
	 * A* algorithm
	 * Heuristic: Number of tiles out of place
	 */
	public void aStar() {
		PriorityQueue<Board> queue = new PriorityQueue<Board> (10, (a,b) -> (a.getHamming() + a.getRunningCost()) - (b.getHamming() + b.getRunningCost()) );
		LinkedList<Board> explored = new LinkedList<Board>();
		int maxQueue = 0;
		int dequeue = 0;
		
		while( !current.isGoal() ) {
			ArrayList<Board> neighbor = current.getNeighbor();
			
			for (Board n : neighbor) {
				if( !explored.contains(n) && !queue.contains(n) ) {
					queue.add(n);
				}
				else if ( queue.contains(n) ) {
					Object[] arr = queue.toArray();
					for (Object o : arr) {
						Board existing = (Board)o;
						if(existing.equals(n)) {
							if((existing.getHamming() + existing.getRunningCost()) > (n.getHamming() + n.getRunningCost()) ) {
								queue.remove(existing);
								queue.add(n);
							}
						}
					}
				}
				else if ( explored.contains(n) ) {
					continue;
				}
			}
			
			if(queue.size() > maxQueue) {
				maxQueue = queue.size();
			}
			
			explored.add(current);
			current = queue.poll();
			dequeue++;			
		}
	
		printPath(current);
		System.out.println("Dequeue size: " + dequeue);
		System.out.println("Max Queue size: " + maxQueue);	
	}

	
	/*
	 * A* algorithm
	 * Heuristic: Manhattan distance
	 */
	public void aStar2() {
		PriorityQueue<Board> queue = new PriorityQueue<Board> (10, (a,b) -> (a.getManhattan() + a.getRunningCost()) - (b.getManhattan() + b.getRunningCost()) );
		LinkedList<Board> explored = new LinkedList<Board>();
		int maxQueue = 0;
		int dequeue = 0;
		
		while( !current.isGoal() ) {
			ArrayList<Board> neighbor = current.getNeighbor();
			
			for (Board n : neighbor) {
				if( !explored.contains(n) && !queue.contains(n) ) {
					queue.add(n);
				}
				else if ( queue.contains(n) ) {
					Object[] arr = queue.toArray();
					for (Object o : arr) {
						Board existing = (Board)o;
						if(existing.equals(n)) {
							if((existing.getManhattan() + existing.getRunningCost()) > (n.getManhattan() + n.getRunningCost()) ) {
								queue.remove(existing);
								queue.add(n);
							}
						}
					}
				}
				else if ( explored.contains(n) ) {
					continue;
				}
			}
			if(queue.size() > maxQueue) {
				maxQueue = queue.size();
			}
			explored.add(current);
			current = queue.poll();
			dequeue++;
		}
		
		printPath(current);
		System.out.println("Dequeue size: " + dequeue);
		System.out.println("Max Queue size: " + maxQueue);

		
	}
	
	/*
	 * A* algorithm
	 * Heuristic: The sum of the tiles that is out of place in row and out of place in column.
	 */
	public void aStar3() {
		PriorityQueue<Board> queue = new PriorityQueue<Board> (10, (a,b) -> (a.getRowColOut() + a.getRunningCost()) - (b.getRowColOut() + b.getRunningCost()) );
		LinkedList<Board> explored = new LinkedList<Board>();
		int maxQueue = 0;
		int dequeue = 0;
		
		while( !current.isGoal() ) {
			ArrayList<Board> neighbor = current.getNeighbor();
			
			for (Board n : neighbor) {
				if( !explored.contains(n) && !queue.contains(n) ) {
					queue.add(n);
				}
				else if ( queue.contains(n) ) {
					Object[] arr = queue.toArray();
					for (Object o : arr) {
						Board existing = (Board)o;
						if(existing.equals(n)) {
							if((existing.getRowColOut() + existing.getRunningCost()) > (n.getRowColOut() + n.getRunningCost()) ) {
								queue.remove(existing);
								queue.add(n);
							}
						}
					}
				}
				else if ( explored.contains(n) ) {
					continue;
				}
			}
			if(queue.size() > maxQueue) {
				maxQueue = queue.size();
			}
			explored.add(current);
			current = queue.poll();
			dequeue++;
		}
		
		printPath(current);
		System.out.println("Dequeue size: " + dequeue);
		System.out.println("Max Queue size: " + maxQueue);

		
	}

	/*
	 * printPath method takes a goal state node and trace parent node back up to the
	 * initial state for printing the complete path that solved the puzzle.
	 */
	public void printPath(Board foundGoal) {
		LinkedList<Board> path = new LinkedList<Board>();
		int totalCost = 0;
		int pathDepth = 0;
		Board current = foundGoal;
		while( current != null ) {
			path.push(current);
			current = current.getParent();
		}
		
		for(Board p : path) {
			int cost = p.getCost();
			totalCost += cost;
			System.out.println(p);
			System.out.println(p.getDirection() + "  Cost: " + cost + "  Total Cost: " + totalCost + "\n\n");
			pathDepth++;
		}
		System.out.println("Depth to solve: " + pathDepth);
		System.out.println("Cost to solve: " + totalCost);
	}
	
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
}
