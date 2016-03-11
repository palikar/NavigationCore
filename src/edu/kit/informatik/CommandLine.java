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
public final class CommandLine {

    private static final Map<String, GraphCommand<String, RouteGraph>> COMMANDS;
    private static final String ARGUMENT_SEPARATOR = ";";
    private static final String COMMAND_SEPARATOR = " ";
    private static final String QUIT_COMMAND = "quit";
    private static RouteGraphParser graphParser;

    private CommandLine() {

    }

    static {
        COMMANDS = new HashMap<>();
        COMMANDS.put("search", new GraphCommand<>(CommandLine::search, 3));
        COMMANDS.put("route", new GraphCommand<>(CommandLine::route, 3));
        COMMANDS.put("remove", new GraphCommand<>(CommandLine::remove, 2));
        COMMANDS.put("insert", new GraphCommand<>(CommandLine::insert, 4));
        COMMANDS.put("info", new GraphCommand<>(CommandLine::info, 0));
        COMMANDS.put("nodes", new GraphCommand<>(CommandLine::nodes, 1));
        COMMANDS.put("vertices", new GraphCommand<>(CommandLine::vertices, 0));

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
    public static void beginInputSequence(ErrorHandler errorHandler,
            RouteGraph graph, RouteGraphParser parser) {

        graphParser = parser;
        while (true) {
            String command = Terminal.readLine();
            if (command == null) {
                System.exit(1);
            }
            String data[] = command.split(COMMAND_SEPARATOR);

            try {
                if (data[0].equals(QUIT_COMMAND)) {
                    System.exit(0);
                } else if (COMMANDS.containsKey(data[0])) {
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

    }

    /**
     * This command find the 'length' of the best path by given criterion
     *
     * @param args The arguments of the command
     * @param graph The graph on which the command should be executed
     * @param errorHandler Error handler that can take care of errors that
     * happen during the command's execution
     */
    private static void search(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
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
    private static void route(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
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
    private static void printPath(Object[] path) {
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
    private static void remove(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
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

        } catch (VertexDoesNotExistException | EdgeDoesNotExistException
                | VertexAlreadyExistsException | EdgeAlreadyExistsException ex) {
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
    private static void insert(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
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
    private static void info(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
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
    private static void nodes(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
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
    private static void vertices(String[] args, RouteGraph graph, ErrorHandler errorHandler) {
        if (graph.vertices().isEmpty()) {
            Terminal.printLine("");
        } else {
            List<String> cities = graph.vertices();
            cities.forEach(c -> Terminal.printLine(c));
        }
    }
}
