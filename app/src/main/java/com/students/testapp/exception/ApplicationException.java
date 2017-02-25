package com.students.testapp.exception;

/**
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class ApplicationException extends RuntimeException {
    private StringBuffer message;
    private StringBuffer logMessage;
    private Class classThrowsException;

    public ApplicationException() {
        this.message = new StringBuffer();
        this.logMessage = new StringBuffer();
    }

    public ApplicationException(String message) {
        super(message);
        this.message = new StringBuffer(message);
        this.logMessage = new StringBuffer();
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.message = new StringBuffer(message);
        this.logMessage = new StringBuffer();
    }

    public ApplicationException addMessage(String message) {
        this.message.append(message);
        return this;
    }

    public ApplicationException addLogMessage(String logMessage) {
        this.logMessage.append(logMessage);
        return this;
    }

    public Class getClassThrowsException() {
        return classThrowsException;
    }

    public ApplicationException setClassThrowsException(Class classThrowsException) {
        this.classThrowsException = classThrowsException;
        return this;
    }

    @Override
    public String getMessage() {
        return message.toString();
    }

    public String getLogMessage() {
        return logMessage.toString();
    }
}
