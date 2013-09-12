package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

public class PlayerOptionEvent extends Event {
	
	private String option;
	private int slot;
	private boolean topSlot;
	
	public PlayerOptionEvent(String option, int slot, boolean topSlot) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}

	public int getSlot() {
		return slot;
	}

	public boolean isTopSlot() {
		return topSlot;
	}
}
