// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.dialogs.relation;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.DataSource;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.visitor.MergeVisitor;
import org.openstreetmap.josm.gui.OptionPaneUtil;
import org.openstreetmap.josm.gui.PleaseWaitRunnable;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.gui.progress.PleaseWaitProgressMonitor;
import org.openstreetmap.josm.io.OsmApi;
import org.openstreetmap.josm.io.OsmServerBackreferenceReader;
import org.openstreetmap.josm.io.OsmTransferException;
import org.xml.sax.SAXException;

/**
 * This is an asynchronous task for loading the parents of a given relation.
 * 
 * Typical usage:
 * <pre>
 *  final ParentRelationLoadingTask task = new ParentRelationLoadingTask(
 *                   child,   // the child relation
 *                   Main.main.getEditLayer(), // the edit layer
 *                   true,  // load fully
 *                   new PleaseWaitProgressMonitor()  // a progress monitor
 *   );
 *   task.setContinuation(
 *       new Runnable() {
 *          public void run() {
 *              if (task.isCancelled() || task.hasError())
 *                  return;
 *              List<Relation> parents = task.getParents();
 *              // do something with the parent relations
 *       }
 *   );
 *
 *   // start the task
 *   Main.worker.submit(task);
 * </pre>
 *
 */
public class ParentRelationLoadingTask extends PleaseWaitRunnable{
    private boolean cancelled;
    private Exception lastException;
    private DataSet referrers;
    private boolean full;
    private OsmDataLayer layer;
    private Relation child;
    private ArrayList<Relation> parents;
    private Runnable continuation;

    /**
     * Creates a new task for asynchronously downloading the parents of a child relation.
     * 
     * @param child the child relation. Must not be null. Must have an id > 0.
     * @param layer  the OSM data layer. Must not be null.
     * @param full if true, parent relations are fully downloaded (i.e. with their members)
     * @param monitor the progress monitor to be used
     * 
     * @exception IllegalArgumentException thrown if child is null
     * @exception IllegalArgumentException thrown if layer is null
     * @exception IllegalArgumentException thrown if child.id == 0
     */
    public ParentRelationLoadingTask(Relation child, OsmDataLayer layer, boolean full, PleaseWaitProgressMonitor monitor ) {
        super(tr("Download referring relations"), monitor, false /* don't ignore exception */);
        if (child == null)
            throw new IllegalArgumentException(tr("parameter ''{0}'' must not be null", "child"));
        if (layer == null)
            throw new IllegalArgumentException(tr("parameter ''{0}'' must not be null", "layer"));
        if (child.id == 0)
            throw new IllegalArgumentException(tr("child.id >0 expected. Got {1}", child.id));
        referrers = null;
        this.layer = layer;
        parents = new ArrayList<Relation>();
        this.child = child;
    }

    /**
     * Set a continuation which is called upon the job finished.
     * 
     * @param continuation the continuation
     */
    public void setContinuation(Runnable continuation) {
        this.continuation = continuation;
    }

    /**
     * Replies true if this has been cancelled by the user.
     * 
     * @return true if this has been cancelled by the user.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Replies true if an exception has been caught during the execution of this task.
     * 
     * @return true if an exception has been caught during the execution of this task.
     */
    public boolean hasError() {
        return lastException != null;
    }


    protected OsmDataLayer getLayer() {
        return layer;
    }

    public List<Relation> getParents() {
        return parents;
    }

    @Override
    protected void cancel() {
        cancelled = true;
        OsmApi.getOsmApi().cancel();
    }

    protected void showLastException() {
        String msg = lastException.getMessage();
        if (msg == null) {
            msg = lastException.toString();
        }
        OptionPaneUtil.showMessageDialog(
                Main.parent,
                msg,
                tr("Error"),
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    protected void finish() {
        if (cancelled) return;
        if (lastException != null) {
            showLastException();
            return;
        }
        parents.clear();
        for (Relation parent : referrers.relations) {
            parents.add((Relation)getLayer().data.getPrimitiveById(parent.id));
        }
        if (continuation != null) {
            continuation.run();
        }
    }

    @Override
    protected void realRun() throws SAXException, IOException, OsmTransferException {
        try {
            progressMonitor.indeterminateSubTask(null);
            OsmServerBackreferenceReader reader = new OsmServerBackreferenceReader(child, full);
            referrers = reader.parseOsm(progressMonitor.createSubTaskMonitor(1, false));
            if (referrers != null) {
                final MergeVisitor visitor = new MergeVisitor(getLayer().data, referrers);
                visitor.merge();

                // copy the merged layer's data source info
                for (DataSource src : referrers.dataSources) {
                    getLayer().data.dataSources.add(src);
                }
                // FIXME: this is necessary because there are  dialogs listening
                // for DataChangeEvents which manipulate Swing components on this
                // thread.
                //
                SwingUtilities.invokeLater(
                        new Runnable() {
                            public void run() {
                                getLayer().fireDataChange();
                            }
                        }
                );

                if (visitor.getConflicts().isEmpty())
                    return;
                getLayer().getConflicts().add(visitor.getConflicts());
                OptionPaneUtil.showMessageDialog(
                        Main.parent,
                        tr("There were {0} conflicts during import.",
                                visitor.getConflicts().size()),
                                tr("Warning"),
                                JOptionPane.WARNING_MESSAGE
                );
            }
        } catch(Exception e) {
            if (cancelled) {
                System.out.println(tr("Warning: ignoring exception because task is cancelled. Exception: {0}", e.toString()));
                return;
            }
            lastException = e;
        }
    }
}