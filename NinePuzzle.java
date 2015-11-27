/*  1 2 3
    4 0 5
    6 7 8
   
   And a solved board is
   
    1 2 3
    4 5 6
    7 8 0
*/   

import java.util.Scanner;
import java.io.File;

public class NinePuzzle{

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
  public static final int[][] SOLVED = { 
    { 1 , 2 , 3 },    
    { 4 , 5 , 6 },
    { 7 , 8 , 0 }
  };

	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the 
		value 0),return true if the board is solvable and false otherwise. 
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	public static boolean SolveNinePuzzle(int[][] B){
		
	  int b = getIndexFromBoard(B);
    Graph g = BuildNinePuzzle();
    boolean[] visited = new boolean[NUM_BOARDS]; // default initialized to false. Good job Java
    int[] yettovisit = new int[NUM_BOARDS]; // should only need NUM_BOARDS but luckily the program will crash otherwise	
    int[] nodeTo = new int[NUM_BOARDS];

    for (int i = 0; i < NUM_BOARDS; i++) { // two initializations for the price of one!
      yettovisit[i] = -1;
      nodeTo[i] = -1;
    }

		return BFS(b, g, visited, yettovisit, nodeTo);
		
	}


  public static boolean BFS(int vertex, Graph g, boolean[] visited, int[] yettovisit, int[] nodeTo) {
    visited[vertex] = true;

    // Reminder: Be lazy! Check to make sure you're done before you get started!
    if (vertex == getIndexFromBoard(SOLVED)) {
      // unwind NodeTo
      int prev = nodeTo[vertex];
      while (prev != -1) {
        printBoard(getBoardFromIndex(prev));
        prev = nodeTo[prev];
      }
      return true;
    }

    int[] adjlist = g.adjacencyList(vertex);

    // mark adjacent vertices as vertices we have to visit if we havent visited them yet
    for (int i = 0; i < yettovisit.length; i++) {
      for (int j = 0; j < adjlist.length; j++) {
        if (yettovisit[i] == -1 && visited[j] == false) // if program crashed due to Index_Out_Of_Range, this is probably your culprit
          yettovisit[i] = j;  
      }
    }

    // pop the first element from queue
    int next = yettovisit[0];
    
    // mark our path the the next node
    nodeTo[next] = vertex;

    // shift all elements down one
    for (int i = 0; i < (yettovisit.length - 1); i++) {
      yettovisit[i] = yettovisit[i + 1];
    }

    // make sure the end element is empty
    yettovisit[yettovisit.length - 1] = -1;

    // if there are no more nodes to visit, search is over with a negative result
    if (next == -1) return false;

    // visit the next available node (and back down the rabbit hole we go)
    return BFS(next, g, visited, yettovisit, nodeTo);
  }
	/*  printBoard(B)
		Print the given 9-puzzle board. The SolveNinePuzzle method above should
		use this method when printing the sequence of moves which solves the input
		board. If any other method is used (e.g. printing the board manually), the
		submission may lose marks.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}
	
	
	/* Board/Index conversion functions
	   These should be treated as black boxes (i.e. don't modify them, don't worry about
	   understanding them). The conversion scheme used here is adapted from
		 W. Myrvold and F. Ruskey, Ranking and Unranking Permutations in Linear Time,
		 Information Processing Letters, 79 (2001) 281-284. 
	*/
	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;
			
			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}
		
	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}
	

	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}


static class Graph {
    public static final int PUZZLE = 9;
    public static final int row = 3;
    public static final int col = 3;

    private int[][] graph; // adjacency list representation
    private int vertices;
    public int numVertices() { return vertices; }
    public Graph(int v) { 
      vertices = v;
      graph = new int[vertices][vertices]; 
      for (int i = 0; i < vertices; i++) {
        for (int j = 0; j < vertices; j++) {
          graph[i][j] = -1; // initialize all edges to zero
        }
      }
    }

    public int[] adjacencyList(int v) {
      int[] ret = new int[vertices];
      for (int i = 0; i < ret.length; i++) {
        ret[i] = graph[v][i];
      }
      return ret;
    }

    // returns false if edge already exists
    public boolean addEdge(int v1, int v2) {
      boolean addedv1 = false;
      boolean addedv2 = false;

      // add v2 to v1's adjacency list
      for (int i = 0; i < vertices; i++) {
        if (graph[v1][i] == -1) {
          graph[v1][i] = v2;
          addedv1 = true;
          break;
        }
      }
      // add v1 to v2's adjacency list
      for (int i = 0; i < vertices; i++) {
        if (graph[v2][i] == -1) {
          graph[v2][i] = v1;
          addedv2 = true;
          break;
        }
      }
      assert((addedv2 == false && addedv1 == false) || (addedv2 == true && addedv1 ==  true));
      return addedv2 && addedv1;
    }
}

  public static Graph BuildNinePuzzle() {

    Graph ninegraph = new Graph(NUM_BOARDS);  
    int vs = ninegraph.numVertices();

    // iterate over all the vertices to find out the edges
    int px = -1; // the position of the zero on the board
    int py = -1;

    for (int v = 0; v < vs; v++) {
      int[][] board = getBoardFromIndex(v); // get which vertex we are on

      // find the zero in the board
      for (int i = 0; i < 3; i++) { 
        for (int j = 0; j < 3; j++) {
          if (board[i][j] == 0) px = i; py = j; 
        }
      }
      assert(px != -1 || py != -1);

      if (px > 0) ninegraph.addEdge(v, getIndexFromBoard(swap(px - 1, py, px, py, board)));
      if (px < 3) ninegraph.addEdge(v, getIndexFromBoard(swap(px + 1, py, px, py, board)));
      if (py > 0) ninegraph.addEdge(v, getIndexFromBoard(swap(px, py - 1, px, py, board)));
      if (py < 3) ninegraph.addEdge(v, getIndexFromBoard(swap(px, py + 1, px, py, board)));
    }
    return ninegraph;
  }
  // =========== swap ===========================================================
  // swap two places in a board
  // copies everything else
  // ============================================================================
  public static int[][] swap(int x1, int y1, int x2, int y2, int[][] b) {
        int[][] board = new int[3][3];
        board[x2][y2] = b[x1][y1];
        board[x1][y1] = b[x2][y2];

        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 3; j++) {
            if (i == x1 && j == y1) continue;
            if (i == x2 && j == y2) continue;
            board[i][j] = b[i][j];
          }
        }
        return board;
  }
}
