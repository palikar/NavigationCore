/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kit.informatik.Utils;

import edu.kit.informatik.BasicGraphs.Graph;
import edu.kit.informatik.Exceptions.GraphParseException;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 * @param <T> The type of the vertices of the graph
 * @param <X>The type of the graph
 */
public interface GraphParser<T, X extends Graph<T>> {

    /**
     * 'TRansforms ' a file into a graph
     *
     * @param file The path of the file containing some representation of graph
     * @param graph Empty graph that should be constructed
     * @return The 'filled' with information graph
     * @throws GraphParseException If some kind of error occurs during parsing
     */
    X deserialize(String file, X graph) throws GraphParseException;

    /**
     * 'Transforms' a graph into series of strings that easily could be saver to
     * a file
     *
     * @param graph The graph that should be made to a bunch of strings
     * @return A array strings that describe the graph
     * @throws GraphParseException If some kind of error occurs during parsing
     */
    String[] serialize(X graph) throws GraphParseException;

}
