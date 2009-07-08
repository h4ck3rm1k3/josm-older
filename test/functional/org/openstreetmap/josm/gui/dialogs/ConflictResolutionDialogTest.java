// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.dialogs;

import javax.swing.JFrame;

import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;

public class ConflictResolutionDialogTest extends JFrame {

    private ConflictResolutionDialog dialog;
    
    protected void build() {
        setSize(100,100);
        dialog = new ConflictResolutionDialog(this);
        dialog.setSize(600,600);
        
        
    }
    protected void populate() {
        Way w1 = new Way(1);
          w1.nodes.add(new Node(10));
          w1.nodes.add(new Node(11));
        
        Way w2 = new Way(1);
          w2.nodes.add(new Node(10));
          w2.nodes.add(new Node(11));
    
       dialog.getConflictResolver().populate(w1, w2);
    }
    
    public void showDialog() {
        dialog.setVisible(true);
    }
    
    public ConflictResolutionDialogTest() {
        build();
    }
    
    static public void main(String args[]) {
        ConflictResolutionDialogTest test = new ConflictResolutionDialogTest();
        test.setVisible(true);
        test.populate();
        test.showDialog();
    }
}