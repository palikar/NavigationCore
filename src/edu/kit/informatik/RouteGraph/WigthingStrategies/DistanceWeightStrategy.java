package edu.kit.informatik.RouteGraph.WigthingStrategies;

import edu.kit.informatik.RouteGraph.Connection;
import edu.kit.informatik.RouteGraph.RouteGraph;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class DistanceWeightStrategy extends RouteGraphWeighStrategy {

    /**
     * Creates a weighting strategy that's weight is the distance between two
     * cities in km
     *
     * @param graph The route graph
     */
    public DistanceWeightStrategy(RouteGraph graph) {
        super(graph);
    }

    @Override
    protected void setConnectionWeigth(Connection next, int weight) {
        next.setDistance(weight);
    }

    @Override
    protected int getConnectionWeigth(Connection next) {
        return next.getDistance();
    }

}
