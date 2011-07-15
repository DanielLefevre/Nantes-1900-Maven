package algos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import javax.activity.InvalidActivityException;
import javax.vecmath.Vector3d;

import modeles.Mesh;
import modeles.Polyline;
import utils.MatrixMethod;
import utils.MatrixMethod.SingularMatrixException;
import utils.ParserSTL;
import utils.ParserSTL.BadFormedFileException;
import utils.WriterSTL;

/**
 * Algorithm of separation between between walls and roofs and treatment.
 * 
 * @author Daniel Lefevre
 */
public class SeparationTreatmentWallsFloors {

	private double angleNormalErrorFactor = 20;
	private double largeAngleNormalErrorFactor = 40;
	private double errorNormalToFactor = 0.2;
	private double errorNumberTrianglesWall = 4;
	private double errorNumberTrianglesRoof = 6;
	private double errorSingularPoints = 0.1;

	private Mesh floorBrut = new Mesh();
	private Vector3d normalFloor = new Vector3d();

	private Mesh currentBuilding = new Mesh();

	private ArrayList<Mesh> wallList = new ArrayList<Mesh>();
	private ArrayList<Mesh> roofList = new ArrayList<Mesh>();

	private Mesh wholeWall = new Mesh();
	private Mesh wholeRoof = new Mesh();

	private Mesh noise = new Mesh();

	private int counterBuilding = 1;
	private int counterWall = 1;
	private int counterRoof = 1;

	private Logger log = Logger.getLogger("logger");

