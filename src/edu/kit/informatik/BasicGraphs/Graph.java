package edu.kit.informatik.BasicGraphs;

import edu.kit.informatik.Exceptions.EdgeAlreadyExistsException;
import edu.kit.informatik.Exceptions.EdgeDoesNotExistException;
import edu.kit.informatik.Exceptions.VertexAlreadyExistsException;
import edu.kit.informatik.Exceptions.VertexDoesNotExistException;
import java.util.List;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 * @param <T>
 */
public interface Graph<T> {

    /**
     * Checks if a given vertex is in the graph
     *
     * @param vert The vertex to be checked
     * @return true if the vertex exists, false other wise
     */
    boolean contains(T vert);

    /**
     * Checks if two vertices are adjacent
     *
     * @param vert1 The first vertex
     * @param vert2 The second vertex
     * @return True if the vertices share and edge, false otherwise
     * @throws VertexDoesNotExistException If one of the vertices ins not in the
     * graph
     */
    boolean adjacent(T vert1, T vert2) throws VertexDoesNotExistException;

    /**
     *
     * @return A list of all of vertices in the graph
     */
    List<T> vertices();

    /**
     *
     * @param vert A vertex of the graph
     * @return A list of vertices that are neighbors of the given one
     * @throws VertexDoesNotExistException If the vertex is not in the graph
     */
    List<T> neighbors(T vert) throws VertexDoesNotExistException;

    /**
     * Adds a vertex to the graph
     *
     * @param vert The vertex to be added
     * @throws VertexAlreadyExistsException If the vertex already exists in the
     * graph
     */
    void addVertex(T vert) throws VertexAlreadyExistsException;

    /**
     * Removes a vertex to the graph
     *
     * @param vert The vertex to be removed
     * @throws VertexDoesNotExistException If the vertex does not exists in the
     * graph
     */
    void removeVertex(T vert) throws VertexDoesNotExistException;

    /**
     * Adds a edge to the graph
     *
     * @param vert1 The first vertex of the edge
     * @param vert2 The second vertex of the edge
     * @throws VertexDoesNotExistException If one of the vertices is not in the
     * graph
     * @throws EdgeAlreadyExistsException If the edge already exist
     */
    void addEdge(T vert1, T vert2) throws VertexDoesNotExistException, EdgeAlreadyExistsException;

    /**
     * Removes a edge to the graph
     *
     * @param vert1 The first vertex of the edge
     * @param vert2 The second vertex of the edge
     * @throws VertexDoesNotExistException If one of the vertices is not in the
     * graph
     * @throws EdgeDoesNotExistException If the edge does not exist...yeah those
     * exception are pretty self explanatory...
     */
    void removeEdge(T vert1, T vert2) throws VertexDoesNotExistException, EdgeDoesNotExistException;

}
