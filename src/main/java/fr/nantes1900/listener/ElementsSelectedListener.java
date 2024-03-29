package fr.nantes1900.listener;

import java.util.EventListener;
import java.util.List;

import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;

/**
 * Listener of elements selected/deselected such as a list of triangles or
 * surfaces.
 * @author Camille Bouquet
 */
public interface ElementsSelectedListener extends EventListener {

    /**
     * Indicates that a new set of triangles has been selected.
     * @param trianglesSelected
     *            the new set of triangles selected
     */
    void newTrianglesSelection(List<Triangle> trianglesSelected);

    /**
     * Indicates that a surface has been selected.
     * @param surfaceSelected
     *            the selected surface
     */
    void surfaceDeselected(Surface surfaceSelected);

    /**
     * Indicates that a surface has been deselected.
     * @param surfaceSelected
     *            the deselected surface
     */
    void surfaceSelected(Surface surfaceSelected);
}
