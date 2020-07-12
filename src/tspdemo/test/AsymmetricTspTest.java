package tspdemo.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import tspdemo.HeldKarpSolver;

class AsymmetricTspTest {

    private static void test(int distanceMatrix[][],
                             int correctTotalDistance,
                             int[] correctOrder) {
        int nNodes = distanceMatrix.length;

        class Node implements HeldKarpSolver.Locatable<Node> {
            Node(int index) {
                this.index = index;
            }

            final int index;

            @Override
            public double distanceTo(Node t) {
                return distanceMatrix[this.index][t.index];
            }
        }

        // Create four nodes representing the distance matrix above.
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < nNodes; i++)
            nodes.add(new Node(i));

        // Solve the problem.
        HeldKarpSolver.Result<Node> path = HeldKarpSolver.solve(nodes, 0);
        assertEquals(nNodes, path.asList().size());
        assertEquals(correctTotalDistance, path.totalDistance(), 1e-9);

        // Make sure that the total distance is correct.
        double distance = 0;
        Node lastNode = nodes.get(0);
        for (Node node : path) {
            distance += lastNode.distanceTo(node);
            lastNode = node;
        }
        assertEquals(correctTotalDistance, distance, 1e-9);

        // Make sure the nodes are in the correct oder.
        for (int i = 0; i < nNodes; i++)
            assertSame(nodes.get(correctOrder[i]), path.asList().get(i));
    }

    @Test
    void test() {
        // Trivial examples:
        test(new int[][] {
                { 0, 1, 100 },
                { 100, 0, 1 },
                { 1, 100, 0 }
        }, 3, new int[] { 1, 2, 0 });

        test(new int[][] {
                { 0, 100, 1 },
                { 1, 0, 100 },
                { 100, 1, 0 }
        }, 3, new int[] { 2, 1, 0 });

        test(new int[][] {
                { 0, 100, 100, 1 },
                { 1, 0, 50, 100 },
                { 1, 100, 0, 100 },
                { 100, 1, 100, 0 }
        }, 53, new int[] { 3, 1, 2, 0 });

        // Example from the Wikipedia article about the Held-Karp algorithm:
        test(new int[][] {
                {  0,  2,  9, 10 },
                {  1,  0,  6,  4 },
                { 15,  7,  0,  8 },
                {  6,  3, 12,  0 }
        }, 21, new int[] { 2, 3, 1, 0 });
    }

}
