package com.students.testapp.model.exception;

import com.students.testapp.exception.ApplicationException;

/**
 * Created by 1 on 24.02.2017.
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
