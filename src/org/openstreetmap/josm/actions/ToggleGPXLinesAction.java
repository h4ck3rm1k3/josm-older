// License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm.actions;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.tools.ShortCut;

public class ToggleGPXLinesAction extends JosmAction {

	public ToggleGPXLinesAction() {
		super(tr("Toggle GPX Lines"), "gps-lines", tr("Toggles the global setting ''{0}''.", tr("Draw lines between raw gps points.")),
		ShortCut.registerShortCut("view:gpxlines", tr("View: Toggle GPX lines"), KeyEvent.VK_X, ShortCut.GROUP_MENU), true);
	}

	public void actionPerformed(ActionEvent e) {
		Main.pref.put("draw.rawgps.lines", !Main.pref.getBoolean("draw.rawgps.lines"));
		Main.map.mapView.repaint();
	}
}