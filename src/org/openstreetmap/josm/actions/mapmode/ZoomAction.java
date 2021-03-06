// License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm.actions.mapmode;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.SelectionManager;
import org.openstreetmap.josm.gui.SelectionManager.SelectionEnded;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.Shortcut;

/**
 * Enable the zoom mode within the MapFrame.
 *
 * Holding down the left mouse button select a rectangle with the same aspect
 * ratio than the current map view.
 * Holding down left and right let the user move the former selected rectangle.
 * Releasing the left button zoom to the selection.
 *
 * Rectangle selections with either height or width smaller than 3 pixels
 * are ignored.
 *
 * @author imi
 */
public class ZoomAction extends MapMode implements SelectionEnded {

    /**
     * Shortcut to the mapview.
     */
    private final MapView mv;
    /**
     * Manager that manages the selection rectangle with the aspect ratio of the
     * MapView.
     */
    private final SelectionManager selectionManager;

    /**
     * Construct a ZoomAction without a label.
     * @param mapFrame The MapFrame, whose zoom mode should be enabled.
     */
    public ZoomAction(MapFrame mapFrame) {
        super(tr("Zoom"), "zoom", tr("Zoom and move map"),
                Shortcut.registerShortcut("mapmode:zoom", tr("Mode: {0}", tr("Zoom")), KeyEvent.VK_Z, Shortcut.GROUP_EDIT),
                mapFrame, ImageProvider.getCursor("normal", "zoom"));
        mv = mapFrame.mapView;
        selectionManager = new SelectionManager(this, true, mv);
    }

    /**
     * Zoom to the rectangle on the map.
     */
    public void selectionEnded(Rectangle r, boolean alt, boolean shift, boolean ctrl) {
        if (r.width >= 3 && r.height >= 3) {
            mv.zoomToFactor(mv.getEastNorth(r.x+r.width/2, r.y+r.height/2), r.getWidth()/mv.getWidth());
        }
    }

    @Override public void enterMode() {
        super.enterMode();
        selectionManager.register(mv);
    }

    @Override public void exitMode() {
        super.exitMode();
        selectionManager.unregister(mv);
    }

    @Override public String getModeHelpText() {
        return tr("Zoom by dragging or Ctrl+. or Ctrl+,; move with Ctrl+up, left, down, right; move zoom with right button");
    }
}
