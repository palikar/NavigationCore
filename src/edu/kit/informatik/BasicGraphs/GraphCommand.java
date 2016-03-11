
package edu.kit.informatik.BasicGraphs;

import edu.kit.informatik.Utils.ErrorHandler;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 * @param <T> The type of the nodes of the graph on which the command should
 * operate
 * @param <G> The type of the graph which the command should operate
 */
public class GraphCommand<T, G extends Graph<T>> {

    private final TriConsumer<String[], G, ErrorHandler> action;
    private final int argCount;

    /**
     * Creates a basic command that can perform some kind of action on a graph
     *
     * @param action The action that should be performed
     * @param argCount The count of the arguments that the operation takes
     */
    public GraphCommand(TriConsumer<String[], G, ErrorHandler> action, int argCount) {
        this.action = action;
        this.argCount = argCount;
    }

    /**
     * When this function is called, the action on the graph in performed
     *
     * @param graph The graph on which the action should be executed
     * @param args The arguments of the actions
     * @param errorHandler Error handler that can take care of errors during the
     * execution of the action
     */
    public void execute(G graph, String args[], ErrorHandler errorHandler) {
        if (args.length != argCount) {
            throw new IllegalArgumentException("The command takes exactly " + argCount + " argument(s)");
        }
        String loweredArgs[] = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            loweredArgs[i] = args[i].toLowerCase();
        }
        action.accept(loweredArgs, graph, errorHandler);

    }

    /**
     * Interface that represents a function that takes three arguments
     *
     * @param <A> The type of the first argument
     * @param <B>The type of the second argument
     * @param <C> The type of the third argument
     */
    @FunctionalInterface
    public interface TriConsumer<A, B, C> {

        /**
         * The function that will be executed
         *
         * @param a First argument
         * @param b Second argument
         * @param c Third argument
         */
        void accept(A a, B b, C c);
    }

}
