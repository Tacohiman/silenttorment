package org.apollo.net.release.r484;

import org.apollo.game.event.impl.UpdateSkillEvent;
import org.apollo.game.model.Skill;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

public class UpdateSkillEventEncoder extends EventEncoder<UpdateSkillEvent>{

	@Override
	public GamePacket encode(UpdateSkillEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(89);
		Skill skill = event.getSkill();
		
		builder.put(DataType.INT, DataOrder.LITTLE, skill.getExperience());
		builder.put(DataType.BYTE, DataTransformation.ADD, skill.getMaximumLevel());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getId());
		return builder.toGamePacket();
	}

}
