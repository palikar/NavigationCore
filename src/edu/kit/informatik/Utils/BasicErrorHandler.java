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
public final class BasicErrorHandler implements ErrorHandler {

    /**
     * Makes a error handler that takes care of basic errors. Just prints them on the screen
     */
    public BasicErrorHandler() {
    }

    @Override
    public void handelException(Exception ex) {
        Terminal.printLine("Error, " + ex.getMessage());
    }

    @Override
    public void printErrorMessage(String msg) {
        Terminal.printLine("Error, " + msg);

    }

}
