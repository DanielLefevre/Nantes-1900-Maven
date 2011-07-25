package nantes1900.tests.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.vecmath.Vector3d;

import nantes1900.models.Mesh;
import nantes1900.models.Polyline;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;

import org.junit.Test;

/**
 * A set of tests for the class Mesh.
 * 
 * @author Daniel Lefevre
 * 
 */
public class MeshTest {

	private Point p1 = new Point(1, 0, -1);
	private Point p2 = new Point(0, 1, 0);
	private Point p3 = new Point(-1, 2, 1);
	private Vector3d vect1 = new Vector3d(0, 0, 1);
	private Edge e1 = new Edge(p1, p2);
	private Edge e2 = new Edge(p2, p3);
	private Edge e3 = new Edge(p3, p1);
	private Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect1);

	private Point p4 = new Point(4, 5, 4);
	private Point p5 = new Point(2, -3, -3);
	private Point p6 = new Point(-2, 4, -5);
	private Vector3d vect2 = new Vector3d(1, 0, 0);
	private Edge e4 = new Edge(p4, p5);
	private Edge e5 = new Edge(p5, p6);
	private Edge e6 = new Edge(p6, p4);
	private Triangle t2 = new Triangle(p4, p5, p6, e4, e5, e6, vect2);

	private Mesh m = new Mesh();

	/**
	 * Constructor of the MeshTest class : creating the mesh which will be an
	 * example.
	 */
	public MeshTest() {
		this.m.add(this.t1);
		this.m.add(this.t2);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#averageNormal()}.
	 */
	@Test
	public void testAverageNormal() {
		assertTrue(this.m.averageNormal().equals(new Vector3d(0.5, 0, 0.5)));
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#xAverage()}.
	 */
	@Test
	public void testxAverage() {
		assertTrue(this.m.xAverage() == 2.0 / 3.0);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#yAverage()}.
	 */
	@Test
	public void testyAverage() {
		assertTrue(this.m.yAverage() == 3.0 / 2.0);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#zAverage()}.
	 */
	@Test
	public void testzAverage() {
		assertTrue(this.m.zAverage() == -2.0 / 3.0);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#xMax()}.
	 */
	@Test
	public void testxMax() {
		assertTrue(this.m.xMax() == 4);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#xMin()}.
	 */
	@Test
	public void testxMin() {
		assertTrue(this.m.xMin() == -2);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#yMax()}.
	 */
	@Test
	public void testyMax() {
		assertTrue(this.m.yMax() == 5);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#yMin()}.
	 */
	@Test
	public void testyMin() {
		assertTrue(this.m.yMin() == -3);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#zMax()}.
	 */
	@Test
	public void testzMax() {
		assertTrue(this.m.zMax() == 4);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#zMin()}.
	 */
	@Test
	public void testzMin() {
		assertTrue(this.m.zMin() == -5);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#xLengthAverage()}.
	 */
	@Test
	public void testxLengthAverage() {
		assertTrue(this.m.xLengthAverage() == 4);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#yLengthAverage()}.
	 */
	@Test
	public void testyLengthAverage() {
		assertTrue(this.m.yLengthAverage() == 5);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#zLengthAverage()}.
	 */
	@Test
	public void testzLengthAverage() {
		assertTrue(this.m.zLengthAverage() == 5.5);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#xBetween(double, double)}.
	 */
	@Test
	public void testXBetween() {
		Mesh xBet = m.xBetween(10, -10);
		assertTrue(xBet.contains(t1));
		assertTrue(xBet.contains(t2));
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#yBetween(double, double)}.
	 */
	@Test
	public void testYBetween() {
		Mesh yBet = m.yBetween(10, -10);
		assertTrue(yBet.contains(t1));
		assertTrue(yBet.contains(t2));
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#zBetween(double, double)}.
	 */
	@Test
	public void testZBetween() {
		Mesh zBet = m.zBetween(10, -10);
		assertTrue(zBet.contains(t1));
		assertTrue(zBet.contains(t2));
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#zMinFace()}.
	 */
	@Test
	public void testZMinFace() {
		assertTrue(m.zMinFace() == t2);
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#faceUnderZ(double)}.
	 */
	@Test
	public void testFaceUnderZ() {
		assertTrue(m.faceUnderZ(2) == t1);
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Mesh#orientedAs(javax.vecmath.Vector3d, double)}.
	 */
	@Test
	public void testOrientedAs() {
		Mesh oriented = m.orientedAs(new Vector3d(0.1, 0.1, 0.9), 15);
		assertTrue(oriented.contains(t1));
		assertFalse(oriented.contains(t2));
	}

	/**
	 * Test method for
	 * {@link nantes1900.models.Mesh#orientedNormalTo(javax.vecmath.Vector3d, double)} .
	 */
	@Test
	public void testOrientedNormalTo() {
		Mesh oriented = m.orientedNormalTo(new Vector3d(0.1, 0.1, 0.9), 0.2);
		assertTrue(oriented.contains(t2));
		assertFalse(oriented.contains(t1));
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#returnUnsortedBounds()}.
	 */
	@Test
	public void testReturnUnsortedBounds() {
		// We create points, but no matter their coordinates, if they are not
		// equals.
		Point p1 = new Point(1, 0, -1);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 1);
		Point p4 = new Point(1, 1, 1);
		Point p5 = new Point(2, 2, 2);
		Point p6 = new Point(-1, 3, 1);
		Point p7 = new Point(-2, 1, 0);
		Point p8 = new Point(3, 4, 5);
		Point p9 = new Point(3, -2, -2);

		Vector3d vect = new Vector3d(0, 0, 1);

		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);
		Edge e4 = new Edge(p1, p4);
		Edge e5 = new Edge(p3, p4);
		Edge e6 = new Edge(p4, p5);
		Edge e7 = new Edge(p1, p5);

		Edge e8 = new Edge(p1, p6);
		Edge e9 = new Edge(p5, p6);
		Edge e10 = new Edge(p6, p7);
		Edge e11 = new Edge(p6, p9);
		Edge e12 = new Edge(p7, p9);
		Edge e13 = new Edge(p8, p9);
		Edge e14 = new Edge(p7, p8);
		Edge e15 = new Edge(p2, p7);
		Edge e16 = new Edge(p1, p7);
		Edge e17 = new Edge(p2, p8);

		Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect);
		Triangle t2 = new Triangle(p1, p3, p4, e3, e4, e5, vect);
		Triangle t3 = new Triangle(p1, p4, p5, e4, e6, e7, vect);
		Triangle t4 = new Triangle(p1, p5, p6, e7, e8, e9, vect);
		Triangle t5 = new Triangle(p1, p6, p7, e8, e10, e16, vect);
		Triangle t6 = new Triangle(p6, p7, p9, e10, e11, e12, vect);
		Triangle t7 = new Triangle(p7, p8, p9, e12, e13, e14, vect);
		Triangle t8 = new Triangle(p2, p7, p8, e14, e15, e17, vect);

		Mesh mesh = new Mesh();
		mesh.add(t1);
		mesh.add(t2);
		mesh.add(t3);
		mesh.add(t4);
		mesh.add(t5);
		mesh.add(t6);
		mesh.add(t7);
		mesh.add(t8);

		Polyline bounds = mesh.returnUnsortedBounds();
		assertTrue(bounds.contains(p1));
		assertTrue(bounds.contains(p2));
		assertTrue(bounds.contains(p3));
		assertTrue(bounds.contains(p4));
		assertTrue(bounds.contains(p5));
		assertTrue(bounds.contains(p6));
		assertTrue(bounds.contains(p7));
		assertTrue(bounds.contains(p8));
		assertTrue(bounds.contains(p9));

		assertTrue(bounds.contains(e1));
		assertTrue(bounds.contains(e2));
		assertFalse(bounds.contains(e3));
		assertFalse(bounds.contains(e4));
		assertTrue(bounds.contains(e5));
		assertTrue(bounds.contains(e6));
		assertFalse(bounds.contains(e7));
		assertFalse(bounds.contains(e8));
		assertTrue(bounds.contains(e9));
		assertFalse(bounds.contains(e10));
		assertTrue(bounds.contains(e11));
		assertFalse(bounds.contains(e12));
		assertTrue(bounds.contains(e13));
		assertFalse(bounds.contains(e14));
		assertTrue(bounds.contains(e15));
		assertTrue(bounds.contains(e16));
		assertTrue(bounds.contains(e17));
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#remove(nantes1900.models.Mesh)}.
	 */
	@Test
	public void testRemoveMesh() {
		Mesh m1 = new Mesh(this.m);
		Mesh m2 = new Mesh();
		m2.add(t1);

		m1.remove(m2);

		assertTrue(m1.contains(t2));
	}

	/**
	 * Test method for {@link nantes1900.models.Mesh#changeBase(double[][])}.
	 */
	@Test
	public void testChangeBase() {
		fail("Not yet implemented"); // TODO
	}
}