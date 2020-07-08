package tspdemo.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import tspdemo.HeldKarpSolver;

class SymmetricTspTest {

    private class Node implements HeldKarpSolver.Locatable<Node> {
        Node(int index, int[] distances) {
            this.index = index;
            this.distances = distances;
        }

        private final int index;
        private final int[] distances;

        @Override
        public double distanceTo(Node t) {
            return distances[t.index];
        }
    }

    @Test
    void testFourNodes() {
        List<Node> nodes = Arrays.asList(
                new Node(0, new int[] { 0, 10, 15, 20 }),
                new Node(1, new int[] { 10, 0, 35, 25 }),
                new Node(2, new int[] { 15, 35, 0, 30 }),
                new Node(3, new int[] { 20, 25, 30, 0 })
        );

        HeldKarpSolver.Result<Node> path = HeldKarpSolver.solve(nodes, 0);
        assertEquals(4, path.asList().size());
        assertEquals(80d, path.totalDistance(), 1e-9);
    }

    @Test
    void testFiveNodes() {
        List<Node> nodes = Arrays.asList(
                new Node(0, new int[] {  0, 12, 10, 19,  8 }),
                new Node(1, new int[] { 12,  0,  3,  7,  2 }),
                new Node(2, new int[] { 10,  3,  0,  6, 20 }),
                new Node(3, new int[] { 19,  7,  6,  0,  4 }),
                new Node(4, new int[] {  8,  2, 20,  4,  0 })
        );

        HeldKarpSolver.Result<Node> path = HeldKarpSolver.solve(nodes, 4);
        assertEquals(5, path.asList().size());
        assertEquals(32d, path.totalDistance(), 1e-9);
    }

    @Test
    void testSixNodes() {
        List<Node> nodes = Arrays.asList(
                new Node(0, new int[] {  0, 12, 29, 22, 13, 24 }),
                new Node(1, new int[] { 12,  0, 19,  3, 25,  6 }),
                new Node(2, new int[] { 29, 19,  0, 21, 23, 28 }),
                new Node(3, new int[] { 22,  3, 21,  0,  4,  5 }),
                new Node(4, new int[] { 13, 25, 23,  4,  0, 16 }),
                new Node(5, new int[] { 24,  6, 28,  5, 16,  0 })
        );

        HeldKarpSolver.Result<Node> path = HeldKarpSolver.solve(nodes, 5);
        assertEquals(6, path.asList().size());
        assertEquals(76d, path.totalDistance(), 1e-9);
    }

}
