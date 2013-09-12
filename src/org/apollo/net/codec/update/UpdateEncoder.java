package org.apollo.net.codec.update;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * A {@link OneToOneEncoder} for the 'on-demand' protocol.
 * @author Graham
 */
public final class UpdateEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel c, Object msg) throws Exception {
		if (msg instanceof OnDemandResponse) {
			OnDemandResponse response = (OnDemandResponse) msg;
			ChannelBuffer container = response.getContainer();
			int type = response.getType();
			int file = response.getFile();

			ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

			int compression = container.readUnsignedByte() & 0xFF;
			int length = container.readInt();
			byte[] cachePayload = new byte[compression != 0 ? length + 4 : length];
			
			System.arraycopy(container.array(), 5, cachePayload, 0, cachePayload.length);

			buffer.writeByte((byte) type);
			buffer.writeShort(file);
			buffer.writeByte((byte) compression);
			buffer.writeInt(length);
			int offset = 8;
			for (byte aCachePayload : cachePayload) {
				if (offset == 512) {
					buffer.writeByte(255);
					offset = 1;
				}
				buffer.writeByte(aCachePayload);
				offset++;
			}
			return buffer;
		}
		return msg;
	}

}
