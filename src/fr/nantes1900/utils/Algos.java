package fr.nantes1900.utils;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Triangle;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Implements a class containing some little algorithms used in the other
 * classes.
 * @author Daniel Lefevre
 */
public final class Algos {

    /**
     * Divide the mesh in block of neighbours. This method use returnNeighbours
     * to find the neighbours of one triangle, and put it into a new mesh into
     * the arraylist. Thus, it takes another triangle and redo the same
     * operation until there is no more triangle. This method does not destroy
     * the mesh in parameter.
     * @param m
     *            the mesh to divide
     * @return an array of the blocks-meshs
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if the edge is bad formed
     */
    public static ArrayList<Mesh> blockExtract(final Mesh m)
        throws MoreThanTwoTrianglesPerEdgeException {
        HashSet<Mesh> thingsList = new HashSet<Mesh>();
        Mesh mesh = new Mesh(m);

        while (!mesh.isEmpty()) {

            Mesh e = new Mesh();
            mesh.getOne().returnNeighbours(e, mesh);
            mesh.remove(e);
            thingsList.add(e);

        }

        return new ArrayList<Mesh>(thingsList);
    }

    /**
     * Divide the mesh in block of neighbours depending on their orientations.
     * This method takes one triangle and use returnNeighbours to find the
     * triangles which are oriented as the first one (with an error) and find
     * into them its neighbours, and put it in a new mesh into the arraylist.
     * Thus, it takes another triangle and redo the same operation until there
     * is no more triangle. This method does not destroy the mesh in parameter.
     * @param m
     *            the mesh to divide
     * @param angleNormalErrorFactor
     *            the error on the orientation
     * @return an array of the blocks-meshs
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             uf the edge is bad formed
     */
    public static ArrayList<Mesh> blockOrientedExtract(final Mesh m,
        final double angleNormalErrorFactor)
        throws MoreThanTwoTrianglesPerEdgeException {
        ArrayList<Mesh> thingsList = new ArrayList<Mesh>();
        Mesh mesh = new Mesh(m);

        while (!mesh.isEmpty()) {

            Mesh e = new Mesh();
            Triangle tri = mesh.getOne();
            Mesh oriented = mesh.orientedAs(tri.getNormal(),
                angleNormalErrorFactor);
            tri.returnNeighbours(e, oriented);
            mesh.remove(e);
            thingsList.add(e);

        }

        return thingsList;
    }

    /**
     * Treat a list of mesh to add the noise which is part of the mesh. This
     * method try to find a block of noise which complete the mesh (of the list)
     * and which have the same orientation. It thus adds it to the mesh.
     * @param list
     *            the list of mesh to complete with noise
     * @param noise
     *            the whole noise
     * @param largeAngleNormalErrorFactor
     *            the error on the orientation
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if the edge is bad formed
     */
    public static void blockTreatOrientedNoise(final ArrayList<Mesh> list,
        final Mesh noise, final double largeAngleNormalErrorFactor)
        throws MoreThanTwoTrianglesPerEdgeException {

        ArrayList<Mesh> m = new ArrayList<Mesh>();

        for (Mesh e : list) {
            Mesh meshAndNoise = new Mesh(e);
            Mesh noiseOriented = noise.orientedAs(e.averageNormal(),
                largeAngleNormalErrorFactor);
            meshAndNoise.addAll(noiseOriented);
            Mesh mes = new Mesh();
            e.getOne().returnNeighbours(mes, meshAndNoise);
            m.add(mes);
            noise.remove(mes);
        }

        list.clear();
        list.addAll(m);
    }

    /**
     * Extract the floor in a mesh. Receiving a mesh of triangles oriented as
     * the floor, tt searches the lowest altitude as the lowest z, and take a
     * stripe of triangles that are contained in the lowest altitude and an
     * error. In this stripe, it takes one triangle, and returns all its
     * neighbours. FIXME : find the altitude factor automatically.
     * @param meshOriented
     *            the floor-oriented triangles that will be treated
     * @param altitudeErrorFactor
     *            the error on the altitude
     * @return the mesh containing the floor extracted
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if the edge is bad formed
     */
    public static Mesh floorExtract(final Mesh meshOriented,
        final double altitudeErrorFactor)
        throws MoreThanTwoTrianglesPerEdgeException {
        Mesh floors = new Mesh();

        try {
            Triangle lowestTriangle = meshOriented.zMinFace();
            double lowestZ = lowestTriangle.zMin();

            Mesh stripe = meshOriented.zBetween(lowestZ, lowestZ
                + altitudeErrorFactor);

            Mesh temp = new Mesh();

            while (!stripe.isEmpty()) {

                lowestTriangle = stripe.getOne();
                temp.clear();
                lowestTriangle.returnNeighbours(temp, meshOriented);
                floors.addAll(temp);
                meshOriented.remove(temp);
                stripe.remove(temp);
            }

            return floors;
        } catch (InvalidParameterException e) {
            return null;
        }
    }

    /**
     * Private constructor.
     */
    private Algos() {
    }

    /**
     * Implements an exception when the floor is empty.
     * @author Daniel Lefevre
     */
    public static class NoFloorException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;
    }
}