package fr.nantes1900.models.extended;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.exceptions.NullArgumentException;
import fr.nantes1900.models.extended.steps.BuildingStep3;
import fr.nantes1900.models.extended.steps.BuildingStep4;
import fr.nantes1900.models.extended.steps.BuildingStep5;
import fr.nantes1900.models.extended.steps.BuildingStep6;
import fr.nantes1900.models.extended.steps.BuildingStep7;

/**
 * Implements a building as containing 6 steps representing the state of the
 * building during the processes 3 to 7.
 * @author Daniel Lefevre
 */
public class Building implements BuildingAccess {

    /**
     * The third building step.
     */
    private BuildingStep3 bStep3;
    /**
     * The fourth building step.
     */
    private BuildingStep4 bStep4;
    /**
     * The fifth building step.
     */
    private BuildingStep5 bStep5;
    /**
     * The sixth building step.
     */
    private BuildingStep6 bStep6;

    /**
     * The seventh building step.
     */
    private BuildingStep7 bStep7;

    /**
     * The gravity normal.
     */
    private Vector3d gravityNormal;

    /**
     * The ground.
     */
    private Ground grounds;
    /**
     * The normal to the ground.
     */
    private Vector3d groundNormal;
    /**
     * The noise.
     */
    private Surface noise;

    /**
     * Constructor.
     * @param surface
     *            the surface representing the building
     */
    public Building(final Surface surface) {
        this.bStep3 = new BuildingStep3(surface);
    }

    /**
     * Getter.
     * @return the third step
     */
    public final BuildingStep3 getbStep3() {
        return this.bStep3;
    }

    /**
     * Getter.
     * @return the fourth step
     */
    public final BuildingStep4 getbStep4() {
        return this.bStep4;
    }

    /**
     * Getter.
     * @return the fifth step
     */
    public final BuildingStep5 getbStep5() {
        return this.bStep5;
    }

    /**
     * Getter.
     * @return the sixth step
     */
    public final BuildingStep6 getbStep6() {
        return this.bStep6;
    }

    /**
     * Launches the third process.
     * @throws NullArgumentException
     *             if some arguments needed in the process have not been
     *             initialized
     */
    public final void launchProcess3() throws NullArgumentException {
        this.bStep3.setArguments(this.gravityNormal);
        this.bStep4 = this.bStep3.launchProcess();
    }

    /**
     * Launches the fourth process.
     * @throws NullArgumentException
     *             if some arguments needed in the process have not been
     *             initialized
     */
    public final void launchProcess4() throws NullArgumentException {
        this.bStep4.setArguments(this.groundNormal, this.grounds, this.noise);
        this.bStep5 = this.bStep4.launchProcess();
        this.bStep5.setArguments(this.noise, this.grounds, this.groundNormal);
    }

    /**
     * Launches the fifth process.
     * @throws NullArgumentException
     *             if some arguments needed in the process have not been
     *             initialized
     */
    public final void launchProcess5() throws NullArgumentException {
        this.bStep6 = this.bStep5.launchProcess();
    }

    /**
     * Launches the sixth process.
     */
    public final void launchProcess6() {
        this.bStep7 = this.bStep6.launchProcess();
    }

    /**
     * Creates a tree node for the third step.
     * @param counter
     *            the number of the current building
     * @return the mutable tree node
     */
    public final DefaultMutableTreeNode returnNode3(final int counter) {
        return this.bStep3.returnNode(counter);
    }

    /**
     * Creates a tree node for the fourth step.
     * @param counter
     *            the number of the current building
     * @return the mutable tree node
     */
    public final DefaultMutableTreeNode returnNode4(final int counter) {
        return this.bStep4.returnNode(counter);
    }

    /**
     * Creates a tree node for the fifth step.
     * @param counter
     *            the number of the current building
     * @return the mutable tree node
     */
    public final DefaultMutableTreeNode returnNode5(final int counter) {
        return this.bStep5.returnNode(counter);
    }

    /**
     * Creates a tree node for the sixth step.
     * @param counter
     *            the number of the current building
     * @return the mutable tree node
     */
    public final DefaultMutableTreeNode returnNode6(final int counter) {
        return this.bStep6.returnNode(counter);
    }

    /**
     * Creates a tree node for the seventh step.
     * @param counter
     *            the number of the current building
     * @return the mutable tree node
     */
    public final DefaultMutableTreeNode returnNode7(final int counter) {
        return this.bStep7.returnNode(counter);
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal to the ground
     * @param gravityNormalIn
     *            the gravity normal
     * @param groundsIn
     *            the grounds
     * @param noiseIn
     *            the noise
     */
    public final void setArguments(final Vector3d groundNormalIn,
            final Vector3d gravityNormalIn, final Ground groundsIn,
            final Surface noiseIn) {
        this.groundNormal = groundNormalIn;
        this.gravityNormal = gravityNormalIn;
        this.grounds = groundsIn;
        this.noise = noiseIn;
    }

    @Override
    public final List<Wall> getWalls(final int step) {
        List<Wall> walls = new ArrayList<>();

        switch (step) {
        case 5:
            walls.addAll(this.bStep5.getWalls());
            break;
        case 6:
            walls.addAll(this.bStep6.getWalls());
            break;
        case 7:
            walls.addAll(this.bStep7.getWalls());
            break;
        default:
            break;
        }

        return walls;
    }

    @Override
    public final List<Roof> getRoofs(final int step) {
        List<Roof> roofs = new ArrayList<>();

        switch (step) {
        case 5:
            roofs.addAll(this.bStep5.getRoofs());
            break;
        case 6:
            roofs.addAll(this.bStep6.getRoofs());
            break;
        case 7:
            roofs.addAll(this.bStep7.getRoofs());
            break;
        default:
            break;
        }

        return roofs;
    }

    /**
     * Getter.
     * @return the seventh step
     */
    public final BuildingStep7 getbStep7() {
        return this.bStep7;
    }
}
