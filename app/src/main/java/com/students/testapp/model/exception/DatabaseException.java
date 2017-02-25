package com.students.testapp.model.exception;

import com.students.testapp.exception.ApplicationException;

/**
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
