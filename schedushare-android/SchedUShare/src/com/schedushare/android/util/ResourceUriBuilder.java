package com.schedushare.android.util;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * This is a simple uri builder class used to construct the URIs used to communicate with core insieme
 * server. This is much cleaner than doign the string manipulation to hack together URIs in the actual
 * tasks.
 */
public class ResourceUriBuilder {
	
	private String resourceUri;

	private final String hostUri;

	private final int hostPort;
	
	private String id;
	
	/**
	 * Instantiates a new resource uri builder.
	 *
	 * @param hostUri the host uri
	 * @param hostPort the host port
	 */
	@Inject
	public ResourceUriBuilder(@Named("host") 
							  final String hostUri,
							  @Named("hostPort")
							  final int hostPort) {
		this.hostUri = hostUri;
		this.hostPort = hostPort;
	}
	
	/**
	 * Sets the resource uri.
	 *
	 * @param resourceUri the resource uri
	 * @return the resource uri builder
	 */
	public ResourceUriBuilder setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
		return this;
	}
	
	/**
	 * Sets the id at the end of a URI.
	 *
	 * @param id the id
	 * @return the resource uri builder
	 */
	public ResourceUriBuilder setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Builds the qualified URI.
	 *
	 * @return the string
	 */
	public String build () {
		assert resourceUri != null : "resource uri must not be null.";
		String resourceResult = "http://"+this.hostUri+":"+String.valueOf(this.hostPort)+"/schedushare/"+this.resourceUri;
		return this.id != null 
				? resourceResult+"/"+this.id : resourceResult;
	}
	
}
