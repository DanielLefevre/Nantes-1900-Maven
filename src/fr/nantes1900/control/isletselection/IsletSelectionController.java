/**
 * TODO by Camille and Luc : missing package-info.java file.
 */
package fr.nantes1900.control.isletselection;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.control.BuildingsIsletController;
import fr.nantes1900.control.GlobalController;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.ResidentialIslet;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.utils.ParserSTL;
import fr.nantes1900.utils.WriterSTL;
import fr.nantes1900.view.isletselection.GlobalTreeView.FileNode;
import fr.nantes1900.view.isletselection.IsletSelectionView;

/**
 * @author Camille
 */
public class IsletSelectionController
{

    /**
     * The controller of the panel containing buttons to perform the different
     * actions.
     */
    private ActionsController        aController;

    /**
     * The controller of the tree used to select an islet.
     */
    private GlobalTreeController     gtController;

    /**
     * The controller of the 3D view which shows a selected islet.
     */
    private Universe3DController     u3DController;

    /**
     * The controller of the selected islet.
     */
    private BuildingsIsletController biController;

    /**
     * View allowing to select an islet and launch a process.
     */
    private IsletSelectionView       isView;

    /**
     * The opened directory corresponding.
     */
    private File                     openedDirectory;

    /**
     * The selected file in the tree.
     */
    private File                     selectedFile;

    /**
     * The parent controller which handles this one.
     */
    private GlobalController         parentController;

    /**
     * Creates a new controller to handle the islet selection window.
     * @param parentControllerIn
     *            Controller which handles this one.
     */
    public IsletSelectionController(final GlobalController parentControllerIn)
    {
        this.parentController = parentControllerIn;
        this.gtController = new GlobalTreeController(this);
        this.aController = new ActionsController(this);
        this.u3DController = new Universe3DController();
        this.biController = new BuildingsIsletController(this,
                this.u3DController);

        this.isView = new IsletSelectionView(this.aController.getActionsView(),
                this.gtController.getGlobalTreeView(),
                this.u3DController.getUniverse3DView());
        this.isView.setVisible(true);
    }

    /**
     * Computes the gravity normal and stores it in a gravity_normal.stl file in
     * the opened directory.
     * @return true - the normal has been correctly saved\n false - the normal
     *         couldn't been stored because no file or triangles are selected.
     */
    public final boolean computeGravityNormal()
    {
        boolean normalSaved = false;
        if (this.selectedFile != null && !this.u3DController.getTrianglesSelected()
                .isEmpty())
        {
            // TODO by Daniel : Move this code
            WriterSTL writer = new WriterSTL(this.openedDirectory.getPath() + "/gravity_normal.stl");
            Point point = new Point(1, 1, 1);
            Edge edge = new Edge(point, point);
            Triangle triangle = new Triangle(point,
                    point,
                    point,
                    edge,
                    edge,
                    edge,
                    this.biController.computeNormalWithTrianglesSelected());
            Mesh mesh = new Mesh();
            mesh.add(triangle);
            writer.setMesh(mesh);
            writer.write();
            System.out.println("Enregistre");
            normalSaved = true;
        } else
        {
            // TODO : put this text in the text file (or XML for Luc).
            JOptionPane.showMessageDialog(this.isView,
                    "Sélectionnez un îlot dans l'arbre\npuis " + "sélectionnez des triangles pour créer la "
                            + "normale\nou sélectionnez \"Utiliser la normale "
                            + "orientée selon la gravité\n",
                    "Aucun îlot ouvert",
                    JOptionPane.ERROR_MESSAGE);
        }

        return normalSaved;
    }

    /**
     * Computes the ground normal.
     */
    public final void computeGroundNormal()
    {
        this.biController.setGroundNormal(this.biController.computeNormalWithTrianglesSelected());
    }

    /**
     * Displays a file in the 3d universe selected in the tree.
     * @param node
     *            The node of the tree corresponding to the file to display.
     */
    public final void displayFile(final DefaultMutableTreeNode node)
    {
        Cursor cursor = new Cursor(Cursor.WAIT_CURSOR);
        this.isView.setCursor(cursor);
        // Reads the file object of the Tree
        FileNode fileNode = (FileNode) node.getUserObject();

        if (fileNode.isFile())
        {
            ParserSTL parser = new ParserSTL(fileNode.getEntireName());
            this.selectedFile = fileNode;

            AbstractBuildingsIslet resIslet;
            try
            {
                // TODO by Daniel : it shouldn't be here, but in the
                // biController...
                resIslet = new ResidentialIslet(parser.read());
                this.biController.setIslet(resIslet);
                this.biController.putGravityNormal();
                this.biController.display();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        this.isView.setCursor(cursor);
    }

    /**
     * Returns the building islet controller.
     * @return The building islet controller.
     */
    public final BuildingsIsletController getBiController()
    {
        return this.biController;
    }

    /**
     * TODO.
     * @return TODO
     */
    public final JFrame getWindow()
    {
        return this.isView;
    }

    /**
     * Launches the treatment of the selected file which is an islet file. The
     * verification that the selected file is an islet file is made at the
     * selection in the tree.
     * @return true - the process of the selected islet has been correctly
     *         launched\n false - the process wasn't launch, because no islet or
     *         ground normal are selected.
     */
    public final boolean launchIsletProcess()
    {
        boolean processLaunched = false;

        if ((!this.u3DController.getTrianglesSelected().isEmpty() || this.aController.getActionsView()
                .isGravityGroundCheckBoxSelected()) && this.selectedFile != null)
        {
            if (!this.aController.getActionsView()
                    .isGravityGroundCheckBoxSelected())
            {
                this.computeGroundNormal();
            } else
            {
                this.biController.useGravityNormalAsGroundNormal();
            }
            this.parentController.launchIsletProcess(this.selectedFile,
                    this.biController);
            processLaunched = true;
        } else
        {
            // TODO by Luc : put this text in a XML file.
            JOptionPane.showMessageDialog(this.isView,
                    "Veuillez sélectionner un îlot et une normale pour " + "lancer le traitement",
                    "Traitement impossible",
                    JOptionPane.ERROR_MESSAGE);
        }

        return processLaunched;
    }

    /**
     * Updates the directory containing the files of islets.
     * @param newDirectory
     *            The new directory.
     */
    public final void updateMockupDirectory(final File newDirectory)
    {
        this.openedDirectory = newDirectory;
        this.gtController.updateDirectory(this.openedDirectory);

        // checks if the gravity normal already exists
        File gravityNormal = new File(this.openedDirectory.getPath() + "/gravity_normal.stl");
        if (!gravityNormal.exists())
        {
            JOptionPane.showMessageDialog(this.isView,
                    "La normale orientée selon la gravité n'a pas été trouvée" + "dans le dossier ouvert.\nVeuillez en créer "
                            + "une nouvelle.",
                    "Normale orientée selon la gravité inexistante",
                    JOptionPane.INFORMATION_MESSAGE);
            this.aController.setComputeNormalMode();
            this.isView.setStatusBarText(FileTools.readHelpMessage("ISGravityNormal",
                    FileTools.MESSAGETYPE_STATUSBAR));
        } else
        {
            try
            {
                // Reads the gravity normal in the file, and keeps it in memory.
                this.biController.readGravityNormal(gravityNormal.getPath());
            } catch (IOException e)
            {
                // If the file can not be read or is not well built.
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.isView.setStatusBarText(FileTools.readHelpMessage("ISLaunchProcess",
                    FileTools.MESSAGETYPE_STATUSBAR));
            this.aController.setLaunchMode();
        }
    }
}
