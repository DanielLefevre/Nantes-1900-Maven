package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

import modeles.Edge;
import modeles.Mesh;
import modeles.Point;
import modeles.Triangle;

import org.junit.Test;

/**
 * Set of JUnit4 tests for the class Triangle.
 * @author Daniel Lefevre
 */
public class TriangleTest {

	private Point p1 = new Point(1, 0, -1);
	private Point p2 = new Point(0, 1, 0);
	private Point p3 = new Point(-1, 2, 1);
	private Vector3d vect = new Vector3d(0, 0, 1);
	private Edge e1 = new Edge(p1, p2);
	private Edge e2 = new Edge(p2, p3);
	private Edge e3 = new Edge(p3, p1);
	Triangle t = new Triangle(p1, p2, p3, e1, e2, e3, vect);

	/**
	 * Test of the method getPoints.
	 */
	@Test
	public void testGetPoints() {
		ArrayList<Point> pointList = new ArrayList<Point>(t.getPoints());
		assertTrue(pointList.get(0) == p1);
		assertTrue(pointList.get(1) == p2);
		assertTrue(pointList.get(2) == p3);
	}

	/**
	 * Test of the method contains.
	 */
	@Test
	public void testContains() {
		assertTrue(t.contains(p1));
		assertTrue(t.contains(p2));
		assertTrue(t.contains(p3));
	}

	/**
	 * Test of the method equals.
	 */
	@Test
	public void testEquals() {
		Triangle tBis = new Triangle(p1, p2, p3, e1, e2, e3, vect);
		assertTrue(t.equals(tBis));
		assertTrue(tBis.equals(t));
	}

	/**
	 * Test of the method xAverage.
	 */
	@Test
	public void testXAverage() {
		assertTrue(t.xAverage() == 0);
	}

	/**
	 * Test of the method yAverage.
	 */
	@Test
	public void testYAverage() {
		assertTrue(t.yAverage() == 1);
	}

	/**
	 * Test of the method zAverage.
	 */
	@Test
	public void testZAverage() {
		assertTrue(t.zAverage() == 0);
	}

	/**
	 * Test of the method xMin.
	 */
	@Test
	public void testXMin() {
		assertTrue(t.xMin() == -1);
	}

	/**
	 * Test of the method xMax.
	 */
	@Test
	public void testXMax() {
		assertTrue(t.xMax() == 1);
	}

	/**
	 * Test of the method yMin.
	 */
	@Test
	public void testYMin() {
		assertTrue(t.yMin() == 0);
	}

	/**
	 * Test of the method yMax
	 */
	@Test
	public void testYMax() {
		assertTrue(t.yMax() == 2);
	}

	/**
	 * Test of the method zMin.
	 */
	@Test
	public void testZMin() {
		assertTrue(t.zMin() == -1);
	}

	/**
	 * Test of the method zMax.
	 */
	@Test
	public void testZMax() {
		assertTrue(t.zMax() == 1);
	}

	/**
	 * Test of the method xMinPoint.
	 */
	@Test
	public void testXMinPoint() {
		assertTrue(t.xMinPoint() == p3);
	}

	/**
	 * Test of the method xMaxPoint.
	 */
	@Test
	public void testXMaxPoint() {
		assertTrue(t.xMaxPoint() == p1);
	}

	/**
	 * Test of the method yMinPoint.
	 */
	@Test
	public void testYMinPoint() {
		assertTrue(t.yMinPoint() == p1);
	}

	/**
	 * Test of the method yMaxPoint.
	 */
	@Test
	public void testYMaxPoint() {
		assertTrue(t.yMaxPoint() == p3);
	}

	/**
	 * Test of the method zMinPoint.
	 */
	@Test
	public void testZMinPoint() {
		assertTrue(t.zMinPoint() == p1);
	}

	/**
	 * Test of the method zMaxPoint.
	 */
	@Test
	public void testZMaxPoint() {
		assertTrue(t.zMaxPoint() == p3);
	}

	/**
	 * Test of the method xProjection.
	 */
	@Test
	public void testXProjection() {
		Triangle tProj = t.xProjection(7.0);
		assertTrue(tProj.getP1().getX() == 7.0);
		assertTrue(tProj.getP2().getX() == 7.0);
		assertTrue(tProj.getP3().getX() == 7.0);
	}

	/**
	 * Test of the method yProjection.
	 */
	@Test
	public void testYProjection() {
		Triangle tProj = t.yProjection(7.0);
		assertTrue(tProj.getP1().getY() == 7.0);
		assertTrue(tProj.getP2().getY() == 7.0);
		assertTrue(tProj.getP3().getY() == 7.0);
	}

