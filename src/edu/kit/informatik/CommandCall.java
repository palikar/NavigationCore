/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kit.informatik;

import edu.kit.informatik.BasicGraphs.GraphCommand;
import edu.kit.informatik.BasicGraphs.GraphOperations;
import edu.kit.informatik.BasicGraphs.GraphWeightStrategy;
import edu.kit.informatik.Exceptions.EdgeAlreadyExistsException;
import edu.kit.informatik.Exceptions.EdgeDoesNotExistException;
import edu.kit.informatik.Exceptions.WeigthStrategyDoesNotExist;
import edu.kit.informatik.Exceptions.VertexAlreadyExistsException;
import edu.kit.informatik.Exceptions.VertexDoesNotExistException;
import edu.kit.informatik.RouteGraph.RouteGraph;
import edu.kit.informatik.Utils.ErrorHandler;
import edu.kit.informatik.Utils.RouteGraphParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class CommandCall {

    private final Map<String, GraphCommand<String, RouteGraph>> COMMANDS;
    private final String ARGUMENT_SEPARATOR = ";";
    private final String COMMAND_SEPARATOR = " ";
    private final String QUIT_COMMAND = "quit";
    private RouteGraphParser graphParser;
    private RouteGraph graph;
    ErrorHandler errorHandler;

    public CommandCall() {

        COMMANDS = new HashMap<>();
        COMMANDS.put("search", new GraphCommand<>(this::search, 3));
        COMMANDS.put("route", new GraphCommand<>(this::route, 3));
        COMMANDS.put("remove", new GraphCommand<>(this::remove, 2));
        COMMANDS.put("insert", new GraphCommand<>(this::insert, 4));
        COMMANDS.put("info", new GraphCommand<>(this::info, 0));
        COMMANDS.put("nodes", new GraphCommand<>(this::nodes, 1));
        COMMANDS.put("vertices", new GraphCommand<>(this::vertices, 0));
    }

    /**
     * Starts the iterative sequence that takes input commands from the command
     * line
     *
     * @param errorHandler Error handler that can handle errors that happen due
     * to user input
     * @param graph The graph on which the commands will operate
     * @param parser A parser for the graph that is capable of serializing the
     * graph
     */
    public void beginInputSequence(ErrorHandler errorHandler,
            RouteGraph graph, RouteGraphParser parser) {

        graphParser = parser;
        this.graph = graph;
        this.errorHandler = errorHandler;

    }

    public void command(String command) {
        String data[] = command.split(COMMAND_SEPARATOR);
        try {
            if (COMMANDS.containsKey(data[0])) {
                if (data.length == 1) {
                    COMMANDS.get(data[0]).execute(graph, new String[0], errorHandler);
                } else {
                    COMMANDS.get(data[0]).execute(graph, data[1].split(ARGUMENT_SEPARATOR), errorHandler);
                }
            } else {
                errorHandler.printErrorMessage("There is no such command");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            errorHandler.printErrorMessage("No arguments fo the command found");
        } catch (IllegalArgumentException ex) {
            errorHandler.handelException(ex);
        }

    }

    /**
     * This command find the 'length' of the best path by given criterion
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private void search(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        try {
            final String startCiy = args[0];
            final String endCiy = args[1];
            final String criterion = args[2];
            if (!RouteGraph.isValidCityName(startCiy) || !RouteGraph.isValidCityName(endCiy)) {
                errorHandler.printErrorMessage("Invalid city name");
                return;
            }

            int pathWeigth;
            Object path[];
            if (criterion.equals("optimal")) {
                path = GraphOperations.getOptimalPathDFS(graph, startCiy, endCiy, graph.getOptimalStrategy());
                pathWeigth = GraphOperations.getPathLenth(path, graph.getOptimalStrategy());
            } else {
                GraphWeightStrategy<String, RouteGraph> weigthStrategy = graph.getWeigthStrategy(criterion);
                path = GraphOperations.getOptimalPathDijkstra(graph, startCiy, endCiy, weigthStrategy);
                pathWeigth = GraphOperations.getPathLenth(path, weigthStrategy);
            }
            Terminal.printLine(String.valueOf(pathWeigth));
        } catch (VertexDoesNotExistException | WeigthStrategyDoesNotExist ex) {
            errorHandler.handelException(ex);
        }

    }

    /**
     * This command find the best path by given criterion
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private void route(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        try {
            final String startCiy = args[0];
            final String endCiy = args[1];
            final String criterion = args[2];
            if (!RouteGraph.isValidCityName(startCiy) || !RouteGraph.isValidCityName(endCiy)) {
                errorHandler.printErrorMessage("Invalid city name");
                return;
            }

            Object path[];
            switch (criterion) {
                case "all":
                    List<Object[]> allPaths = GraphOperations.getAllPathsDFS(graph, startCiy, endCiy);
                    allPaths.forEach(p -> printPath(p));
                    return;
                case "optimal":
                    path = GraphOperations.getOptimalPathDFS(graph, startCiy, endCiy, graph.getOptimalStrategy());
                    break;
                default:
                    GraphWeightStrategy<String, RouteGraph> weigthStrategy = graph.getWeigthStrategy(criterion);
                    path = GraphOperations.getOptimalPathDijkstra(graph, startCiy, endCiy, weigthStrategy);
                    break;
            }
            printPath(path);

        } catch (VertexDoesNotExistException | WeigthStrategyDoesNotExist ex) {
            errorHandler.handelException(ex);
        }
    }

    /**
     * Helper command for printing a path of a graph on the console
     *
     * @param path Array of the objects that should be showed
     */
    private void printPath(Object[] path) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            if (i == path.length - 1) {
                builder.append(path[i]);
            } else {
                builder.append(path[i]).append(" ");
            }
        }
        Terminal.printLine(builder.toString());
    }

    /**
     * This command removes a connection between two cities. If the graph
     * becomes disconnected, the operation does not happen. If one of the cities
     * has no more connections, it also is being deleted
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private void remove(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        try {
            final String firstCity = args[0];
            final String secondCity = args[1];

            if (!RouteGraph.isValidCityName(firstCity) || !RouteGraph.isValidCityName(secondCity)) {
                errorHandler.printErrorMessage("Invalid city name");
                return;
            }

            int dist = graph.getDistanceStrategy().getWeigth(firstCity, secondCity);
            int time = graph.getTimeStrategy().getWeigth(firstCity, secondCity);
            int optimal = graph.getOptimalStrategy().getWeigth(firstCity, secondCity);

            graph.removeEdge(firstCity, secondCity);

            if (graph.neighbors(firstCity).isEmpty()) {
                graph.removeVertex(firstCity);
            }
            if (graph.neighbors(secondCity).isEmpty()) {
                graph.removeVertex(secondCity);
            }

            if (GraphOperations.getConnectedComponentsCount(graph) > 1) {

                if (!graph.contains(firstCity)) {
                    graph.addVertex(firstCity);
                }
                if (!graph.contains(secondCity)) {
                    graph.addVertex(secondCity);
                }

                graph.addEdge(firstCity, secondCity);
                graph.getDistanceStrategy().setWeight(firstCity, secondCity, dist);
                graph.getTimeStrategy().setWeight(firstCity, secondCity, time);
                graph.getOptimalStrategy().setWeight(firstCity, secondCity, optimal);
                errorHandler.printErrorMessage("Cannot execute the operaton because the graph becomes disconnected");

            } else {
                Terminal.printLine("OK");
            }

        } catch (VertexDoesNotExistException | EdgeDoesNotExistException | VertexAlreadyExistsException | EdgeAlreadyExistsException ex) {
            errorHandler.handelException(ex);
        }

    }

    /**
     * This command adds a connection between two cities as long a one of them
     * already exists
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private void insert(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        try {
            final String firstCity = args[0];
            final String secondCity = args[1];
            final int dist = Integer.parseInt(args[2]);
            final int time = Integer.parseInt(args[3]);

            if (!RouteGraph.isValidCityName(firstCity) || !RouteGraph.isValidCityName(secondCity)) {
                errorHandler.printErrorMessage("Invalid city name");
                return;
            }

            boolean isFristIn = graph.contains(firstCity);
            boolean isSecondIn = graph.contains(secondCity);
            if (isFristIn || isFristIn) {
                if (!isFristIn) {
                    graph.addVertex(firstCity);
                }
                if (!isSecondIn) {
                    graph.addVertex(secondCity);
                }
                graph.addEdge(firstCity, secondCity);
                graph.getDistanceStrategy().setWeight(firstCity, secondCity, dist);
                graph.getTimeStrategy().setWeight(firstCity, secondCity, time);
                graph.getOptimalStrategy().
                        setWeight(firstCity,
                                secondCity,
                                RouteGraph.getOptimalWeigthRule(dist, time));
                Terminal.printLine("OK");
            } else {
                errorHandler.printErrorMessage("Both of the cities do not exist");
            }
        } catch (NumberFormatException e) {
            errorHandler.printErrorMessage("Bad input for a integer value");
        } catch (VertexAlreadyExistsException | VertexDoesNotExistException | EdgeAlreadyExistsException ex) {
            errorHandler.handelException(ex);
        }

    }

    /**
     * This command serializes the graph and prints it on the screen
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private void info(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        if (graph.vertices().isEmpty()) {
            Terminal.printLine("");
        } else {
            String lines[] = graphParser.serialize(graph);
            for (String line : lines) {
                Terminal.printLine(line);
            }
        }
    }

    /**
     * This command show all adjacent cities to a given one
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private void nodes(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        try {
            final String city = args[0];
            if (!RouteGraph.isValidCityName(city)) {
                errorHandler.printErrorMessage("Invalid city name");
                return;
            }
            List<String> neighbors = graph.neighbors(city);
            neighbors.forEach(c -> Terminal.printLine(c));
        } catch (VertexDoesNotExistException ex) {
            errorHandler.handelException(ex);
        }
    }

    /**
     * This command shows all cities in the graph
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private void vertices(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        if (graph.vertices().isEmpty()) {
            Terminal.printLine("");
        } else {
            List<String> cities = graph.vertices();
            cities.forEach(c -> Terminal.printLine(c));
        }
    }
}
