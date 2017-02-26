package com.students.testapp.exception;

/**
 * Wrapper for Runtime Exception for pretty output all
 * errors to the UI.
 *
 * @author Andrii Chernysh.
 *         E-mail : itcherry97@gmail.com
 */
public class ApplicationException extends RuntimeException {
    /**
     * Message to show in the UI
     */
    private StringBuffer message;
    /**
     * Log message of concrete class
     */
    private StringBuffer logMessage;
    /**
     * Class, that generates exception
     */
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

    /**
     * Add message in way of Builder pattern
     *
     * @param message error message
     * @return exception instance
     */
    public ApplicationException addMessage(String message) {
        this.message.append(message);
        return this;
    }
    /**
     * Add message in way of Builder pattern for logger
     *
     * @param logMessage error message for logger
     * @return exception instance
     */
    public ApplicationException addLogMessage(String logMessage) {
        this.logMessage.append(logMessage);
        return this;
    }

    /**
     * @return class instance that throws exception
     */
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