	/**
	 * Test of the method zProjection.
	 */
	@Test
	public void testzProjection() {
		Triangle tProj = t.zProjection(7.0);
		assertTrue(tProj.getP1().getZ() == 7.0);
		assertTrue(tProj.getP2().getZ() == 7.0);
		assertTrue(tProj.getP3().getZ() == 7.0);
	}

	/**
	 * Test of the method angularTolerance.
	 */
	@Test
	public void testAngularTolerance() {
		Vector3d vector = new Vector3d(0, 1, 0);
		Triangle tBis = new Triangle(p1, p2, p3, e1, e2, e3, vector);

		assertFalse(t.angularTolerance(vector, 60));
		assertFalse(t.angularTolerance(tBis, 60));

		assertTrue(t.angularTolerance(vector, 100));
		assertTrue(t.angularTolerance(tBis, 100));
	}

	/**
	 * Test of the method isNormalTo.
	 */
	@Test
	public void testIsNormalTo() {
		Vector3d vector = new Vector3d(0, 1, 0);
		Vector3d vector2 = new Vector3d(0, 0, 1);

		assertTrue(t.isNormalTo(vector, 1));
		assertFalse(t.isNormalTo(vector2, 0.2));
	}

	/**
	 * Test of the method returnNeighbours.
	 */
	@Test
	public void testReturnNeighbours() {
		Point p4 = new Point(3, 4, 5);
		Point p5 = new Point(-3, -4, -5);
		Point p6 = new Point(-3.5, -1.2, 5.9);
		Edge e4 = new Edge(p1, p4);
		Edge e5 = new Edge(p2, p4);
		Edge e6 = new Edge(p1, p5);
		Edge e7 = new Edge(p2, p5);
		Edge e8 = new Edge(p1, p6);
		Edge e9 = new Edge(p2, p6);
		Edge e10 = new Edge(p5, p6);
		Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
		Triangle t3 = new Triangle(p1, p3, p5, e3, e6, e7, vect);
		Triangle t4 = new Triangle(p2, p3, p6, e2, e8, e9, vect);
		Triangle t5 = new Triangle(p1, p5, p6, e6, e8, e10, vect);

		Mesh contain = new Mesh();
		contain.add(t);
		contain.add(t2);
		contain.add(t3);
		contain.add(t4);
		contain.add(t5);

		//TODO : ajouter un autre triangle qui n'est pas un voisin et tester le résultat.

		Mesh ret = new Mesh();
		t.returnNeighbours(ret, contain);

		assertTrue(ret.contains(t));
		assertTrue(ret.contains(t2));
		assertTrue(ret.contains(t3));
		assertTrue(ret.contains(t4));
		assertTrue(ret.contains(t5));
	}

	/**
	 * Test of the method getNeighbours.
	 */
	@Test
	public void testGetNeighbours() {
		Point p4 = new Point(3, 4, 5);
		Point p5 = new Point(-3, -4, -5);
		Point p6 = new Point(-3.5, -1.2, 5.9);
		Edge e4 = new Edge(p1, p4);
		Edge e5 = new Edge(p2, p4);
		Edge e6 = new Edge(p1, p5);
		Edge e7 = new Edge(p2, p5);
		Edge e8 = new Edge(p1, p6);
		Edge e9 = new Edge(p2, p6);
		Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
		Triangle t3 = new Triangle(p1, p3, p5, e3, e6, e7, vect);
		Triangle t4 = new Triangle(p2, p3, p6, e2, e8, e9, vect);

		Mesh contain = new Mesh();
		contain.add(t);
		contain.add(t2);
		contain.add(t3);
		contain.add(t4);

		ArrayList<Triangle> l = t.getNeighbours();

		assertFalse(l.contains(t));
		assertTrue(l.contains(t2));
		assertTrue(l.contains(t3));
		assertTrue(l.contains(t4));
	}
	
	/**
	 * Test of the method isNeighboor.
	 */
	public void testIsNeighboor() {

		Point p4 = new Point(3, 4, 5);
		Point p5 = new Point(-3, -4, -5);
		Point p6 = new Point(-3.5, -1.2, 5.9);
		Edge e4 = new Edge(p1, p4);
		Edge e5 = new Edge(p2, p4);
		Edge e6 = new Edge(p1, p5);
		Edge e7 = new Edge(p2, p5);
		Edge e8 = new Edge(p1, p6);
		Edge e9 = new Edge(p2, p6);
		Triangle t2 = new Triangle(p1, p2, p4, e1, e4, e5, vect);
		Triangle t3 = new Triangle(p1, p3, p5, e3, e6, e7, vect);
		Triangle t4 = new Triangle(p2, p3, p6, e2, e8, e9, vect);

		assertFalse(t.isNeighboor(t));
		assertTrue(t.isNeighboor(t2));
		assertTrue(t.isNeighboor(t3));
		assertTrue(t.isNeighboor(t4));
	}
}