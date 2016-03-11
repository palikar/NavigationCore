/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kit.informatik.Utils;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public interface ErrorHandler {

    /**
     * Takes care of errors in form of exceptions
     *
     * @param ex Exception that should be 'dealt with '
     */
    void handelException(Exception ex);

    /**
     * Takes care of errors in form of message
     *
     * @param msg A message of a error
     */
    void printErrorMessage(String msg);

}
