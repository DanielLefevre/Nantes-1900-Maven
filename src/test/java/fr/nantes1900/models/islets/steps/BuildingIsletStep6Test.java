package fr.nantes1900.models.islets.steps;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Point;

/**
 * Implements a testing class for BuildingIsletStep6.
 * @author Daniel Lefèvre
 */
public class BuildingIsletStep6Test extends TestCase {

    /**
     * Test method for
     * {@link nantes1900.models.islets.steps.BuildingIsletStep6#getCloserProjectedPointOnEdge(fr.nantes1900.models.basis.Point, java.util.List)}
     * .
     */
    @Test
    public static void testGetCloserProjectedPointOnEdge() {
        Point p1 = new Point(0, 0, 0);
        Point p2 = new Point(0, 1, 0);
        Point p3 = new Point(0.5, 1, 0);
        Point p4 = new Point(0.5, 0.5, 0);
        Point p5 = new Point(1, 0.5, 0);
        Point p6 = new Point(1, 0, 0);

        Edge e1 = new Edge(p1, p2);
        Edge e2 = new Edge(p2, p3);
        Edge e3 = new Edge(p3, p4);
        Edge e4 = new Edge(p4, p5);
        Edge e5 = new Edge(p5, p6);
        Edge e6 = new Edge(p6, p1);

        List<Edge> edges = new ArrayList<>();
        edges.add(e1);
        edges.add(e2);
        edges.add(e3);
        edges.add(e4);
        edges.add(e5);
        edges.add(e6);

        Point p = new Point(-1, 0.5, 0);

        Assert.assertTrue(BuildingsIsletStep6.getCloserProjectedPointOnEdge(p,
                edges).equals(new Point(0, 0.5, 0)));
    }
}
