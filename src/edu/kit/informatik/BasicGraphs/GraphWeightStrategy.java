
package edu.kit.informatik.BasicGraphs;

import edu.kit.informatik.Exceptions.VertexDoesNotExistException;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 * @param <T> The type of the vertices graph to which the strategy belongs
 * @param <G> The type of the graph to which the strategy belongs
 */
public interface GraphWeightStrategy<T, G extends Graph<T>> {

    /**
     * Returns the graph on which this weighting strategy operates
     *
     * @return A graph that is being 'weighted'
     */
    G getGraph();

    /**
     * Gets the weight of a edge of the graph
     *
     * @param vert1 Vertex1 of the edge
     * @param vert2 Vertex2 of the edge
     * @return The value of the weight
     * @throws VertexDoesNotExistException If the start or end vertex does not
     * exist
     *
     */
    int getWeigth(T vert1, T vert2) throws VertexDoesNotExistException;

    /**
     * Sets the weight of a edge of the graph
     *
     * @param vert1 Vertex1 of the edge
     * @param vert2 Vertex2 of the edge
     * @param weight The value that should be set for the edge
     * @throws VertexDoesNotExistException If the start or end vertex does not
     * exist
     *
     */
    void setWeight(T vert1, T vert2, int weight) throws VertexDoesNotExistException;

}
