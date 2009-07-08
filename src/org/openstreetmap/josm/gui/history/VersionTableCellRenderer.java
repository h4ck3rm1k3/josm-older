// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.history;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.openstreetmap.josm.data.osm.OsmPrimitiveType;
import org.openstreetmap.josm.data.osm.history.HistoryOsmPrimitive;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 * The {@see TableCellRenderer} for a list of versions in {@see HistoryBrower}
 * 
 */
public class VersionTableCellRenderer extends JLabel implements TableCellRenderer {

    static private Logger logger = Logger.getLogger(VersionTableCellRenderer.class.getName());

    public final static Color BGCOLOR_SELECTED = new Color(143,170,255);
    public final static Color BGCOLOR_IS_REFERENCE_POINT = new Color(255,197,197);

    protected HashMap<OsmPrimitiveType, ImageIcon> icons = null;

    public VersionTableCellRenderer() {
        loadIcons();
        setOpaque(true);
    }

    protected void loadIcons() {
        icons = new HashMap<OsmPrimitiveType, ImageIcon>();
        icons.put(OsmPrimitiveType.NODE, ImageProvider.get("data", "node"));
        icons.put(OsmPrimitiveType.WAY, ImageProvider.get("data", "way"));
        icons.put(OsmPrimitiveType.RELATION, ImageProvider.get("data", "relation"));
    }

    protected void renderIcon(HistoryOsmPrimitive primitive) {
        ImageIcon icon = null;
        if (primitive != null) {
            icon = icons.get(primitive.getType());
        }
        setIcon(icon);
    }

    protected void renderText(HistoryOsmPrimitive primitive) {
        // render lable text
        //
        StringBuilder sb = new StringBuilder();
        if (primitive == null) {
            sb.append("");
        } else {
            sb.append(tr("Version {0}", Long.toString(primitive.getVersion())));
        }
        setText(sb.toString());

        // render tooltip text
        //
        sb = new StringBuilder();
        if (primitive == null) {
            sb.append("");
        } else {
            sb.append(
                    tr("Version {0} created on {1} by {2}",
                            Long.toString(primitive.getVersion()),
                            new SimpleDateFormat().format(primitive.getTimestamp()),
                            primitive.getUser()
                    )
            );
        }
        setToolTipText(sb.toString());
    }

    protected void renderBackground(JTable table, int row, boolean isSelected) {
        Color bgColor = Color.WHITE;
        if (isSelected) {
            bgColor = BGCOLOR_SELECTED;
        } else if (getModel(table).isReferencePointInTime(row)) {
            bgColor = BGCOLOR_IS_REFERENCE_POINT;
        }
        setBackground(bgColor);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        HistoryOsmPrimitive primitive = (HistoryOsmPrimitive)value;
        renderIcon(primitive);
        renderText(primitive);
        renderBackground(table, row, isSelected);
        return this;
    }

    protected HistoryBrowserModel.VersionTableModel getModel(JTable table) {
        return (HistoryBrowserModel.VersionTableModel)table.getModel();
    }
}