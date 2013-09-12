package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

public final class RootInterfaceEvent extends Event {
	
	private int id;
	
	public RootInterfaceEvent(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

}
