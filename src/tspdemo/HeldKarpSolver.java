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
        /** Computes the distance to another object of the same type. */
        double distanceTo(T t);
    }

    /**
     * The iterable result of solving a TSP.
     */
    public static class Result<T> implements Iterable<T> {
        private double totalDistance;
        private List<T> path;

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

        // First, compute the adjacency matrix.
        double adjacency[][] = new double[nNodes][nNodes];
        for (int i = 0; i < nNodes; i++) {
            for (int j = 0; j < nNodes; j++) {
                adjacency[i][j] = nodes.get(i).distanceTo(nodes.get(j));
            }
        }

        // Now initialize the cost and predecessor matrices.
        int predecessor[][] = new int[1 << nNodes][nNodes];
        double cost[][] = new double[1 << nNodes][nNodes];
        for (int node = 0; node < nNodes; node++) {
            cost[1 << node][node] = adjacency[startNode][node];
        }

        // Dynamic programming: Find optimal solutions for smaller sub-problems.
        for (int s = 2; s <= nNodes; s++) {
            for (int S = (1 << s) - 1; S < (1 << nNodes); S++) {
                if (Integer.bitCount(S) == s && ((S & (1 << startNode)) == 0) || s == nNodes) {
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
        int backK = startNode, backS = (1 << nNodes) - 1;
        double minDistance = cost[backS][backK];
        List<T> result = new LinkedList<>();
        for (int i = 0; i < nNodes; i++) {
            result.add(0, nodes.get(backK));
            int oldS = backS;
            backS = backS & ~(1 << backK);
            backK = predecessor[oldS][backK];
        }
        return new Result<>(minDistance, result);
    }

}
