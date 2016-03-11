package edu.kit.informatik.Utils;

import edu.kit.informatik.Exceptions.EdgeAlreadyExistsException;
import edu.kit.informatik.Exceptions.GraphParseException;
import edu.kit.informatik.Exceptions.VertexAlreadyExistsException;
import edu.kit.informatik.Exceptions.VertexDoesNotExistException;
import edu.kit.informatik.RouteGraph.Connection;
import edu.kit.informatik.RouteGraph.RouteGraph;
import java.util.List;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class RouteGraphParser implements GraphParser<String, RouteGraph> {

    /**
     *
     * Basic parser that can read a route graph out of a file
     *
     *
     */
    public RouteGraphParser() {

    }

    @Override
    public RouteGraph deserialize(String file, RouteGraph graph) throws GraphParseException {

        try {
            String fileLines[] = FileInputHelper.read(file);
            int index = 1;
            String line = fileLines[0];
            while (!line.equals("--")) {
                if (!isValidCityName(line)) {
                    throw new GraphParseException(line + " is not a valid city name. "
                            + "These name should match " + RouteGraph.CITY_NAME_PATTERN);
                }
                graph.addVertex(line.toLowerCase());
                line = fileLines[index];
                index++;
            }
            while (index < fileLines.length) {
                line = fileLines[index];

                String connectionData[] = line.split(";");
                if (connectionData.length != 4) {
                    throw new GraphParseException("Error when parsing the input file."
                            + " A connection between cities should be descrebed"
                            + " like <City1>;<City2>;<Dostance in km>;<Distance in m>");

                }
                final int distanceKm = Integer.parseInt(connectionData[2]);
                final int distanceTime = Integer.parseInt(connectionData[3]);
                final String vert1 = connectionData[0].toLowerCase();
                final String vert2 = connectionData[1].toLowerCase();

                graph.addEdge(vert1, vert2);
                graph.getDistanceStrategy().setWeight(vert1, vert2, distanceKm);
                graph.getTimeStrategy().setWeight(vert1, vert2, distanceTime);
                graph.getOptimalStrategy().setWeight(vert1, vert2,
                        RouteGraph.getOptimalWeigthRule(distanceKm, distanceTime));

                index++;
            }

        } catch (VertexAlreadyExistsException ex) {
            throw new GraphParseException("Error when parsing the input file. Duplicate vertex was found");
        } catch (VertexDoesNotExistException ex) {
            throw new GraphParseException("Error when parsing the input file. Not existent city in the connection"
                    + " description was found");
        } catch (NumberFormatException ex) {
            throw new GraphParseException("Error when parsing the input file. Bad input for a integer value");
        } catch (EdgeAlreadyExistsException ex) {
            throw new GraphParseException("Error while parsing the input file. "
                    + "An edge occurse tweice in the given graph");
        }

        return graph;
    }

    @Override
    public String[] serialize(RouteGraph graph) {

        List<String> cities = graph.vertices();
        List<Connection> connections = graph.getConnections();
        String lines[] = new String[cities.size() + connections.size() + 1];
        int index = 0;
        while (index < cities.size()) {
            lines[index] = cities.get(index++);
        }
        lines[index++] = "--";
        while (index - cities.size() - 1 < connections.size()) {
            lines[index] = connections.get(index++ - cities.size() - 1).toString();
        }

        return lines;
    }

    private boolean isValidCityName(String name) {
        return RouteGraph.isValidCityName(name);
    }

}
