package com.students.testapp.controller.exception;

import com.students.testapp.exception.ApplicationException;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class ControllerException extends ApplicationException {
    @Override
    public ControllerException addMessage(String message) {
        super.addMessage(message);
        return this;
    }

    @Override
    public ControllerException addLogMessage(String logMessage) {
        super.addLogMessage(logMessage);
        return this;
    }
}
