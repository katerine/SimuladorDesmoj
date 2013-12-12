package desmoj.tutorial2.ResExample;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import desmoj.core.advancedModellingFeatures.Res;
import java.util.concurrent.TimeUnit;

/**
 * This is the model class. It is the main class of a simple process-oriented
 * model of the quay of a container terminal using resources to represent berths.
 * It focuses on the allocation of berths (docking space at a quay) to incoming
 * container ships. There are eight equal-sized berths on the quay in question.
 * Every time a ship arrives it tries to allocate one, two, or three of these
 * berths depending on its size. If there is enough space available, the
 * container ship docks and starts unloading its freight. Otherwise, it has to
 * wait in a queue until other ships leave the port and free the occupied berths.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class ResExample extends Model {

    /** model parameter: the number of berths at the quay */
    static int NUM_BERTHS = 8;

    /** model parameter: the arrival rate of ships */
    static double ARRIVAL_RATE = 3.0;

    /** A random number stream for the size of the container ships */
    DiscreteDistUniform shipSizeStream;

    /** A random number stream for the inter-arrival times of ships */
    ContDistExponential shipsArrivalStream;

    /** A random number stream for the service duration (time needed for
     *  unloading at one berth) */
    ContDistNormal serviceTimeStream;

    /** A Res construct used to model the berths	*/
    Res berths;

    /**
     * ResExample constructor.
     *
     * Creates a new ResExample model via calling
     * the constructor of the superclass.
     *
     * @param owner the model this model is part of (set to <tt>null</tt> when there is no such model)
     * @param modelName this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace flag to indicate if this model shall produce output to the trace file
     */
    public ResExample(Model owner, String name, boolean showInReport, boolean showIntrace) {

        super(owner, name, showInReport, showIntrace);

    }

    /**
     * returns a description of the model to be used in the report.
     * @return model description as a string
     */
    public String description() {

        return "This is a simple model to demonstrate the use of the Res construct."+
        "In this model, the 8 berths at the quay of a container terminal are " +
        "represented by resources." +
        "A ship coming into the harbour requires 1 to 3 berths for unloading." +
        "After service the ship releases the berths again and leaves the harbour";
    }
    
    /**
     * initialises static model components like distributions and queues.
     */
    public void init() {

        // initialise distributions:
        // (1) for the size of ships
        shipSizeStream = new DiscreteDistUniform(this,"Ship size", 1, 3, true, false) ;
        // (2) for the ship's interarrival time
        shipsArrivalStream = new ContDistExponential(this, "Ship arrival",ARRIVAL_RATE, true, false);
        // (3) for the time needed to unload a ship at one berth
        serviceTimeStream = new ContDistNormal(this, "Service time", 1.0, 6.0, true, false);
        serviceTimeStream.setNonNegative(true);

        // initialise the Res construct
        berths = new Res(this, "Berths", NUM_BERTHS, true, true);
    }

    /**
     * activates dynamic model components (simulation processes).
     *
     * This method is used to place all events or processes on the
     * internal event list of the simulator which are necessary to start
     * the simulation.
     *
     * In this case, only the ship generator has to be
     * created and activated.
     */
    public void doInitialSchedules() {
        new ShipGenerator(this, "ShipGenerator", true).activate(new TimeSpan(getShipArrivalTime()));
    }

    /**
     * Returns a time span for the service time of a ship
     * Stream is set to NONNEGATIVE=TRUE
     * @return double
     */
    protected double getServiceTime() {

        return serviceTimeStream.sample();
    }

    /**
     * Returns a time span for the next interarrival time of a ship
     * Stream is set to NONNEGATIVE=TRUE
     * @return double
     */
    protected double getShipArrivalTime() {

        return shipsArrivalStream.sample();
    }

    /**
     * Returns the size of a ship in number of berths needed
     * @return int
     */
    protected long getShipSize() {

        return shipSizeStream.sample();
    }

    /** run the model */
    public static void main(String args[]) {

        // create model and experiment
        ResExample model = new ResExample(null, "ResExample", true, false);
        Experiment exp = new Experiment("ResExampleExperiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);

        // connect both
        model.connectToExperiment(exp);

        // set experiment parameters
        exp.setShowProgressBar(true);  // display a progress bar (or not)
        exp.stop(new TimeInstant(1500));   // set end of simulation at 1500 minutes
        exp.tracePeriod(new TimeInstant(0), new TimeInstant(100));  // set the period of the trace
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(50));   // and debug output

        // start the Experiment with start time 0.0
        exp.start();

        // --> now the simulation is running until it reaches its ending criterion
        // ...
        // ...
        // <-- after reaching the ending criterion, the main thread returns here

        // generate the report (and other output files)
        exp.report();

        // stop all threads still alive and close all output files
        exp.finish();
    }
}
