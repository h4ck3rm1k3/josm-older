// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.command;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.html.Option;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.conflict.Conflict;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.gui.OptionPaneUtil;
import org.openstreetmap.josm.gui.PrimitiveNameFormatter;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.tools.ImageProvider;

public class ConflictAddCommand extends Command {
    private Conflict<? extends OsmPrimitive> conflict;

    public ConflictAddCommand(OsmDataLayer layer, Conflict<? extends OsmPrimitive> conflict) {
        super(layer);
        this.conflict  = conflict;
    }

    protected void warnBecauseOfDoubleConflict() {
        OptionPaneUtil.showMessageDialog(
                Main.parent,
                tr("<html>Layer ''{0}'' already has a conflict for primitive<br>"
                        + "''{1}''.<br>"
                        + "This conflict can't be added.</html>",
                        getLayer().getName(),
                        new PrimitiveNameFormatter().getName(conflict.getMy())
                ),
                tr("Double conflict"),
                JOptionPane.ERROR_MESSAGE
        );
    }
    @Override public boolean executeCommand() {
        try {
            getLayer().getConflicts().add(conflict);
        } catch(IllegalStateException e) {
            e.printStackTrace();
            warnBecauseOfDoubleConflict();
        }
        return true;
    }

    @Override public void undoCommand() {
        if (! Main.map.mapView.hasLayer(getLayer())) {
            System.out.println(tr("Warning: layer ''{0}'' doesn't exist anymore. Can't remove conflict for primitmive ''{1}''",
                    getLayer().getName(),
                    new PrimitiveNameFormatter().getName(conflict.getMy())
            ));
            return;
        }
        getLayer().getConflicts().remove(conflict);
    }

    @Override public void fillModifiedData(Collection<OsmPrimitive> modified, Collection<OsmPrimitive> deleted, Collection<OsmPrimitive> added) {
        // nothing to fill
    }

    @Override public MutableTreeNode description() {
        return new DefaultMutableTreeNode(
                new JLabel(
                        tr("Add conflict for ''{0}''",
                                new PrimitiveNameFormatter().getName(conflict.getMy())
                        ),
                        ImageProvider.get(OsmPrimitiveType.from(conflict.getMy())),
                        JLabel.HORIZONTAL
                )
        );
    }
}