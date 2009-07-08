// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.conflict.relation;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.RelationMember;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.conflict.nodes.NodeListMerger;
import org.openstreetmap.josm.gui.conflict.nodes.NodeListMergerTest;

public class RelationMemberMergerTest extends JFrame {
    
    private RelationMemberMerger merger;
    
    protected void populate() {
        Relation r1 = new Relation();
        r1.members.add(new RelationMember("role1", new Node(1)));
        r1.members.add(new RelationMember("role2", new Way(2)));
        r1.members.add(new RelationMember("role3", new Relation(3)));

        
        Relation r2 = new Relation();
        r2.members.add(new RelationMember("role1", new Node(1)));
        r2.members.add(new RelationMember("role2", new Way(2)));
        r2.members.add(new RelationMember("role3", new Relation(3)));
        
        merger.populate(r1, r2);

    }
    
    protected void build() {
        merger = new RelationMemberMerger();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(merger, BorderLayout.CENTER);
    }
    
    public RelationMemberMergerTest() {
        build();
        populate();
    }
    
    static public void main(String args[]) {
        RelationMemberMergerTest test = new RelationMemberMergerTest();
        test.setSize(600,600);
        test.setVisible(true);
    }
}