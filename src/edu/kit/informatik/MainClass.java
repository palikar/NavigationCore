package edu.kit.informatik;

import edu.kit.informatik.Utils.RouteGraphParser;
import edu.kit.informatik.Exceptions.GraphParseException;
import edu.kit.informatik.RouteGraph.RouteGraph;
import edu.kit.informatik.Utils.BasicErrorHandler;
import edu.kit.informatik.Utils.ErrorHandler;
import edu.kit.informatik.Utils.ParseErrorHandler;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public final class MainClass {

    private MainClass() {

    }

    /**
     * The program main entering point
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            Terminal.printLine("Error, USAGE: <input graph file>");
            System.exit(1);
        }
        String file = args[0];
        ErrorHandler errorHandler = new ParseErrorHandler();
        RouteGraphParser parser = new RouteGraphParser();
        RouteGraph graph = null;
        try {
            graph = parser.deserialize(file, new RouteGraph());
        } catch (GraphParseException ex) {
            errorHandler.handelException(ex);
        }
        errorHandler = new BasicErrorHandler();
        CommandLine.beginInputSequence(errorHandler, graph, parser);
    }

}
