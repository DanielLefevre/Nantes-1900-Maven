package modeles;

import java.util.*;

import javax.vecmath.Vector3d;

import utils.Writer;

/**
 * An ensemble of Triangle with a defined type
 */
public class Mesh extends HashSet<Triangle>{

	private static final long serialVersionUID = 1L;

	public Mesh() {
		super();
	}

	public Mesh(Collection<? extends Triangle> c) {
		super(c);
	}

	public Mesh orientedAs(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for(Triangle f : this) {
			if(f.angularTolerance(normal, error))
				ret.add(f);
		}
		return ret;
	}

	public Mesh orientedNormalTo(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for(Triangle f : this) {
			if(f.isNormalTo(normal, error))
				ret.add(f);
		}
		return ret;
	}

	/**
	 * Compute the average normal of all Faces composing this Ensemble
	 * @return average The average Vector3d normal.
	 */
	public Vector3d averageNormal(){
		int n = this.size();
		Vector3d average = new Vector3d();
		for(Triangle face : this){
			average.add(face.getNormal());
		}
		average.scale(1/(double)n);
		return average;
	}

	/**
	 * Compute the average z-coordinate of all points of all faces from this Ensemble
	 * @return the average z-coordinate of all points
	 */
	public double zAverage(){
		double zAverage = 0;
		for(Triangle face : this){
			zAverage += face.zAverage();
		}
		return zAverage/this.size();
	}

	public double xAverage(){
		double xAverage = 0;
		for(Triangle face : this){
			xAverage += face.xAverage();
		}
		return xAverage/this.size();
	}

	public double yAverage(){
		double yAverage = 0;
		for(Triangle face : this){
			yAverage += face.yAverage();
		}
		return yAverage/this.size();
	}

	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.xMax() > xMaxi){
				xMaxi = face.xMax();
			}
		}
		return xMaxi;
	}

	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.xMin() < xMini){
				xMini = face.xMin();
			}
		}
		return xMini;
	}

	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMax() > yMaxi){
				yMaxi = face.yMax();
			}
		}
		return yMaxi;
	}

	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMin() < yMini){
				yMini = face.yMin();
			}
		}
		return yMini;
	}

	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMax() > zMaxi){
				zMaxi = face.zMax();
			}
		}
		return zMaxi;
	}

	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMin() < zMini){
				zMini = face.zMin();
			}
		}
		return zMini;
	}

	public Triangle zMinFace() {
		Triangle triangle = null;
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMin() < zMini){
				zMini = face.zMin();
				triangle = face;
			}
		}
		return triangle;
	}

	public Point yMaxPoint() {
		Point p = null;
		double yMax = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMax() > yMax){
				yMax = face.yMax();
				p = face.yMaxPoint();
			}
		}
		return p;
	}

	public Point yMinPoint() {
		Point p = null;
		double yMin = Double.POSITIVE_INFINITY;
		for (Triangle face : this){
			if (face.yMin() < yMin){
				yMin = face.yMin();
				p = face.yMinPoint();
			}
		}
		return p;
	}

	public Point zMaxPoint() {
		Point p = null;
		double zMax = Double.NEGATIVE_INFINITY;
		for (Triangle face : this){
			if (face.zMax() > zMax){
				zMax = face.zMax();
				p = face.zMaxPoint();
			}
		}
		return p;
	}

	public Triangle zMinFaceUnder(double zMax) {
		Triangle t = null, temp;
		Iterator<Triangle> i = this.iterator();
		while(t == null && i.hasNext()) {
			temp = i.next();
			if (temp.zMin() < zMax) {
				t = temp;
			}
		}
		return t;
	}

	public Triangle getOne() {
		return this.iterator().next();
	}

	public void remove(Mesh aSuppr) {
		for(Triangle f : aSuppr) {
			this.remove(f);
		}
	}

	public Mesh changeBase(double[][] matrix) {
		Mesh ens = new Mesh();
		for(Triangle f : this) {
			ens.add(f.changeBase(matrix));
		}
		return ens;
	}

	public Mesh zBetween(double m1, double m2) {
		double min = Math.min(m1, m2);
		double max = Math.max(m1, m2);
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.zMax() < max && t.zMin() > min)
				ens.add(t);
		}
		return ens;
	}

	public Mesh xBetween(double m1, double m2) {
		double min = Math.min(m1, m2);
		double max = Math.max(m1, m2);
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.xMax() < max && t.xMin() > min)
				ens.add(t);
		}
		return ens;
	}

	public Mesh yBetween(double m1, double m2) {
		double min = Math.min(m1, m2);
		double max = Math.max(m1, m2);
		Mesh ens = new Mesh();
		for(Triangle t : this) {
			if(t.yMax() < max && t.yMin() > min)
				ens.add(t);
		}
		return ens;
	}



	public Mesh getInBounds(Point p, double tailleBoule) {
		Mesh ens = new Mesh();
		for(Triangle tri : this) {
			if(p.distance(tri.getCentroid()) < tailleBoule)
				ens.add(tri);
		}
		return ens;
	}

	//	public Triangle getOutOfIndex(HashSet<Point> index, double tailleBoule) {
	//		boolean tag = true;
	//		for(Triangle t : this) {
	//			Point p = t.getCentroid();
	//			if(index.isEmpty())
	//				return t;
	//			for(Point point : index) {
	//				if(p.distance3D(point) < tailleBoule/(double)2) {
	//					tag = false;
	//					break;
	//				}
	//			}
	//			if(tag)
	//				return t;
	//			else
	//				tag = true;
	//		}
	//		return null;
	//	}

	public Mesh zProjection(double z) {
		Mesh e = new Mesh();
		for(Triangle t : this) {
			e.add(t.zProjection(z));
		}
		return e;
	}

	public Mesh xProjection(double x) {
		Mesh e = new Mesh();
		for(Triangle t : this) {
			e.add(t.xProjection(x));
		}
		return e;
	}

	//TODO : à refaire !
	public ArrayList<Border> determinerFrontieres() {
		ArrayList<Border> e = new ArrayList<Border>();

		Border front = new Border();
		for(Triangle tri : this) {
			if(tri.getNumVoisins() < 3)
				front.addAll(tri.getFront());
		}

		while(!front.getEdgeSet().isEmpty()) {
			Edge arete = front.getEdgeSet().iterator().next();
			Border ret = new Border();
			front.returnNeighbours(ret, arete);
			e.add(ret);
			front.suppress(ret);	
		}

		return e;
	}

	public void clearNeighbours() {
		for(Triangle t : this) {
			t.clearVoisins();
		}
	}

	public void writeA(String fileName) {
		Writer.writeSTLA(fileName, this);
		System.out.println(fileName + " written in STL ASCII!");
	}

	public void writeB(String fileName) {
		Writer.writeSTLB(fileName, this);
		System.out.println(fileName + " written in STL binary!");
	}
}