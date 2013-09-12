package org.apollo.net.release.r484;

import org.apollo.game.event.impl.PlayerOptionEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

public class PlayerOptionEventEncoder extends EventEncoder<PlayerOptionEvent>{

	@Override
	public GamePacket encode(PlayerOptionEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(84, PacketType.VARIABLE_BYTE);
		builder.put(DataType.BYTE, DataTransformation.ADD, event.isTopSlot() ? 1 : 0);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getSlot());
		builder.putString(event.getOption());
		return builder.toGamePacket();
	}
}
