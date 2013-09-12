package org.apollo.net.release.r484;

import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.game.event.impl.PlayerOptionEvent;
import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.event.impl.RegionChangeEvent;
import org.apollo.game.event.impl.RootInterfaceEvent;
import org.apollo.game.event.impl.UpdateSkillEvent;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

public class Release484 extends Release {

	public static final int PACKET_SIZES[] = new int[256];

	static {
		for (int i = 0; i < PACKET_SIZES.length; i++)
			PACKET_SIZES[i] = -3;
	}

	public Release484() {
		super(484, PacketMetaDataGroup.createFromArray(PACKET_SIZES));
		init();
	}

	private void init() {
		register(RegionChangeEvent.class, new RegionChangeEventEncoder());
		register(RootInterfaceEvent.class, new RootInterfaceEncoder());
		register(OpenInterfaceEvent.class, new OpenInterfaceEventEncoder());
		register(UpdateSkillEvent.class, new UpdateSkillEventEncoder());
		register(PlayerOptionEvent.class, new PlayerOptionEventEncoder());
		register(PlayerSynchronizationEvent.class, new PlayerSynchronizationEventEncoder());
	}
}
