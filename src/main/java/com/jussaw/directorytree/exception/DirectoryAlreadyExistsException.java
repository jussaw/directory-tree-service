package com.jussaw.directorytree.exception;

/**
 * Exception thrown when a directory already exists.
 */
public class DirectoryAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public DirectoryAlreadyExistsException(String message) {
        super(message);
    }
}
