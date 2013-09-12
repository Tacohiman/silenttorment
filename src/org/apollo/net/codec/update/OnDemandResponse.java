package org.apollo.net.codec.update;

import org.jboss.netty.buffer.ChannelBuffer;

public class OnDemandResponse {
	
	private ChannelBuffer container;
	private int type;
	private int file;
	
	public OnDemandResponse(ChannelBuffer container, int type, int file) {
		this.container = container;
		this.type = type;
		this.file = file;
	}
	
	public ChannelBuffer getContainer() {
		return container;
	}

	public int getType() {
		return type;
	}

	public int getFile() {
		return file;
	}
}
