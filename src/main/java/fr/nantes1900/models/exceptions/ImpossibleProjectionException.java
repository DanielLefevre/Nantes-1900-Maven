package fr.nantes1900.models.exceptions;

/**
 * Implements an exception when a building is not enough simplified to allow the
 * ground projection.
 * @author Daniel Lefevre, Camille Bouquet
 */
public final class ImpossibleProjectionException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public ImpossibleProjectionException() {
    }
}
