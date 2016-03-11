
package edu.kit.informatik.Exceptions;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public class VertexAlreadyExistsException extends Exception {

    /**
     * Basic constructor
     *
     * @param msg The message of this exception
     */

    public VertexAlreadyExistsException(String msg) {
        super(msg);
    }
}
