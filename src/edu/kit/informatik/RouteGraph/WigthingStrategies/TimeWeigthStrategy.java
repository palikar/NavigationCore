package edu.kit.informatik.RouteGraph.WigthingStrategies;

import edu.kit.informatik.RouteGraph.Connection;
import edu.kit.informatik.RouteGraph.RouteGraph;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class TimeWeigthStrategy extends RouteGraphWeighStrategy {

    /**
     * Creates a weighting strategy that's weight is the time between two cities
     * in min
     *
     * @param graph The route graph
     */
    public TimeWeigthStrategy(RouteGraph graph) {
        super(graph);
    }

    @Override
    protected void setConnectionWeigth(Connection next, int weight) {
        next.setTime(weight);
    }

    @Override
    protected int getConnectionWeigth(Connection next) {
        return next.getTime();
    }
}
