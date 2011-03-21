/*
 * UnknownFunctionException.java
 * -----------------------------
 * 
 * Part of the URY Server Platform
 * 
 * V0.00  2011/03/20
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.backend.server.exceptions;

/**
 * Exception thrown when a handler receives a request for a path that does not
 * correspond to one of its functions.
 * 
 * @author Matt Windsor
 * 
 */
public class UnknownFunctionException extends HandlingException {
    /**
     * 
     */
    private static final long serialVersionUID = -7557785978712465975L;

    /**
     * Construct a new UnknownFunctionException.
     * 
     * @param path
     *            The path that was requested.
     */
    public UnknownFunctionException(String path) {
	super("Not found: " + path);
    }

}
