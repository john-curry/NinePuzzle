package assignment;

public class Graph {
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


  public static Graph BuildNinePuzzle() {

    Graph ninegraph = new Graph(NUM_BOARDS);  
    int vs = ninegraph.numVertices();

    // iterate over all the vertices to find out the edges
    int px, py = -1; // the position of the zero on the board

    for (int v = 0; v < vs; v++) {
      int[][] board = getBoardFromIndex(v); // get which vertex we are on

      // find the zero in the board
      for (int i = 0; i < 3; i++) { 
        for (int j = 0; j < 3; j++) {
          if (board[i][j] == 0) px = i; py = j; 
        }
      }
      assert(px != -1 || py != -1);

      if (px > 0) ninegraph.addEdge(v, getIndexFromBoard(swap(px - 1, py)));
      if (px < 3) ninegraph.addEdge(v, getIndexFromBoard(swap(px + 1, py)));
      if (py > 0) ninegraph.addEdge(v, getIndexFromBoard(swap(px, py - 1)));
      if (py < 3) ninegraph.addEdge(v, getIndexFromBoard(swap(px, py + 1)));
    }
  }
  // =========== swap ===========================================================
  // swap two places in a board
  // copies everything else
  // ============================================================================
  public static int[][] swap(int x1, int y1, int x2, int y2, int[][] b) {
        int[][] board = new board[3][3];
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
