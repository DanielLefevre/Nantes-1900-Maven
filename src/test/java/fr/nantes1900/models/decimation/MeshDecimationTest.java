package fr.nantes1900.models.decimation;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.utils.ParserSTL;

/**
 * Implements test on the class MeshDecimation.
 * @author Daniel Lefevre
 */
public class MeshDecimationTest extends TestCase {

    /**
     * Integration test method for the
     * fr.nantes1900.models.decimation.MeshDecimation and
     * fr.nantes1900.models.decimation.Decimator classes.
     */
    @Test
    public static final void testDecimation() {
        ParserSTL parser = new ParserSTL("files/tests/testDecim.stl");

        try {
            MeshDecimation mesh = new MeshDecimation(parser.read());

            // 1. Compute the Qi matrices for each vi.
            mesh.computeQiMatrices();

            // 2. Select all valid pairs.
            mesh.selectValidPairs();

            // 3. Compute errors for all valid pairs.
            mesh.computeErrors();

            // 4. Sort valid pairs.
            Edge edge = mesh.selectMinimalErrorPair();

            // Test part.
            List<Triangle> triangles = edge.getTriangles();
            // End test part.

            // 5. Collapse the pair with the less cost.
            List<Edge> edges = mesh.collapseMinusCostPair(edge,
                    mesh.computeNewVertex(edge));

            // 6. Recomputes every errors.
            mesh.updateMatricesAndErrors(edges);

            // Test part.
            Assert.assertTrue(mesh.size() == 8);
            Assert.assertFalse(mesh.contains(triangles.get(0)));
            Assert.assertFalse(mesh.contains(triangles.get(1)));
            // End test part.

        } catch (IOException e) {
            Assert.fail();
        }
    }
}
