package tspdemo.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import tspdemo.HeldKarpSolver;

class AsymmetricTspTest {

    @Test
    void testWikipediaExample() {
        // Example from the Wikipedia article about the Held-Karp algorithm:

        final int distanceMatrix[][] = {
                {  0,  2,  9, 10 },
                {  1,  0,  6,  4 },
                { 15,  7,  0,  8 },
                {  6,  3, 12,  0 }
        };

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
        for (int i = 0; i < 4; i++)
            nodes.add(new Node(i));

        // Solve the problem.
        HeldKarpSolver.Result<Node> path = HeldKarpSolver.solve(nodes, 0);
        assertEquals(4, path.asList().size());
        assertEquals(21d, path.totalDistance(), 1e-9);

        // Make sure that the total distance is correct.
        double distance = 0;
        Node lastNode = nodes.get(0);
        for (Node node : path)
            distance += lastNode.distanceTo(node);
        assertEquals(21d, distance, 1e-9);

        // Make sure the nodes are in the correct oder.
        assertSame(nodes.get(1), path.asList().get(0));
        assertSame(nodes.get(3), path.asList().get(1));
        assertSame(nodes.get(2), path.asList().get(2));
        assertSame(nodes.get(0), path.asList().get(3));
    }

}
