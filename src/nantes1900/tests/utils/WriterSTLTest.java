package nantes1900.tests.utils;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.vecmath.Vector3d;

import nantes1900.models.Mesh;
import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;
import nantes1900.utils.ParserSTL;
import nantes1900.utils.WriterSTL;
import nantes1900.utils.ParserSTL.BadFormedFileException;

import org.junit.Test;


/**
 * A set of tests for the class Writer.
 * 
 * @author Daniel Lefevre
 */
public class WriterSTLTest {

	/**
	 * Test method for
	 * {@link nantes1900.utils.WriterSTL#write(java.lang.String, nantes1900.models.Mesh)}. Same
	 * test as readSTL in ParserTest.
	 */
	@Test
	public void testWrite() {

		Point p1 = new Point(1, 0, -1);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(-1, 2, 1);
		Vector3d vect1 = new Vector3d(0, 0, 1);
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);
		Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, vect1);

		Point p4 = new Point(4, 5, 4);
		Point p5 = new Point(2, -3, -3);
		Point p6 = new Point(-2, 4, -5);
		Vector3d vect2 = new Vector3d(1, 0, 0);
		Edge e4 = new Edge(p4, p5);
		Edge e5 = new Edge(p5, p6);
		Edge e6 = new Edge(p6, p4);
		Triangle t2 = new Triangle(p4, p5, p6, e4, e5, e6, vect2);

		Mesh write = new Mesh();
		write.add(t1);
		write.add(t2);

		write.writeSTL("WriterTest.stl");
		try {
			Mesh read = new Mesh(ParserSTL.readSTL("WriterTest.stl"));
			assertTrue(read.size() == 2);
			ArrayList<Triangle> readList = new ArrayList<Triangle>(read);
			assertTrue(readList.get(0).equals(t1) || readList.get(0).equals(t2));
			assertTrue(readList.get(1).equals(t1) || readList.get(1).equals(t2));
		} catch (BadFormedFileException e) {
			fail("BadFormedFileException !");
		} catch (IOException e) {
			fail("IOException !");
		}

		// FIXME : it doesn't remove the file.
		new File("WriterTest.stl").delete();
	}

	/**
	 * Test method for {@link nantes1900.utils.WriterSTL#setWriteMode(int)}.
	 */
	@Test
	public void testSetWriteMode() {
		WriterSTL.setWriteMode(WriterSTL.ASCII_MODE);
		assertTrue(WriterSTL.getWriteMode() == WriterSTL.ASCII_MODE);

		WriterSTL.setWriteMode(WriterSTL.BINARY_MODE);
		assertTrue(WriterSTL.getWriteMode() == WriterSTL.BINARY_MODE);
	}
}