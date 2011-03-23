/*
 * DecodeFailureException.java
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
 * Exception thrown when the protocol decoder fails.
 * 
 * @author Matt Windsor
 */
public class DecodeFailureException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -4080891009444990296L;

    /**
     * Construct a new DecodeFailureException with a chained exception.
     * 
     * @param cause
     *            The exception to chain.
     */
    public DecodeFailureException(Throwable cause) {
	super("Decoding engine failed with reason: " + cause.getMessage(),
		cause);
    }
}
