package edu.kit.informatik.RouteGraph;

import edu.kit.informatik.BasicGraphs.Graph;
import edu.kit.informatik.Exceptions.EdgeAlreadyExistsException;
import edu.kit.informatik.Exceptions.EdgeDoesNotExistException;
import edu.kit.informatik.Exceptions.WeigthStrategyDoesNotExist;
import edu.kit.informatik.Exceptions.VertexAlreadyExistsException;
import edu.kit.informatik.Exceptions.VertexDoesNotExistException;
import edu.kit.informatik.RouteGraph.WigthingStrategies.DistanceWeightStrategy;
import edu.kit.informatik.RouteGraph.WigthingStrategies.OptimalWeightStrategy;
import edu.kit.informatik.RouteGraph.WigthingStrategies.RouteGraphWeighStrategy;
import edu.kit.informatik.RouteGraph.WigthingStrategies.TimeWeigthStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class RouteGraph implements Graph<String> {

    /**
     * The pattern that all city names should match
     */
    public static final String CITY_NAME_PATTERN = "[A-Za-z-]+";

    private final List<City> cities;
    private final List<Connection> connections;
    private final Map<String, RouteGraphWeighStrategy> weigthStrategies;

    /**
     * Initializes a route graph with cities and connections between them.
     */
    public RouteGraph() {
        cities = new ArrayList<>();
        connections = new ArrayList<>();
        weigthStrategies = new HashMap<>();
        weigthStrategies.put("time", new TimeWeigthStrategy(this));
        weigthStrategies.put("route", new DistanceWeightStrategy(this));
        weigthStrategies.put("optimal", new OptimalWeightStrategy(this));

    }

    @Override
    public boolean contains(String vert) {
        return cities.contains(City.getEmptyCity(vert));
    }

    @Override

    public boolean adjacent(String vert1, String vert2) throws VertexDoesNotExistException {
        if (!contains(vert1)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert1 + " in the graph");
        }
        if (!contains(vert2)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert2 + " in the graph");
        }

        return this.connections.contains(Connection.getEmptyConnection(vert1, vert2));
    }

    @Override
    public List<String> vertices() {
        return cities.stream().map(c -> c.getName()).collect(Collectors.toList());
    }

    @Override
    public List<String> neighbors(String vert) throws VertexDoesNotExistException {
        if (!contains(vert)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert + " in the graph");
        }
        final ArrayList<String> neighborsSofar = new ArrayList<>();
        City temp = City.getEmptyCity(vert);
        connections.forEach((Connection c) -> {
            if (c.getFromCity().equals(temp)) {
                neighborsSofar.add(c.getToCity().getName());
            } else if (c.getToCity().equals(temp)) {
                neighborsSofar.add(c.getFromCity().getName());
            }
        });
        return neighborsSofar;
    }

    @Override
    public void addVertex(String vert) throws VertexAlreadyExistsException {
        if (contains(vert)) {
            throw new VertexAlreadyExistsException("There is already city with the name " + vert + " in the graph");
        }
        cities.add(new City(vert));
    }

    @Override
    public void removeVertex(String vert) throws VertexDoesNotExistException {
        if (!contains(vert)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert + " in the graph");
        }

        City temp = City.getEmptyCity(vert);
        cities.remove(City.getEmptyCity(vert));
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getFromCity().equals(temp)
                    || connections.get(i).getToCity().equals(temp)) {
                connections.remove(i);
            }
        }
    }

    @Override
    public void addEdge(String vert1, String vert2) throws VertexDoesNotExistException, EdgeAlreadyExistsException {
        if (!contains(vert1)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert1 + " in the graph");
        }
        if (!contains(vert2)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert2 + " in the graph");
        }
        if (!connections.contains(Connection.getEmptyConnection(vert1, vert2))) {
            connections.add(new Connection(new City(vert1), new City(vert2)));
        } else {
            throw new EdgeAlreadyExistsException("There already is a connection between " + vert1 + " and " + vert2);
        }

    }

    @Override
    public void removeEdge(String vert1, String vert2) throws VertexDoesNotExistException, EdgeDoesNotExistException {
        if (!contains(vert1)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert1 + " in the graph");
        }
        if (!contains(vert2)) {
            throw new VertexDoesNotExistException("There is no city with the name " + vert2 + " in the graph");
        }
        if (!connections.remove(Connection.getEmptyConnection(vert1, vert2))) {
            throw new EdgeDoesNotExistException("There is no connections between " + vert1 + " and " + vert2);
        }
    }

    /**
     *
     * @return All of the connections between cities in the route graph
     */
    public List<Connection> getConnections() {
        return connections;
    }

    /**
     * Gets one of the weighting strategy of the route graph by it's name
     *
     * @param strategy The name of the strategy
     * @return The strategy
     * @throws WeigthStrategyDoesNotExist If the there is no strategy with this
     * name
     */
    public RouteGraphWeighStrategy getWeigthStrategy(String strategy) throws WeigthStrategyDoesNotExist {
        if (!this.weigthStrategies.containsKey(strategy)) {
            throw new WeigthStrategyDoesNotExist("There is no weight strategy with this name in this route graph");
        }
        return this.weigthStrategies.get(strategy);
    }

    /**
     *
     * @return A weighting strategy which represents the distance between two
     * cities in km
     */
    public RouteGraphWeighStrategy getDistanceStrategy() {
        return this.weigthStrategies.get("route");
    }

    /**
     *
     * @return A weighting strategy which represents the time between two cities
     * in min
     */
    public RouteGraphWeighStrategy getTimeStrategy() {
        return this.weigthStrategies.get("time");
    }

    /**
     *
     * @return A weighting strategy which represents the optimal path-length
     * value between two cities
     */
    public RouteGraphWeighStrategy getOptimalStrategy() {
        return this.weigthStrategies.get("optimal");
    }

    /**
     * Checks if a given city name is valid
     *
     * @param name Name of the city that should be checked
     * @return true if it is valid name of a city
     */
    public static boolean isValidCityName(String name) {
        return name.matches(CITY_NAME_PATTERN);
    }

    /**
     * The optimal value for a connection between cities is calculated according
     * to this method
     *
     * @param distanceKm The distance between the cities in km
     * @param distanceTime The time between the cities in min
     * @return The optimal value
     */
    public static int getOptimalWeigthRule(int distanceKm, int distanceTime) {
        return distanceKm * distanceKm + distanceTime * distanceTime;
    }

}
