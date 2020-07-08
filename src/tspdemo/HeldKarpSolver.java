package tspdemo;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Uses the Held-Karp algorithm to find an optimal solution to a symmetric
 * or asymmetric TSP. Since TSP is NP hard, this algorithm takes exponential
 * time.
 *
 * @author Tobias Nießen
 */
public class HeldKarpSolver {

    /** Something that can be located (relative to another "something"). */
    public interface Locatable<T extends Locatable<T>> {
        /**
         * Computes the distance to another object of the same type.
         *
         * @param t The target object.
         * @return The distance (can also be <code>0</code> or
         *         <code>POSITIVE_INFINITY</code>).
         */
        double distanceTo(T t);
    }

    /**
     * The iterable result of solving a TSP.
     */
    public static class Result<T> implements Iterable<T> {
        private final double totalDistance;
        private final List<T> path;

        private Result(double totalDistance, List<T> path) {
            this.totalDistance = totalDistance;
            this.path = path;
        }

        /**
         * @return the nodes on the optimal path, in order
         */
        public List<T> asList() {
            return Collections.unmodifiableList(path);
        }

        /**
         * @return the total distance of the optimal path
         */
        public double totalDistance() {
            return totalDistance;
        }

        @Override
        public Iterator<T> iterator() {
            return path.iterator();
        }
    }

    /**
     * Applies the Held-Karp algorithm to a list of {@link Locatable} nodes.
     *
     * @param nodes The list of nodes.
     * @param startNode The index of the start node.
     * @return The computed optimal path.
     */
    public static <T extends Locatable<T>> Result<T> solve(List<T> nodes, int startNode) {
        final int nNodes = nodes.size();

        // First, compute the adjacency matrix. If the TSP is symmetric,
        // the adjacency matrix will be symmetric, otherwise, it will be
        // asymmetric.
        double adjacency[][] = computeAdjacencyMatrix(nodes);

        // Now initialize the cost and predecessor matrices.
        // The first index ("row") of the matrix represents a subset S of all
        // nodes. The second index ("column") of the matrix is the index k of
        // a single node.
        // The value cost[S][k] is the total distance of the optimal path from
        // the startNode that visits each node in S exactly once, and ends in k.
        // The predecessor matrix works the same way, but instead of containing
        // the distance, it tracks the second-to-last node in S, let's call it m.
        int predecessor[][] = new int[1 << nNodes][nNodes];
        double cost[][] = new double[1 << nNodes][nNodes];
        for (int node = 0; node < nNodes; node++) {
            cost[1 << node][node] = adjacency[startNode][node];
        }

        // Dynamic programming: Find optimal solutions for smaller sub-problems.
        for (int s = 2; s <= nNodes; s++) {
            // This loop (and the following condition) iterate over all sets S of size s.
            // The goal is to compute the row [S]. Note that all rows [S'] with |S'| < s
            // have already been computed.
            for (int S = (1 << s) - 1; S < (1 << nNodes); S++) {
                if (Integer.bitCount(S) == s && ((S & (1 << startNode)) == 0) || s == nNodes) {
                    // For each node k in S, compute the entry [S][k], using the
                    // row [S\{k}]. Since k is in S, |S\{k}| < |S| holds, so
                    // [S\{k}] has already been computed in a previous iteration
                    // (or during initialization).
                    for (int k = 0; k < nNodes; k++) {
                        if ((S & (1 << k)) != 0) {
                            cost[S][k] = Double.POSITIVE_INFINITY;
                            for (int m = 0; m < nNodes; m++) {
                                if (m != k && (S & (1 << m)) != 0) {
                                    double c = cost[S & ~(1 << k)][m] + adjacency[m][k];
                                    if (c < cost[S][k]) {
                                        cost[S][k] = c;
                                        predecessor[S][k] = m;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Reconstruct the optimal path through the predecessor matrix.
        return reconstructResult(nodes, startNode, cost, predecessor);
    }

    /**
     * Computes the adjacency matrix for a list of nodes.
     *
     * @param nodes The list of {@link Locatable} nodes.
     * @return An adjacency matrix.
     */
    private static <T extends Locatable<T>> double[][] computeAdjacencyMatrix(List<T> nodes) {
        int n = nodes.size();
        double adjacency[][] = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacency[i][j] = nodes.get(i).distanceTo(nodes.get(j));
            }
        }
        return adjacency;
    }

    /**
     * Reconstructs the {@link Result} of the Held-Karp algorithm based
     * on computed cost and predecessor matrices.
     *
     * @param nodes The original list of nodes
     * @param nodeIndex The start node index
     * @param cost The computed cost matrix
     * @param predecessor The computed predecessor matrix
     * @return The {@link Result} of the Held-Karp algorithm
     */
    private static <T extends Locatable<T>> Result<T> reconstructResult(
            List<T> nodes, int nodeIndex,
            double[][] cost, int[][] predecessor) {
        int setIndex = (1 << nodes.size()) - 1;
        double minDistance = cost[setIndex][nodeIndex];
        List<T> result = new LinkedList<>();
        for (int i = 0; i < nodes.size(); i++) {
            result.add(0, nodes.get(nodeIndex));
            int prevSetIndex = setIndex;
            setIndex = setIndex & ~(1 << nodeIndex);
            nodeIndex = predecessor[prevSetIndex][nodeIndex];
        }
        return new Result<>(minDistance, result);
    }

}