	/**
	 * Create the algorithm. Create the logger, and put the writing mode in
	 * default BINARY_MODE.
	 */
	public SeparationTreatmentWallsFloors() {

		// Options set
		WriterSTL.setWriteMode(WriterSTL.BINARY_MODE);

		log.setLevel(Level.INFO);
		log.setUseParentHandlers(false);
		log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	/**
	 * Set up the debug mode. Use the finest level of the logger, and write
	 * every STL files in ASCII format to be lisible by any notepad.
	 */
	public void setDebugMode() {
		log.setLevel(Level.FINEST);

		WriterSTL.setWriteMode(WriterSTL.ASCII_MODE);
	}

	/**
	 * Allows to change algorithm coefficients.
	 */
	public void setOptions(double angleNormalErrorFactor,
			double largeAngleNormalErrorFactor, double errorNormalToFactor,
			double errorNumberTrianglesWall, double errorNumberTrianglesRoof) {
		this.angleNormalErrorFactor = angleNormalErrorFactor;
		this.largeAngleNormalErrorFactor = largeAngleNormalErrorFactor;
		this.errorNormalToFactor = errorNormalToFactor;
		this.errorNumberTrianglesWall = errorNumberTrianglesWall;
		this.errorNumberTrianglesRoof = errorNumberTrianglesRoof;
	}

	/**
	 * Execute for each building the algorithm.
	 */
	public void apply() {

		this.parseFloor();

		while (new File("Originals/building - " + this.counterBuilding + ".stl")
				.exists()) {

			this.parseBuilding("Originals/building - " + this.counterBuilding
					+ ".stl");
			this.extractFloorNormal();

			this.sortWalls();
			this.sortRoofs();

			this.treatNoiseWalls();
			this.treatNoiseRoofs();

			this.treatWall();
			this.treatRoof();

			this.writeCityGMLWalls();

			this.counterBuilding++;
		}
	}

	/**
	 * Parse the floor brut. Put it in this.floorBrut. Catch and treat the
	 * errors.
	 */
	private void parseFloor() {
		try {
			this.floorBrut = new Mesh(ParserSTL.readSTL("Originals/floor.stl"));
		} catch (BadFormedFileException e) {
			log.severe("Error in the file !");
		} catch (IOException e) {
			log.severe("Error : file does not exist or is unreadable !");
		}
	}

	/**
	 * Parse the building. Catch and treat the errors.
	 * 
	 * @param fileName
	 *            the name of the file containing the building
	 */
	private void parseBuilding(String fileName) {
		try {
			this.currentBuilding = new Mesh(ParserSTL.readSTL(fileName));
		} catch (BadFormedFileException e) {
			log.severe("Error in the file !");
			System.exit(1);
		} catch (IOException e) {
			log.severe("Error : file does not exist or is unreadable !");
			System.exit(1);
		}
	}

	/**
	 * Compute the average of the normals of the floor brut to obtain the normal
	 * to the floor. Catch and treat matrix exceptions.
	 */
	private void extractFloorNormal() {
		// The floor normal is the average of all the normals fo the triangles
		// in the brut floor file
		normalFloor = floorBrut.averageNormal();

		// Try a base creation if the matrix is not singular
		try {
			double[][] matrix;

			matrix = MatrixMethod.createOrthoBase(normalFloor);

			// Change the normalFloor : now it is z-oriented : (0, 0, 1)
			MatrixMethod.changeBase(normalFloor, matrix);

		} catch (SingularMatrixException e) {
			log.severe("Error in the matrix !");
			System.exit(1);
		}
	}

	/**
	 * 
	 */
	private void sortWalls() {

		Mesh wallOriented = this.currentBuilding.orientedNormalTo(
				this.normalFloor, this.errorNormalToFactor);

		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(wallOriented,
				this.angleNormalErrorFactor);

		int size = 0;

		for (Mesh e : thingsList) {
			this.currentBuilding.remove(e);
			size += e.size();
		}

		for (Mesh e : thingsList) {
			if (e.size() >= this.errorNumberTrianglesWall * (double) size
					/ (double) thingsList.size()) {
				this.wallList.add(e);
			} else
				this.noise.addAll(e);
		}

		for (Mesh w : this.wallList) {
			this.wholeWall.addAll(w);
		}
	}

	/**
	 * 
	 */
	private void sortRoofs() {

		ArrayList<Mesh> thingsList = Algos.blockOrientedExtract(
				this.currentBuilding, this.angleNormalErrorFactor);

		int size = 0;

		for (Mesh e : thingsList) {
			size += e.size();
		}

		for (Mesh e : thingsList) {
			if ((e.size() >= this.errorNumberTrianglesRoof * (double) size
					/ (double) thingsList.size())
					&& (e.averageNormal().dot(this.normalFloor) > 0)) {
				this.roofList.add(e);
			} else
				this.noise.addAll(e);
		}

		for (Mesh r : this.roofList) {
			this.wholeRoof.addAll(r);
		}
	}

	/**
	 * Add the noise to the wall which is its neighbour, and has the same
	 * orientation. The orientation is determined by the
	 * largeAngleNormalErrorFactor.
	 */
	private void treatNoiseWalls() {

		// Add the oriented and neighbour noise to the walls.
		this.wallList = Algos.blockTreatOrientedNoise(this.wallList,
				this.noise, this.largeAngleNormalErrorFactor);

		// Add all the walls to build the wholeWall.
		this.wholeWall = new Mesh();
		for (Mesh w : this.wallList) {
			this.wholeWall.addAll(w);
		}
	}

	/**
	 * Add the noise to the floor which is its neighbour, and has the same
	 * orientation. The orientation is determined by the
	 * largeAngleNormalErrorFactor.
	 */
	private void treatNoiseRoofs() {

		// Add the oriented and neighbour noise to the roofs.
		this.roofList = Algos.blockTreatOrientedNoise(this.roofList,
				this.noise, this.largeAngleNormalErrorFactor);

		// Add all the roofs to build the wholeRoof.
		this.wholeRoof = new Mesh();
		for (Mesh w : this.roofList) {
			this.wholeRoof.addAll(w);
		}
	}

	/**
	 * 
	 */
	private void treatWall() {

		counterWall = 0;

		// For each wall
		while (counterWall < wallList.size()) {

			Mesh wall = wallList.get(counterWall);

			// Compute the contour of the wall
			Polyline longestBound = wall.returnLongestBound(this.normalFloor);

			// Order the polyline of the contour
//			try {
//				longestBound.order(this.normalFloor);
//			} catch (InvalidActivityException e1) {
//				e1.printStackTrace();
//			}

			Vector3d normalWall = wall.averageNormal();
			double[][] matrixWall = null, matrixWallInv = null;

			// Base change to have the surface in the (x,y) plane
			try {
				matrixWall = MatrixMethod.createOrthoBase(normalWall);
				matrixWallInv = MatrixMethod.getInversMatrix(matrixWall);
			} catch (SingularMatrixException e) {
				System.err.println("Error in the matrix !");
				System.exit(1);
			}

			// Project on the plane at the z coordinate of the z average of
			// all triangles of the wall.
			longestBound.changeBase(matrixWall);
			longestBound = longestBound.zProjection(longestBound.zAverage());

			// Compute the singular points
			Polyline singularPoints = null;
			try {
				singularPoints = longestBound
						.determinateSingularPoints(this.errorSingularPoints, this.normalFloor);
			} catch (InvalidActivityException e) {
				e.printStackTrace();
			}

			// Inverse base change
			singularPoints.changeBase(matrixWallInv);
			singularPoints.returnCentroidMesh().write(
					"Files/wallSingularPoints - " + counterWall + ".stl");

			counterWall++;
		}
	}

	/**
	 * 
	 */
	private void treatRoof() {

		counterRoof = 0;

		while (counterRoof < roofList.size()) {

			Mesh roof = roofList.get(counterRoof);

			// Compute the contour of the roof
			Polyline longestBound = roof.returnLongestBound(this.normalFloor);

//			// Order the polyline of the contour
//			try {
//				longestBound.order(this.normalFloor);
//			} catch (InvalidActivityException e1) {
//				e1.printStackTrace();
//			}

			Vector3d normalRoof = roof.averageNormal();
			double[][] matrixRoof = null, matrixRoofInv = null;

			// Base change to have the surface in the (x,y) plane
			try {
				matrixRoof = MatrixMethod.createOrthoBase(normalRoof);
				matrixRoofInv = MatrixMethod.getInversMatrix(matrixRoof);
			} catch (SingularMatrixException e) {
				System.err.println("Error in the matrix !");
				System.exit(1);
			}

			// Project on the plane at the z coordinate of the z average of
			// all triangles of the roof.
			longestBound.changeBase(matrixRoof);
			longestBound = longestBound.zProjection(longestBound.zAverage());

			// Compute the singular points
			Polyline singularPoints = null;
			try {
				singularPoints = longestBound
						.determinateSingularPoints(this.errorSingularPoints, this.normalFloor);
			} catch (InvalidActivityException e) {
				e.printStackTrace();
			}

			// Inverse base change
			singularPoints.changeBase(matrixRoofInv);
			longestBound.returnCentroidMesh().write(
					"Files/roofSingularPoints - " + counterRoof + ".stl");

			counterRoof++;
		}
	}

	/**
	 *
	 */
	private void writeCityGMLWalls() {
		// counterWall = 0;
		//
		// while (counterWall < wallList.size()) {
		//
		// Mesh wall = wallList.get(counterWall);
		//
		// try {
		// WriterCityGML.write("test", wall);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// counterWall++;
		// }
	}
}