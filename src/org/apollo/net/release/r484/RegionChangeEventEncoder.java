package org.apollo.net.release.r484;

import org.apollo.game.event.impl.RegionChangeEvent;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link RegionChangeEvent}.
 * @author Graham
 */
public final class RegionChangeEventEncoder extends EventEncoder<RegionChangeEvent> {

	@Override
	public GamePacket encode(RegionChangeEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(31, PacketType.VARIABLE_SHORT);
		Position pos = event.getPosition();
		int regionX = pos.getRegionX();
		int regionY = pos.getRegionY();
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, regionY);
		boolean forceSend = true;
		if((((regionX / 8) == 48) || ((regionX / 8) == 49)) && ((regionY / 8) == 48)) {
			forceSend = false;
		}
		if(((regionX / 8) == 48) && ((regionY / 8) == 148)) {
			forceSend = false;
		}
		for(int xCalc = (regionX - 6) / 8; xCalc <= ((regionX + 6) / 8); xCalc++) {
			for(int yCalc = (regionY - 6) / 8; yCalc <= ((regionY + 6) / 8); yCalc++) {
				if(forceSend || ((yCalc != 49) && (yCalc != 149) && (yCalc != 147) && (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					builder.put(DataType.INT, 0);
					builder.put(DataType.INT, 0);
					builder.put(DataType.INT, 0);
					builder.put(DataType.INT, 0);
				}
			}
		}
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, regionX);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, pos.getHeight());
		builder.put(DataType.SHORT, pos.getLocalX());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, pos.getLocalY());
		return builder.toGamePacket();
	}
}
