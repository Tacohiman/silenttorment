package org.apollo.net.release.r484;

import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

public class ServerMessageEventEncoder extends EventEncoder<ServerMessageEvent>{

	@Override
	public GamePacket encode(ServerMessageEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder();
		return builder.toGamePacket();
	}

}
