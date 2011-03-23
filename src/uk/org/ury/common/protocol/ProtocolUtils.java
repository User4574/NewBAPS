/*
 * ProtocolUtils.java
 * ------------------
 * 
 * Part of the URY Common Packages
 * 
 * V0.00  2011/03/23
 * 
 * (C) 2011 URY Computing
 */

package uk.org.ury.common.protocol;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import uk.org.ury.common.protocol.exceptions.DecodeFailureException;
import uk.org.ury.common.protocol.exceptions.EncodeFailureException;
import uk.org.ury.common.protocol.exceptions.InvalidMessageException;

/**
 * Utilities for converting between strings encoded in the response protocol and
 * collections of items, as well as validating and unpicking protocol messages.
 * 
 * @author Matt Windsor
 * 
 */
public class ProtocolUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Encode a key-value map into a protocol string.
     * 
     * The map can contain strings, lists and other maps. Other types may be
     * accepted by the underlying encoding engine, but the above types are the
     * only ones explicitly accepted.
     * 
     * @param items
     *            The key-value map of items, which should contain strings,
     *            lists and maps. The keys of any map should be protocol
     *            directives.
     * 
     * @return A string containing the encoded representation of the map.
     * 
     * @throws EncodeFailureException
     *             if the encoding engine fails to encode the map as a string.
     */
    public static String encode(Map<String, Object> items)
	    throws EncodeFailureException {
	try {
	    return mapper.writeValueAsString(items);
	} catch (Exception e) {
	    throw new EncodeFailureException(e);
	}
    }

    /**
     * Decodes a protocol string into a key-value map.
     * 
     * @param string
     *            The string to decode.
     * 
     * @return A key-value map mapping directives to strings, lists and maps.
     * 
     * @throws DecodeFailureException
     *             if the decoding engine fails to decode the string.
     */
    public static Map<String, Object> decode(String string) throws DecodeFailureException {
	try {
	    return mapper.readValue(string, new TypeReference<Map<String, Object>>() {});
	} catch (Exception e) {
	    throw new DecodeFailureException(e);
	}
    }

    /**
     * Checks if a response is flagged as having OK status.
     * 
     * @param response
     *            The response message, as a key-value map (eg in decoded
     *            format).
     * 
     * @return true if the response is flagged with OK status, false if not (eg
     *         ERROR status).
     * 
     * @throws InvalidMessageException
     *             if the response is invalid (eg the status is missing).
     */
    public static boolean responseIsOK(Map<String, Object> response)
	    throws InvalidMessageException {
	if (response.containsKey(Directive.STATUS.toString()) == false)
	    throw new InvalidMessageException("No status line in response.");

	if ((response.get(Directive.STATUS.toString()) instanceof String) == false)
	    throw new InvalidMessageException("Status is not a string.");

	return (((String) response.get(Directive.STATUS.toString()))
		.equals(Status.OK.toString()));
    }
}
