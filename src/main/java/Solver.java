import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

public class Solver {
    // you will need to add a class for search nodes,
    // and a MinPQ to store them in as you solve
    private class SearchNode implements Comparable<SearchNode> {
        public Board board;
        public int moves;
        public int manhattan;
        public int priority;
        public SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.manhattan = board.manhattan();
            this.priority = moves + manhattan;
        }
        //hi
        @Override
        public int compareTo(SearchNode o) {
            if (this.priority > o.priority) return 1;
            if (this.priority < o.priority) return -1;
            return 0;
        }
    }

    MinPQ<SearchNode> tree = new MinPQ<>();
    SearchNode solution = null;
    boolean isSolvable  = true;
    public Solver(Board initial) {
      // find a solution to the initial board (using the A* algorithm)
        tree.insert(new SearchNode(initial, 0, null));

        while (!tree.isEmpty()) {
            SearchNode current = tree.delMin();

            if (current.board.isGoal()) {
                solution = current;
                return;
            }

            Iterable<Board> neighbors = current.board.neighbors();
            for (Board neighbor : neighbors) {

                if (current.previous == null || !neighbor.equals(current.previous.board)) {
                    SearchNode newNode = new SearchNode(neighbor, current.moves + 1, current);
                    tree.insert(newNode);
                }
            }
        }
        isSolvable = false;
    }

    public boolean isSolvable() {
      // is the initial board solvable?
      return isSolvable;
    }

    public int moves() {
      // min number of moves to solve initial board; -1 if unsolvable
        if (!isSolvable) return -1;
      return solution.moves;
    }

    public Iterable<Board> solution() {
      // sequence of boards in a shortest solution; null if unsolvable
        if (!isSolvable) return null;

        ArrayList<Board> solutionList = new ArrayList<>();
        SearchNode temp = solution;
        while (temp != null) {
            solutionList.add(temp.board);
            if (temp.previous != null) {
                temp = temp.previous;
            }
        }
      return solutionList;
    }

    public static void main(String[] args) {
      // solve a slider puzzle (given below)
    }
}
