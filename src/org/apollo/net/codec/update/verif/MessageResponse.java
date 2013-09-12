package org.apollo.net.codec.update.verif;

import org.apollo.net.codec.update.UpdateDecoder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class MessageResponse {

	private MessageRequest request;
	private int contextRevision;

	public MessageResponse(MessageRequest request, int contextRevision) {
		this.request = request;
		this.contextRevision = contextRevision;
	}

	public ChannelBuffer getBuffer() {
		ChannelBuffer buf = ChannelBuffers.buffer(1);
		buf.writeByte(request.getRevision() != contextRevision ? UpdateDecoder.OUT_OF_DATE : UpdateDecoder.COMPLETED_REQUEST);
		return buf;
	}
}
