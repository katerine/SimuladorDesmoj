package desmoj.tutorial2.ResExample;

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a process source, which continually generates
 * ships in order to keep the simulation running.
 *
 * It will create a new ship, activate it (so that it arrives at
 * the quai) and then wait until the next ship arrival is
 * due.
 * @author Ruth Meyer
 */
public class ShipGenerator extends SimProcess {

    /**
     * Constructs a new ship generator process.
     * @param owner the model this ship generator belongs to
     * @param name this ship generator's name
     * @param showInTrace flag to indicate if this process shall produce output for the trace
     */
    public ShipGenerator(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }
    /**
     * Describes this process's life cycle: continually generate new ships.
     */
    public void lifeCycle() {

        // get a reference to the model
        ResExample model = (ResExample)getModel();

        // endless loop:
        while (true) {

            // create a new ship and let it arrive at the harbour (i.e. activate it)
            new Ship(model, "Ship", true).activate();

            // wait until next ship arrival is due
            hold(new TimeSpan(model.getShipArrivalTime(), TimeUnit.MINUTES));
        }
    }
} /* end of process class */
