//License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm.actions;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.visitor.Visitor;
import org.openstreetmap.josm.tools.OpenBrowser;

public class HistoryInfoAction extends JosmAction {

	public HistoryInfoAction() {
		super(tr("OSM History Information"), "about",tr("Display history information about OSM ways or nodes."), KeyEvent.VK_H, KeyEvent.SHIFT_DOWN_MASK, true);
	}

	public void actionPerformed(ActionEvent e) {
                new Visitor() {
                        public void visit(Node n) {
				OpenBrowser.displayUrl("http://www.openstreetmap.org/browse/node/" + n.id + "/history");
			}

                        public void visit(Way w) {
                                OpenBrowser.displayUrl("http://www.openstreetmap.org/browse/way/" + w.id + "/history");
                        }

                        public void visit(Relation e) {
                              OpenBrowser.displayUrl("http://www.openstreetmap.org/browse/relation/" + e.id + "/history");
                        }

                        public void visitAll() {
                                for (OsmPrimitive osm : Main.ds.getSelected())
                                        osm.visit(this);
                        }
                }.visitAll();

	}

}