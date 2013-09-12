package org.apollo.net.release.r484;

import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

public class OpenInterfaceEventEncoder extends EventEncoder<OpenInterfaceEvent> {

	@Override
	public GamePacket encode(OpenInterfaceEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(0);
		builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId());
		builder.put(DataType.INT, DataOrder.LITTLE, event.getRootInterface() << 16 | event.getChildComponent());
		builder.put(DataType.BYTE, DataTransformation.ADD, event.getLayer());
		return builder.toGamePacket();
	}
}
