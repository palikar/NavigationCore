package edu.kit.informatik.RouteGraph.WigthingStrategies;

import edu.kit.informatik.RouteGraph.Connection;
import edu.kit.informatik.BasicGraphs.GraphWeightStrategy;
import edu.kit.informatik.Exceptions.VertexDoesNotExistException;
import edu.kit.informatik.RouteGraph.RouteGraph;
import java.util.Iterator;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public abstract class RouteGraphWeighStrategy implements GraphWeightStrategy<String, RouteGraph> {

    private final RouteGraph graph;

    /**
     * Abstract weight strategy for a route graph
     *
     * @param graph The graph on which the which the strategies should apply
     */
    public RouteGraphWeighStrategy(RouteGraph graph) {
        this.graph = graph;
    }

    @Override
    public int getWeigth(String vert1, String vert2) throws VertexDoesNotExistException {
        if (!graph.contains(vert1)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert1 + " in the graph");
        }
        if (!graph.contains(vert2)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert2 + " in the graph");
        }
        Iterator<Connection> it = getGraph().getConnections().iterator();
        Connection temp = Connection.getEmptyConnection(vert1, vert2);
        while (it.hasNext()) {
            Connection next = it.next();
            if (next.equals(temp)) {
                return getConnectionWeigth(next);
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public void setWeight(String vert1, String vert2, int weight) throws VertexDoesNotExistException {
        if (!graph.contains(vert1)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert1 + " in the graph");
        }
        if (!graph.contains(vert2)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert2 + " in the graph");
        }

        Iterator<Connection> it = getGraph().getConnections().iterator();
        Connection temp = Connection.getEmptyConnection(vert1, vert2);
        while (it.hasNext()) {
            Connection next = it.next();
            if (next.equals(temp)) {
                setConnectionWeigth(next, weight);
                return;
            }
        }

    }

    /**
     * Sets a weight of the connection between two cities
     *
     * @param next The connection on which the weight should be set
     * @param weight The value of the weight
     */
    protected abstract void setConnectionWeigth(Connection next, int weight);

    /**
     *
     * @param next The connection whose value should be returned
     * @return The value of the connection according to the strategy that implements this functions
     */
    protected abstract int getConnectionWeigth(Connection next);

    @Override
    public RouteGraph getGraph() {
        return graph;
    }
}
