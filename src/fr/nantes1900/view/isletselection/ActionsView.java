/**
 * 
 */
package fr.nantes1900.view.isletselection;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.components.HelpButton;

/**
 * @author Camille
 */
public class ActionsView extends JPanel
{
    /**
     * Version ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Button used to open a mockup part.
     */
    private JButton           bOpen;

    /**
     * Button used to launch the process of an islet.
     */
    private JButton           bLaunch;

    /**
     * Button used to display some help.
     */
    private HelpButton        bHelp;

    /**
     * Creates a new panel containing the open and launch buttons.
     */
    public ActionsView()
    {
        this.bOpen = new JButton("Ouvrir");
        this.bLaunch = new JButton("Lancer");
        this.bLaunch.setEnabled(false);
        this.bHelp = new HelpButton(
                FileTools.readHelpMessage(FileTools.KEY_IS_OPENDIRECTORY, FileTools.MESSAGETYPE_TOOLTIP),
                FileTools.readHelpMessage(FileTools.KEY_IS_OPENDIRECTORY, FileTools.MESSAGETYPE_MESSAGE),
                FileTools.readHelpMessage(FileTools.KEY_IS_OPENDIRECTORY, FileTools.MESSAGETYPE_TITLE));

        this.add(this.bOpen);
        this.add(this.bLaunch);
        this.add(bHelp);
    }

    /**
     * Returns the open folder button.
     * @return The open button.
     */
    public JButton getOpenButton()
    {
        return this.bOpen;
    }

    /**
     * Returns the launch islet treatment button.
     * @return The launch button.
     */
    public JButton getLaunchButton()
    {
        return this.bLaunch;
    }

    /**
     * Returns the help button.
     * @return The help button.
     */
    public HelpButton getHelpButton()
    {
        return this.bHelp;
    }
}
