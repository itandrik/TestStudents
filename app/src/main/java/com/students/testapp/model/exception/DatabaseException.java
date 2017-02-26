package com.students.testapp.model.exception;

import com.students.testapp.exception.ApplicationException;

/**
 * Wrapper for Runtime Exception for pretty output all
 * errors to the UI. It is Exception, that generate database.
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class DatabaseException extends ApplicationException {
    @Override
    public DatabaseException addMessage(String message) {
        super.addMessage(message);
        return this;
    }

    @Override
    public DatabaseException addLogMessage(String logMessage) {
        super.addLogMessage(logMessage);
        return this;
    }
}
