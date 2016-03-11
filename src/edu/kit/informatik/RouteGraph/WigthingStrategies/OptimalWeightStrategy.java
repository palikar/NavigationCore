
package edu.kit.informatik.RouteGraph.WigthingStrategies;

import edu.kit.informatik.RouteGraph.Connection;
import edu.kit.informatik.RouteGraph.RouteGraph;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class OptimalWeightStrategy extends RouteGraphWeighStrategy {

    /**
     * Creates a weighting strategy that's weight is the optimal path-length
     * between two cities in km
     *
     * @param graph The route graph
     */
    public OptimalWeightStrategy(RouteGraph graph) {
        super(graph);
    }

    @Override
    protected void setConnectionWeigth(Connection next, int weight) {
        next.setOptimal(weight);
    }

    @Override
    protected int getConnectionWeigth(Connection next) {
        return next.getOptimal();
    }

}
