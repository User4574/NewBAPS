package uk.org.ury.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Map;

import uk.org.ury.protocol.ProtocolUtils;
import uk.org.ury.protocol.exceptions.DecodeFailureException;

public class Client {
    /**
     * Get a raw response from the server.
     * 
     * @param file
     *            The "file", including path and query string, to fetch from the
     *            server.
     * 
     * @return The response from the server, as a key-value map.
     * 
     * @throws DecodeFailureException
     *             if the decode failed.
     */
    public Map<?, ?> get(String file) throws DecodeFailureException {
	URL url = null;
	URLConnection uc = null;
	String result = "";

	try {
	    url = new URL("http://localhost:8000" + file);
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	try {
	    uc = url.openConnection();

	    BufferedReader in = new BufferedReader(new InputStreamReader(
		    uc.getInputStream()));

	    String inputLine;

	    for (inputLine = in.readLine(); inputLine != null; inputLine = in
		    .readLine()) {
		result += inputLine;
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return ProtocolUtils.decode(result);
    }
}
