package org.apollo.net.codec.update;

import org.apollo.net.codec.update.OnDemandRequest.Priority;
import org.apollo.net.codec.update.verif.MessageRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * A {@link FrameDecoder} for the 'on-demand' protocol.
 * @author Graham
 */
public final class UpdateDecoder extends FrameDecoder {

	public static final int OUT_OF_DATE = 6, COMPLETED_REQUEST = 0;

	/**
	 * @author Graham
	 */
	private enum State {
		READ_VERSION, READ_REQUEST
	}

	private State state = State.READ_VERSION;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel c, ChannelBuffer buf) throws Exception {
		if (buf.readableBytes() >= 4) {
			if (state == State.READ_VERSION) {
				state = State.READ_REQUEST;
				int revision = buf.readInt();
				return new MessageRequest(revision);
			} else {
				int priority = buf.readUnsignedByte();
				int type = buf.readUnsignedByte();
				int file = buf.readUnsignedShort();
				if (priority == 1 || priority == 2) 
					return new OnDemandRequest(type, file, Priority.valueOf(priority));
			}
		}
		return null;
	}

}
