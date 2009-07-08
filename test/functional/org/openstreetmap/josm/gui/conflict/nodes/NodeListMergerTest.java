// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.conflict.nodes;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;

public class NodeListMergerTest extends JFrame {


    private NodeListMerger nodeListMerger;

    protected void populate() {
        Way w1 = new Way();
        Node n1;
        w1.nodes.add(n1 = new Node(1));
        for (int i=0; i < 20; i++) {
            n1.put("key" + i, "value" + i);
        }
        String note = "";
        for (int i=0; i < 50; i++) {
            note += " A very long text ";
        }
        n1.put("note", note);
        w1.nodes.add(new Node(2));
        w1.nodes.add(new Node(3));

        Way w2 = new Way();
        w2.nodes.add(new Node(4));
        w2.nodes.add(new Node(5));
        w2.nodes.add(new Node(6));

        nodeListMerger.populate(w1, w2);

    }

    protected void populateLong() {
        Way w1 = new Way();
        for (int i = 0; i < 100; i++) {
            w1.nodes.add(new Node(i));
        }

        Way w2 = new Way();
        for (int i = 1; i < 200; i+=2) {
            w2.nodes.add(new Node(i));
        }
        nodeListMerger.populate(w1, w2);

    }

    protected void build() {
        nodeListMerger = new NodeListMerger();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(nodeListMerger, BorderLayout.CENTER);
    }

    public NodeListMergerTest() {
        build();
        populate();
    }

    static public void main(String args[]) {
        NodeListMergerTest test = new NodeListMergerTest();
        test.setSize(600,600);
        test.setVisible(true);
    }


}