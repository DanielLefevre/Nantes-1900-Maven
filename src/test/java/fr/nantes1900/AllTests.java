package fr.nantes1900;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.nantes1900.models.EdgeTest;
import fr.nantes1900.models.MeshTest;
import fr.nantes1900.models.PointTest;
import fr.nantes1900.models.PolygonTest;
import fr.nantes1900.models.TriangleTest;
import fr.nantes1900.models.decimation.MeshDecimationTest;
import fr.nantes1900.models.islets.steps.BuildingIsletStep6Test;
import fr.nantes1900.utils.MatrixMethodTest;
import fr.nantes1900.utils.ParserSTLTest;

/**
 * Class to test every class tests of the project.
 * @author Daniel Lefevre
 */
@RunWith(Suite.class)
@SuiteClasses(value = {EdgeTest.class, PointTest.class, TriangleTest.class,
        PolygonTest.class, MeshTest.class, MatrixMethodTest.class,
        ParserSTLTest.class, MeshDecimationTest.class,
        BuildingIsletStep6Test.class})
public final class AllTests {

    /**
     * Private constructor.
     */
    private AllTests() {
    }
}
