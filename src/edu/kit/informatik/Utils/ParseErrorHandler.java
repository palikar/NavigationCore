/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kit.informatik.Utils;

import edu.kit.informatik.Terminal;

/**
 *
 * @author Stanislav
 * @version 0.0.42
 */
public class ParseErrorHandler implements ErrorHandler {

    /**
     * Makes a error handler that takes care of errors during the parsing.
     * Prints a message on the screen and then terminates the program
     */
    public ParseErrorHandler() {
    }

    @Override
    public void handelException(Exception ex) {
        Terminal.printLine("Error, " + ex.getMessage());
        System.exit(1);
    }

    @Override
    public void printErrorMessage(String msg) {
        Terminal.printLine("Error, " + msg);
        System.exit(1);
    }

}
