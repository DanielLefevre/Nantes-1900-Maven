package fr.nantes1900.control.isletprocess.characteristics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.constants.Icons;
import fr.nantes1900.constants.TextsKeys;
import fr.nantes1900.control.isletprocess.IsletProcessController;
import fr.nantes1900.models.exceptions.InvalidCaseException;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.utils.FileTools;
import fr.nantes1900.view.isletprocess.characteristics.CharacteristicsStep6View;

/**
 * Characteristics panel for the sixth step of process of an islet.
 * @author Camille Bouquet, Luc Jallerat
 */
public class CharacteristicsStep6Controller extends
        AbstractCharacteristicsSurfacesController {

    /**
     * Tells if the surface is locked.
     */
    private boolean surfaceLocked = false;
    /**
     * Surface to display neighbors from.
     */
    private Surface surfaceToCheck;

    /**
     * Higlighted surface.
     */
    private Surface highlightedSurface;

    /**
     * Creates a new controller for the 6th step characteristics panel.
     * @param parentControllerIn
     *            the controller which handles this controller.
     * @param newSurface
     *            the new selected surface
     * @param neighbors
     *            the list of current neighbors
     */
    public CharacteristicsStep6Controller(
            final IsletProcessController parentControllerIn,
            final Surface newSurface, final List<Surface> neighbors) {
        super(parentControllerIn);

        this.surfaceToCheck = newSurface;
        this.setSurfacesList(neighbors);
        this.cView = new CharacteristicsStep6View(neighbors);

        ((CharacteristicsStep6View) this.cView)
                .setModificationsEnabled(this.surfaceLocked);

        ((CharacteristicsStep6View) this.cView).getLockButton()
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent arg0) {
                        CharacteristicsStep6Controller.this.surfaceLocked = !CharacteristicsStep6Controller.this.surfaceLocked;
                        JButton source = ((JButton) arg0.getSource());

                        if (CharacteristicsStep6Controller.this.surfaceLocked) {
                            source.setIcon(new ImageIcon(Icons.UNLOCK));
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_LOCKMESH));
                            CharacteristicsStep6Controller.this.parentController
                                    .lock(true);

                        } else {
                            source.setIcon(new ImageIcon(Icons.LOCK));
                            source.setToolTipText(FileTools
                                    .readElementText(TextsKeys.KEY_UNLOCKMESH));
                            CharacteristicsStep6Controller.this.parentController
                                    .lock(false);
                        }

                        ((CharacteristicsStep6View) CharacteristicsStep6Controller.this.cView)
                                .setModificationsEnabled(CharacteristicsStep6Controller.this.surfaceLocked);
                    }

                });

        this.cView.getValidateButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (((CharacteristicsStep6View) CharacteristicsStep6Controller.this.cView)
                        .isNoiseSelected()) {
                    for (Surface surface : CharacteristicsStep6Controller.this
                            .getSurfacesList()) {
                        try {
                            CharacteristicsStep6Controller.this.parentController
                                    .getBiController().action6(surface,
                                            ActionTypes.TURN_TO_NOISE);
                        } catch (InvalidCaseException e) {
                            JOptionPane
                                    .showMessageDialog(
                                            CharacteristicsStep6Controller.this.cView,
                                            FileTools
                                                    .readInformationMessage(
                                                            TextsKeys.KEY_ERROR_INCORRECTACTION,
                                                            TextsKeys.MESSAGETYPE_MESSAGE),
                                            FileTools
                                                    .readInformationMessage(
                                                            TextsKeys.KEY_ERROR_INCORRECTACTION,
                                                            TextsKeys.MESSAGETYPE_TITLE),
                                            JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    setSurfacesList(((CharacteristicsStep6View) CharacteristicsStep6Controller.this.cView)
                            .getList());
                    CharacteristicsStep6Controller.this.parentController
                            .getBiController()
                            .action6(
                                    CharacteristicsStep6Controller.this.surfaceToCheck,
                                    getSurfacesList());
                    CharacteristicsStep6Controller.this.parentController
                            .lock(false);
                }
                CharacteristicsStep6Controller.this.parentController
                        .refreshViews();
            }
        });

        ((CharacteristicsStep6View) this.cView).getJListNeighbors()
                .addListSelectionListener(new ListSelectionListener() {

                    @Override
                    public void valueChanged(final ListSelectionEvent arg0) {
                        Surface selectedSurface = ((CharacteristicsStep6View) CharacteristicsStep6Controller.this.cView)
                                .getJListNeighbors().getSelectedValue();
                        if (CharacteristicsStep6Controller.this.highlightedSurface != null) {
                            CharacteristicsStep6Controller.this.parentController
                                    .getU3DController()
                                    .unHighlightSurface(
                                            CharacteristicsStep6Controller.this.highlightedSurface);
                        }
                        CharacteristicsStep6Controller.this.highlightedSurface = selectedSurface;
                        if (CharacteristicsStep6Controller.this.highlightedSurface != null) {
                            CharacteristicsStep6Controller.this.parentController
                                    .getU3DController()
                                    .highlightSurface(
                                            CharacteristicsStep6Controller.this.highlightedSurface);
                        }
                    }
                });
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.control.isletprocess.AbstractCharacteristicsSurfacesController
     * #addSurfaceSelected(fr.nantes1900.models.extended.Surface)
     */
    @Override
    public final void addSurfaceSelected(final Surface surfaceSelected) {
        if (this.surfaceLocked) {
            this.getSurfacesList().add(surfaceSelected);
            modifyViewCharacteristics();
        }
    }

    /**
     * Tells if the surface is locked or not.
     * @return true - the surface is locked\n false - the surface is not locked
     */
    public final boolean isSurfaceLocked() {
        return this.surfaceLocked;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.control.isletprocess.AbstractCharacteristicsSurfacesController
     * #modifyViewCharacteristics()
     */
    @Override
    public final void modifyViewCharacteristics() {
        ((CharacteristicsStep6View) this.cView).setList(this.getSurfacesList());
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.control.isletprocess.AbstractCharacteristicsSurfacesController
     * #removeSurfaceSelected(fr.nantes1900.models.extended.Surface)
     */
    @Override
    public final void removeSurfaceSelected(final Surface surfaceSelected) {
        if (this.surfaceLocked) {
            this.getSurfacesList().remove(surfaceSelected);
            modifyViewCharacteristics();
        }
    }

    /**
     * Highlights the surface.
     * @param surface
     *            the surface
     */
    public final void setHilightedSurface(final Surface surface) {
        ((CharacteristicsStep6View) this.cView).getJListNeighbors()
                .setSelectedValue(surface, true);
    }
}
