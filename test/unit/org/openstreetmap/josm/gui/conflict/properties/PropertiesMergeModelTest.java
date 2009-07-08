// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.conflict.properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.projection.Epsg4326;
import org.openstreetmap.josm.gui.conflict.MergeDecisionType;

public class PropertiesMergeModelTest {

    static public class ObserverTest implements Observer {
        public int numInvocations;

        public void update(Observable o, Object arg) {
            numInvocations++;
            test();
        }

        public void test() {
        }

        public void assertNumInvocations(int count) {
            assertEquals(count, numInvocations);
        }
    }

    PropertiesMergeModel model;

    @Before
    public void setUp() {
        model = new PropertiesMergeModel();
        Main.proj = new Epsg4326();
    }

    @Test
    public void populate() {
        Node n1 = new Node(1);
        Node n2 = new Node(1);
        model.populate(n1, n2);

        Way w1 = new Way(1);
        Way w2 = new Way(1);
        model.populate(w2, w2);

        Relation r1 = new Relation(1);
        Relation r2 = new Relation(1);
        model.populate(r1, r2);
    }

    @Test
    public void decidingAboutCoords() {
        Node n1 = new Node(1);
        Node n2 = new Node(1);
        model.populate(n1, n2);
        assertFalse(model.hasCoordConflict());

        n1.setCoor(new LatLon(1,1));
        model.populate(n1, n2);
        assertTrue(model.hasCoordConflict());


        n1 = new Node(1);
        n2.setCoor(new LatLon(2,2));
        model.populate(n1, n2);
        assertTrue(model.hasCoordConflict());

        n1.setCoor(new LatLon(1,1));
        n2.setCoor(new LatLon(2,2));
        model.populate(n1, n2);
        assertTrue(model.hasCoordConflict());

        // decide KEEP_MINE  and ensure notification via Observable
        //
        ObserverTest observerTest;
        model.addObserver(
                observerTest = new ObserverTest() {
                    @Override
                    public void test() {
                        assertTrue(model.isCoordMergeDecision(MergeDecisionType.KEEP_MINE));
                    }
                }
        );
        model.decideCoordsConflict(MergeDecisionType.KEEP_MINE);
        assertTrue(model.isCoordMergeDecision(MergeDecisionType.KEEP_MINE));
        observerTest.assertNumInvocations(1);

        // decide KEEP_THEIR and  ensure notification via Observable
        //
        model.deleteObserver(observerTest);
        model.addObserver(
                observerTest = new ObserverTest() {
                    @Override
                    public void test() {
                        assertTrue(model.isCoordMergeDecision(MergeDecisionType.KEEP_THEIR));
                    }
                }
        );
        model.decideCoordsConflict(MergeDecisionType.KEEP_THEIR);
        assertTrue(model.isCoordMergeDecision(MergeDecisionType.KEEP_THEIR));
        observerTest.assertNumInvocations(1);
        model.deleteObserver(observerTest);
    }


}