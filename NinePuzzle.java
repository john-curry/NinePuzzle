import java.util.*;
import java.io.File;
import java.lang.Integer;

public class NinePuzzle{

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
  public static final boolean DEBUG    = false;
  public static final boolean DEBUG_L2 = false;
  public static int nodesVisited = 0;
  public static int addedEdges = 0;
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
      Queue<Integer> yettovisit = new LinkedList<Integer>();
      int[] nodeTo = new int[NUM_BOARDS];

      for (int i = 0; i < NUM_BOARDS; i++) { // two initializations for the price of one!
        nodeTo[i] = -1;
        visited[i] = false;
      }
      for (int i : g.adjacencyList(b)) {
        if (visited[i] == false) {
          yettovisit.add(i);
        }
      }
      while (yettovisit.peek() != null) {
        visited[b] = true;
        if (b == getIndexFromBoard(SOLVED)) {
          int prev = nodeTo[b]; 
          while (prev != -1) {
            NinePuzzle.printBoard(getBoardFromIndex(prev));
            prev = nodeTo[prev];
          }
          return true;
        }
        int next = yettovisit.remove();
        for (int i : g.adjacencyList(next)) {
          if (visited[i] == false) {
            yettovisit.add(i);
            nodeTo[i] = b;
          }
        }
        b = next;
      }
      return false;
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
      System.out.println("Visited " + nodesVisited + " nodes and created " + addedEdges + "edges");
    }


  static class Graph {
      public static final int PUZZLE = 9;
      public static final int row = 3;
      public static final int col = 3;

      private int edgesadded = 0;
      private int vertices;
      private ArrayList<LinkedHashSet<Integer>> graph;

      public Graph(int v) { 
        assert v > 0 : "number of vertices should not be non-zero";
        vertices = v;
        graph = new ArrayList<LinkedHashSet<Integer>>(v);
        for (int i = 0; i < v; i++) {
          graph.add(new LinkedHashSet<Integer>());
        }

      }

      public LinkedHashSet<Integer> adjacencyList(int v) {
        assert (v>-1)&&(v<vertices) : "Vertex not in graph. " + v;
        LinkedHashSet<Integer> alist = graph.get(v);
        assert alist.size() < 5: "More vertices in adjacency list then reasonable. Probably duplicates.";
        assert !alist.contains(new Integer(v)) : "Somehow you got duplicates.";
        return alist;

      }
      
    // returns false if edge already exists
    public boolean addEdge(int rv1, int rv2) { // the r is for raw
      assert NinePuzzle.isConnected(rv1, rv2) : "You are trying to add edges that are not connected";
      assert rv1 != rv2 : "adding and edge between a vertex and itself";
      assert (rv1 < vertices) && (rv2 < vertices) : "Paranioa is kicking in.";
      Integer v1 = new Integer(rv1);
      Integer v2 = new Integer(rv2);

      assert (graph.get(v1) != null) || (graph.get(v2) != null) : "Classic example of defensive programming.";
      graph.get(rv1).add(v2);
      graph.get(rv2).add(v1);
      addedEdges++;
      return true;
    }
    public int numVertices() { return vertices; }
    public int edgesAdded() { return edgesadded; }
}

  // determines whether or not two vertices should be connected or not
  // only two spaces in the board should be different
  // and they should be right next to each other
  // an eye bleeder of a method that is not gauranteed to work
  public static boolean isConnected(int v1, int v2) {
    boolean connected = false;
    int [][] b1 = getBoardFromIndex(v1);
    int [][] b2 = getBoardFromIndex(v2);
    // two graphs are connected if they have all two spots different, one of them is zero and the two spots have the same value.
    int diffcounter = 0;
    for (int i = 0; i < 3; i++) { 
      for (int j = 0; j < 3; j++) {
        if (b1[i][j] != b2[i][j]) {
          diffcounter++;
        }
      }
    }
    if (diffcounter == 2) {
      int x1 = -1;
      int x2 = -1;
      int y1 = -1;
      int y2 = -1;

      for (int i = 0; i < 3; i++) { 
        for (int j = 0; j < 3; j++) {
          if (b1[i][j] != b2[i][j]) {
            x1 = i; y1 = j;
          }
        }
      }
      for (int i = 2; i >= 0; i--) { 
        for (int j = 2; j >= 0; j--) {
          if ((b1[i][j] != b2[i][j])) {
            x2 = i; y2 = j;
          }
        }
      }

      boolean haszero = (b1[x1][y1] == 0) ^ (b1[x2][y2] == 0);
        haszero = haszero && (b2[x1][y1] == 0) ^ (b2[x2][y2] == 0);
      
      if (!haszero) connected = false;
      
      int value = -1;

      if (b1[x1][y1] == 0) {
        value = b1[x2][y2];
      } else {
        value = b1[x1][y1];
      }

      if (!((b2[x1][y1] == value) ^ (b2[x2][y2] == value))) connected = false;

      if (!((Math.abs(x1 - x2) == 1) ^ (Math.abs(y1 - y2) == 1))) connected  = false;

      if (!((Math.abs(x1 - x2) == 0) ^ (Math.abs(y1 - y2) == 0))) connected = false;
      
      connected = true;
    }
    if (!connected) {
      System.out.println("These boards are not connected!");
      NinePuzzle.printBoard(b1);
      NinePuzzle.printBoard(b2);
      throw new RuntimeException("BAD VERTICES. YOU DO NOT GET TO BE CONNECTED");
    }
    return connected;
  }
    
  public static Graph BuildNinePuzzle() {

    Graph ninegraph = new Graph(NUM_BOARDS);  
    int vs = ninegraph.numVertices();

    // iterate over all the vertices to find out the edges
    for (int v = 0; v < vs; v++) {
      int px = -1; // the position of the zero on the board
      int py = -1;

      int[][] board = getBoardFromIndex(v); // get which vertex we are on

      // find the zero in the board
      for (int i = 0; i < 3; i++) { 
        for (int j = 0; j < 3; j++) {
          if (board[i][j] == 0) { px = i; py = j; }
        }
      }
      assert(px > -1 && py > -1) : "position of the zero should non-negative:" + px + " " + py;
      assert(px < 3 && py < 3) : "position of the zero should be less then 3: " + px + " " + py;
      assert(board[px][py] == 0) : "px and py do not point at the zero on the board";
      if (px > 0) {
        int[][] b = new int[3][3];
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 3; j++) {
            b[i][j] = board[i][j];
          }
        }
        b[px - 1][py] = board[px][py];
        b[px][py] = board[px - 1][py];
        ninegraph.addEdge(v, getIndexFromBoard(b)); 
        assert(b[px - 1][py] == 0 && b[px][py] != 0);
      }
      if (px < 2) {
        int[][] b = new int[3][3];
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 3; j++) {
            b[i][j] = board[i][j];
          }
        }
        b[px + 1][py] = board[px][py];
        b[px][py] = board[px + 1][py];
        ninegraph.addEdge(v, getIndexFromBoard(b)); 
        assert(b[px + 1][py] == 0 && b[px][py] != 0);
      }
      if (py < 2) {
        int[][] b = new int[3][3];
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 3; j++) {
            b[i][j] = board[i][j];
          }
        }
        b[px][py + 1] = board[px][py];
        b[px][py] = board[px][py + 1];
        ninegraph.addEdge(v, getIndexFromBoard(b)); 
        assert((b[px][py + 1]) == 0 && (b[px][py] != 0));
      }
      if (py > 0) {
        int[][] b = new int[3][3];
        for (int i = 0; i < 3; i++) {
          for (int j = 0; j < 3; j++) {
            b[i][j] = board[i][j];
          }
        }
        b[px][py - 1] = board[px][py];
        b[px][py] = board[px][py - 1];
        ninegraph.addEdge(v, getIndexFromBoard(b)); 
        assert(b[px][py - 1] == 0 && b[px][py] != 0);
      }
      if (px > 0 && px < 2 && py > 0 && py < 2) {
        assert ninegraph.adjacencyList(v).size() == 4;
      }
      if (px == 0 && py == 0) {
        assert ninegraph.adjacencyList(v).size() == 2;
      }
      if (px == 2 && py == 0) {
        assert ninegraph.adjacencyList(v).size() == 2;
      }
      if (px == 0 && py == 2) {
        assert ninegraph.adjacencyList(v).size() == 2;
      }
      if (px == 2 && py == 2) {
        assert ninegraph.adjacencyList(v).size() == 2;
      }
      if (px == 0 && py == 1) {
        assert ninegraph.adjacencyList(v).size() == 3;
      }
      if (px == 1 && py == 0) {
        assert ninegraph.adjacencyList(v).size() == 3;
      }
      if (px == 2 && py == 1) {
        assert ninegraph.adjacencyList(v).size() == 3;
      }
      if (px == 1 && py == 2) {
        assert ninegraph.adjacencyList(v).size() == 3;
      }
    }
    if (DEBUG_L2) {
      for (int i = 0; i < NUM_BOARDS; i++) {
        for (Integer j : ninegraph.adjacencyList(i)) {
          assert isConnected(i,j.intValue()): "Vertex " + i + " should not be connected to vertex " + j.intValue();
        }
      }
    }
    return ninegraph;
  }
}
