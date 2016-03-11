package edu.kit.informatik.BasicGraphs;

import edu.kit.informatik.Exceptions.VertexDoesNotExistException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class GraphOperations {

    private GraphOperations() {
    }

    /**
     * Operations that finds the optimal path in a given graph with a given
     * weighting strategy of the graph using the Dijkstra algorithm
     *
     * @param <X> Base type of the graph
     * @param <T> The type of the graph
     * @param <S> The type of the weighting strategy
     * @param graph The graph on which the operation should be executed
     * @param startVert The starting vertex of the path
     * @param endVert The vertex with which the path should end
     * @param weightStrategy The weighting strategy of the graph with which are
     * measured the distances between vertices
     * @return Array of objects that represent the found path;
     * @throws VertexDoesNotExistException If the start or end vertex does not
     * exist
     */
    public static <X, T extends Graph<X>, S extends GraphWeightStrategy<X, T>> Object[]
            getOptimalPathDijkstra(T graph, X startVert, X endVert, S weightStrategy)
            throws VertexDoesNotExistException {

        if (!graph.contains(endVert)) {
            throw new VertexDoesNotExistException("There is no vertex with the name " + endVert + " in the graph");
        }
        if (!graph.contains(startVert)) {
            throw new VertexDoesNotExistException("There is no vertex with the name " + startVert + " in the graph");
        }

        List<X> queue = new ArrayList<>();
        Map<X, Integer> distances = new HashMap<>();
        List<X> pathSoFar = new ArrayList<>();

        List<X> vertices = graph.vertices();
        for (int i = 0; i < vertices.size(); i++) {
            queue.add(vertices.get(i));
            distances.put(vertices.get(i), Integer.MAX_VALUE);
        }

        distances.put(startVert, 0);

        while (!queue.isEmpty()) {
            X choosen = queue.get(0);
            int best = distances.get(choosen);

            for (int i = 1; i < queue.size(); i++) {
                X newU = queue.get(i);
                int newBest = distances.get(newU);
                if (best > newBest) {
                    best = newBest;
                    choosen = newU;
                }
            }

            pathSoFar.add(choosen);
            if (choosen.equals(endVert)) {
                return pathSoFar.toArray();
            }
            queue.remove(choosen);
            List<X> neighbors = graph.neighbors(choosen);

            for (int i = 0; i < neighbors.size(); i++) {
                X nextNeigbor = neighbors.get(i);
                if (queue.contains(nextNeigbor)) {
                    int dist = distances.get(choosen) + weightStrategy.getWeigth(choosen, nextNeigbor);
                    if (dist < distances.get(nextNeigbor)) {
                        distances.put(nextNeigbor, dist);

                    }
                }
            }

        }

        return null;
    }

    /**
     * Convenient method for getOptimalPathDijkstra when the graph implements a
     * single weighting strategy on it's own
     *
     * @param <X> Base type of the graph
     * @param <T> The type of the graph
     * @param graph The graph on which the operation should be executed
     * @param startVert The starting vertex of the path
     * @param endVert The vertex with which the path should end measured the
     * @return Array of objects that represent the found path
     * @throws VertexDoesNotExistException If the start or end vertex does not
     * exist
     */
    public static <X, T extends Graph<X> & GraphWeightStrategy<X, T>> Object[]
            getOptimalPathDijkstra(T graph, X startVert, X endVert) throws VertexDoesNotExistException {
        return getOptimalPathDijkstra(graph, startVert, endVert, graph);
    }

    /**
     * Finds all possible paths(without cycles) between two vertices of a graph
     *
     * @param <X> Base type of the graph
     * @param <T> The type of the graph
     * @param graph The graph of which the paths should be found
     * @param startVert The starting vertex
     * @param endVert The ending vertex
     * @return List of arrays of objects. The arrays the represent the paths.
     * @throws VertexDoesNotExistException If the start or end vertex does not
     * exist
     */
    public static <X, T extends Graph<X>> List<Object[]>
            getAllPathsDFS(T graph, X startVert, X endVert) throws VertexDoesNotExistException {
        if (!graph.contains(endVert)) {
            throw new VertexDoesNotExistException("There is no vertex with the name " + endVert + " in the graph");
        }
        if (!graph.contains(startVert)) {
            throw new VertexDoesNotExistException("There is no vertex with the name " + startVert + " in the graph");
        }
        List<Object[]> paths = new ArrayList<>();
        dfs(graph, startVert, endVert, new ArrayList<>(), new ArrayList<>(), paths);
        return paths;
    }

    private static <X, T extends Graph<X>, S extends GraphWeightStrategy<X, T>> void
            dfs(T graph,
                    X startVert,
                    X endVert,
                    List<X> visited,
                    List<X> pathSoFar,
                    List<Object[]> paths) throws VertexDoesNotExistException {

        if (startVert.equals(endVert)) {
            pathSoFar.add(startVert);
            paths.add(pathSoFar.toArray());
            pathSoFar.remove(startVert);
            return;
        }
        visited.add(startVert);
        pathSoFar.add(startVert);
        List<X> neighbors = graph.neighbors(startVert);
        for (int i = 0; i < neighbors.size(); i++) {
            if (!visited.contains(neighbors.get(i))) {
                dfs(graph, neighbors.get(i), endVert, visited, pathSoFar, paths);
            }

        }
        visited.remove(startVert);
        pathSoFar.remove(startVert);

    }

    /**
     * Operations that finds the optimal path in a given graph with a given
     * weighting strategy of the graph. This operation first finds all possible
     * paths between the vertices and then chooses the best path (kind of
     * ridiculous, I know, but the project specification demands it this way. I
     * advise you just to use the Dijkstra algorithm with whatever strategy you
     * want).
     *
     * @param <X> Base type of the graph
     * @param <T> The type of the graph
     * @param <S> The type of the weighting strategy
     * @param graph The graph on which the operation should be executed
     * @param startVert The starting vertex of the path
     * @param endVert The vertex with which the path should end
     * @param weightStrategy The weighting strategy of the graph with which are
     * measured the distances between vertices
     * @return Array of objects that represent the found path
     * @throws VertexDoesNotExistException If the start or end vertex does not
     * exist
     */
    public static <X, T extends Graph<X>, S extends GraphWeightStrategy<X, T>> Object[]
            getOptimalPathDFS(T graph, X startVert, X endVert, S weightStrategy)
            throws VertexDoesNotExistException {
        if (!graph.contains(endVert)) {
            throw new VertexDoesNotExistException("There is no vertex with the name " + endVert + " in the graph");
        }
        if (!graph.contains(startVert)) {
            throw new VertexDoesNotExistException("There is no vertex with the name " + startVert + " in the graph");
        }
        List<Object[]> allPaths = getAllPathsDFS(graph, startVert, endVert);
        int best = getPathLenth(allPaths.get(0), weightStrategy);
        int bestIndex = 0;
        for (int i = 1; i < allPaths.size(); i++) {
            int newPath = getPathLenth(allPaths.get(i), weightStrategy);
            if (newPath < best) {
                best = newPath;
                bestIndex = i;
            }
        }
        return allPaths.get(bestIndex);

    }

    /**
     * Convenient method for getOptimalPathDFS when the graph implements a
     * weight strategy on it's own *
     *
     * @param <X> Base type of the graph
     * @param <T> The type of the graph
     * @param graph The graph on which the operation should be executed
     * @param startVert The starting vertex of the path
     * @param endVert The vertex with which the path should end
     * @return Array of objects that represent the found path
     * @throws VertexDoesNotExistException If the start or end vertex does not
     * exist
     */
    public static <X, T extends Graph<X> & GraphWeightStrategy<X, T>> Object[]
            getOptimalPathDFS(T graph, X startVert, X endVert)
            throws VertexDoesNotExistException {

        return getOptimalPathDFS(graph, startVert, endVert, graph);

    }

    /**
     * Find the 'weight' of a given path in a graph based on a given weighting
     * strategy
     *
     * @param <S> The type of the weighting strategy
     * @param path The path that should be calculated
     * @param weightStrategy The weighting strategy to be used
     * @return The weight of the path
     * @throws VertexDoesNotExistException If one of the vertices does not exist
     */
    @SuppressWarnings("unchecked")
    public static <S extends GraphWeightStrategy> int
            getPathLenth(Object[] path, S weightStrategy) throws VertexDoesNotExistException {

        int sum = 0;
        for (int i = 0; i < path.length - 1; i++) {
            sum += weightStrategy.getWeigth(path[i], path[i + 1]);
        }
        return sum;

    }

    /**
     * Finds the count of the connected components of the graph
     *
     * @param <X> Base type of the graph
     * @param <T> The type of the graph
     * @param graph The graph that should be examined
     * @return The count of the connected components
     * @throws VertexDoesNotExistException If this is thrown...SOMETHING IS GONE
     * TERRABLY WRONG. Probably you are accessing the same graph from two
     * different threads...and that is REALLY bad given the fact that the graph
     * is not synchronised. I could have made it synchronised but we haven't
     * studied multithreading yet, so....yeah
     */
    public static <X, T extends Graph<X>> int
            getConnectedComponentsCount(T graph) throws VertexDoesNotExistException {

        int connectedComponents = 0;

        List<X> visited = new ArrayList<>();
        List<X> vertices = graph.vertices();

        for (int i = 0; i < vertices.size(); i++) {

            X nextVert = vertices.get(i);

            if (visited.contains(nextVert)) {
                continue;
            }
            connectedComponents++;
            Queue<X> queue = new ArrayDeque<>();
            queue.add(nextVert);
            visited.add(nextVert);

            while (!queue.isEmpty()) {

                X vert = queue.remove();
                List<X> neigbors = graph.neighbors(vert);

                for (int j = 0; j < neigbors.size(); j++) {

                    if (!visited.contains(neigbors.get(j))) {
                        visited.add(neigbors.get(j));
                        queue.add(neigbors.get(j));
                    }
                }
            }
        }
        return connectedComponents;

    }

}
