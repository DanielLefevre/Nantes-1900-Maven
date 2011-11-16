/**
 * 
 */
package fr.nantes1900.control;

import java.io.File;

import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.control.isletselection.IsletSelectionController;

/**
 * @author Camille
 */
public class GlobalController
{
    /**
     * The controller of the selection window.
     */
    @SuppressWarnings("unused")
    private IsletSelectionController isletSelectionController;
    private IsletProcessController isletProcessController;

    /**
     * Creates a new global controller which creates others global controllers.
     */
    public GlobalController()
    {
        this.isletSelectionController = new IsletSelectionController(this);
    }

    /**
     * Launches the process of an islet and opens the new window.
     * @param isletFile
     *            The file containing data of the islet to process.
     * @param biController 
     */
    public void launchIsletProcess(File isletFile, BuildingsIsletController biController)
    {
        isletProcessController = new IsletProcessController(this, isletFile, biController);
        isletSelectionController.getWindow().setVisible(false);
    }
}
