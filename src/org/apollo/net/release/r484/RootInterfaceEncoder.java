package org.apollo.net.release.r484;

import org.apollo.game.event.impl.RootInterfaceEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

public class RootInterfaceEncoder extends EventEncoder<RootInterfaceEvent> {

	@Override
	public GamePacket encode(RootInterfaceEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(207);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getId());
		return builder.toGamePacket();
	}
}
