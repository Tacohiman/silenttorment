package org.apollo.net.codec.update.verif;

/**
 * Request for the verification part of the 'on-demand' updating.
 * 
 * @author Cjay0091
 * 
 */
public class MessageRequest {

	/**
	 * The requested revision.
	 */
	private int revision;

	/**
	 * Constructs the request;
	 */
	public MessageRequest(int revision) {
		this.revision = revision;
	}

	/**
	 * Gets the current revision made by the constructor.
	 * 
	 * @return the requested revision.
	 */
	public int getRevision() {
		return revision;
	}
}
