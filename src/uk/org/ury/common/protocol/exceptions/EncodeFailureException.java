/*
 * EncodeFailureException.java
 * ---------------------------
 * 
 * Part of the URY Common Package
 * 
 * V0.00  2011/03/23
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.common.protocol.exceptions;

/**
 * Exception thrown when the protocol encoder fails.
 * 
 * @author Matt Windsor
 */
public class EncodeFailureException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 8651419347999155020L;

    /**
     * Construct a new EncodeFailureException with a chained exception.
     * 
     * @param cause
     *            The exception to chain.
     */
    public EncodeFailureException(Throwable cause) {
	super("Encoding engine failed with reason: " + cause.getMessage(),
		cause);
    }
}
