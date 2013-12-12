

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a ship in the ResExample
 * model.
 *
 * A ship arrives at the harbour and requests
 * a number of berths according to its size. If there is
 * enough space available, it proceeds to dock at the quay and
 * is unloaded. Otherwise it has to wait.
 * After service is completed, it leaves the system.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class Ship extends SimProcess {

    /** this ship's size in number of berths needed for docking at the quay */
    private long size;

    /**
     * Constructs a new ship.
     * @param owner the model this process belongs to
     * @param name this ship's name
     * @param showInTrace flag to indicate if this process shall produce output for the trace
     */
    public Ship(Model owner, String name, boolean showInTrace) {

        super(owner, name , showInTrace);
        this.size = ((ResExample)owner).getShipSize();
    }
    /**
     * Describes this ship's life cycle:
     *
     * On arrival, the ship will request a number of berths according to its size.
     * This will result in having the ship wait until enough space is available.
     * It will then proceed to the quay for unloading.
     * After service it leaves the system.
     */
    public void lifeCycle() {

        // get a reference to the model
        ResExample model = (ResExample)getModel();

        // try to acquire the needed berths
        model.berths.provide((int)size);

        // got its resources and gets serviced
        // action print out to console
        this.sendTraceNote("is docked and gets loaded");
        // unloading depends on the size of ship
        hold(new TimeSpan(model.getServiceTime() * this.size));

        // ship has been serviced
        // releases its resources
        model.berths.takeBack((int)size);

        // leaves the system
        this.sendTraceNote("departs for the Baltic Sea");
    }
}
