import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.lang.StringBuilder;
import java.util.ArrayList;

public class Board {
    private final int[][] blocks;
    private final int n;
    public Board(int[][] blocks) {

        this.n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.blocks[i][j] = blocks[i][j];
            }
        }
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        // suggestions for immutability in the Binary Heap video
    }

    public int dimension() {
        // board dimension n
        return n;
    }

    public int hamming() {
        // number of blocks out of place
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int block = blocks[i][j];
                if (block != 0 && block != i * n + j + 1) {
                    count++;
                }
            }
        }
        return count;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        int count = 0;
        int rowOfGoal;
        int colOfGoal;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int block = blocks[i][j];
                if (block != 0) {
                    rowOfGoal = (int) ((block - 1) / n);
                    colOfGoal = (int) ((block - 1) % n);
                    //praise intelliJ autofill for giving me this logic

                    count += (Math.abs(i - rowOfGoal) + Math.abs(j - colOfGoal));
                }

            }
        }
        return count;
    }

    public String toString() {
        // string representation of this board
        // for example
        // 3
        //  1 0 3
        //  4 2 5
        //  7 8 6

        StringBuilder stringRepresentation = new StringBuilder();
        //constructs string

        stringRepresentation.append(n);
        stringRepresentation.append("\n");
        //sets up the size of the visualization

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                stringRepresentation.append(blocks[i][j] + " ");
                //adds each element to string
            }
            stringRepresentation.append("\n");
            //line break every row
        }
        return stringRepresentation.toString();
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (this.n != ((Board) y).n) {
            return false;
        }
        boolean returnBoolean = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.blocks[i][j] != ((Board) y).blocks[i][j]) {
                    returnBoolean = false;
                }
            }
        }
        return returnBoolean;
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int[][] twinBuilder = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinBuilder[i][j] = blocks[i][j];
            }
        }

        if (twinBuilder[0][0] == 0 || twinBuilder[0][1] == 0) {
            int temp = twinBuilder[1][0];
            twinBuilder[1][0] = twinBuilder[1][1];
            twinBuilder[1][1] = temp;
        } else {
            int temp = twinBuilder[0][0];
            twinBuilder[0][0] = twinBuilder[0][1];
            twinBuilder[0][1] = temp;
        }
        return new Board(twinBuilder);
    }

    public boolean isGoal() {
        // is this board the goal board?
        int expectedValue = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    if (blocks[i][j] != 0) {
                        return false;
                    }
                } else if (blocks[i][j] != expectedValue) {
                    return false;
                }
                expectedValue++;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        // holds all neighbors found
        ArrayList<Board> neighbors = new ArrayList<>();

        //finds the location of the empty space
        int mainRow = 0;
        int mainCol = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    mainRow = i;
                    mainCol = j;
                }
            }
        }
        //checks and then makes left neighbor
        if (mainRow - 1 >= 0) {
            int [][] leftBoard = duplicateBoard();
            movePiece(leftBoard, mainRow, mainCol, mainRow - 1, mainCol);
            neighbors.add(new Board(leftBoard));
        }
        //checks and then makes right neighbor
        if (mainRow + 1 < n) {
            int [][] rightBoard = duplicateBoard();
            movePiece(rightBoard, mainRow, mainCol, mainRow + 1, mainCol);
            neighbors.add(new Board(rightBoard));
        }
        //checks and then makes up neighbor
        if (mainCol - 1 >= 0) {
            int [][] upBoard = duplicateBoard();
            movePiece(upBoard, mainRow, mainCol, mainRow, mainCol - 1);
            neighbors.add(new Board(upBoard));
        }
        //checks and then makes down neighbor
        if (mainCol + 1 < n) {
            int [][] downBoard = duplicateBoard();
            movePiece(downBoard, mainRow, mainCol, mainRow, mainCol + 1);
            neighbors.add(new Board(downBoard));
        }

        return neighbors;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In("./8puzzle-test-files/puzzle3x3-07.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    private void movePiece(int[][] board, int rowOld, int colOld, int rowNew, int colNew) {
        int temp = board[rowOld][colOld];
        board[rowOld][colOld] = board[rowNew][colNew];
        board[rowNew][colNew] = temp;
    }
    private int[][] duplicateBoard() {
        int[][] newBoard = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newBoard[i][j] = blocks[i][j];
            }
        }
        return newBoard;
    }
}
