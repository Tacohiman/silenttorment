package org.apollo.update;

import java.io.IOException;

import org.apollo.net.codec.update.OnDemandRequest;
import org.apollo.net.codec.update.OnDemandResponse;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.openrs.cache.Cache;

/**
 * A worker which services 'on-demand' requests.
 * @author Graham
 */
public final class OnDemandRequestWorker extends RequestWorker<OnDemandRequest, Cache> {

	/**
	 * Creates the 'on-demand' request worker.
	 * @param dispatcher The dispatcher.
	 * @param fs The file system.
	 */
	public OnDemandRequestWorker(UpdateDispatcher dispatcher, Cache fs) {
		super(dispatcher, fs);
	}

	@Override
	protected ChannelRequest<OnDemandRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextOnDemandRequest();
	}

	@Override
	protected void service(Cache cache, Channel channel, OnDemandRequest request) throws IOException {
		int type = request.getType();
		int file = request.getFile();

		ChannelBuffer buffer;
		if (type == 255 && file == 255) {
			buffer = ChannelBuffers.wrappedBuffer(cache.getTable());
		} else {
			buffer = ChannelBuffers.wrappedBuffer(cache.getStore().read(type, file));
			if (type != 255)
				buffer = buffer.slice(0, buffer.readableBytes() - 2);
		}
		channel.write(new OnDemandResponse(buffer, type, file));
	}
}
