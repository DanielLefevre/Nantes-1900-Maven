package fr.nantes1900.models.islets.buildings.steps;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.islets.buildings.UnCompletedParametersException;
import fr.nantes1900.utils.MatrixMethod;
import fr.nantes1900.utils.MatrixMethod.SingularMatrixException;

/**
 * @author Daniel
 */
public class BuildingsIsletStep0 extends AbstractBuildingsIsletStep
{
    /**
     * The initial mesh before every treatment.
     */
    private Mesh       initialTotalMesh;

    /**
     * Change base matrix from the current base to a base which is ground-like
     * oriented.
     */
    private double[][] matrix = new double[MatrixMethod.MATRIX_DIMENSION][MatrixMethod.MATRIX_DIMENSION];

    /**
     * The normal to the gravity.
     */
    private Vector3d   gravityNormal;

    /**
     * TODO.
     * @throws UnCompletedParametersException
     *             TODO.
     */
    public final void changeBase() throws UnCompletedParametersException
    {
        if (this.matrix == null || this.initialTotalMesh == null)
        {
            throw new UnCompletedParametersException();
        }
        this.initialTotalMesh.changeBase(this.matrix);
    }

    /**
     * Creates a change base matrix with the normal to the ground. See the
     * MatrixMethod class for more informations.
     */
    public final void createChangeBaseMatrix()
    {
        try
        {
            // Base change
            this.matrix = MatrixMethod.createOrthoBase(this.gravityNormal);
            MatrixMethod.changeBase(this.gravityNormal, this.matrix);

        } catch (final SingularMatrixException e)
        {
            System.out.println("Error : the matrix is badly formed !");
            System.exit(1);
        }
    }

    /**
     * Constructor.
     * @param mesh
     *            the mesh representing the entire islet
     */
    public BuildingsIsletStep0(final Mesh mesh)
    {
        this.initialTotalMesh = mesh;
    }

    /**
     * Initialize some arguments needed for the treatement.
     * @param gravityNormalIn
     *            the normal to the gravity
     */
    public final void setArguments(final Vector3d gravityNormalIn)
    {
        this.gravityNormal = gravityNormalIn;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #launchTreatment()
     */
    @Override
    public final BuildingsIsletStep1 launchTreatment()
    {
        try
        {
            this.createChangeBaseMatrix();
            this.changeBase();
        } catch (UnCompletedParametersException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new BuildingsIsletStep1(this.initialTotalMesh);
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.islets.buildings.steps.AbstractBuildingsIsletStep
     * #returnNode()
     */
    @Override
    public final DefaultMutableTreeNode returnNode()
    {
        // TODO : create a method toString in the class Mesh to use it here.
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(this.initialTotalMesh);
        return node;
    }

    /**
     * Getter.
     * @return the initial total mesh
     */
    public final Mesh getInitialTotalMesh()
    {
        return this.initialTotalMesh;
    }

}
