package org.apollo.game.sync.block;

import org.apollo.game.model.Hit;

public class HitBlock extends SynchronizationBlock {
	
	private final Hit hit;
	private final int currentHitpoints;
	private final int maximumHitpoints;
	
	HitBlock(Hit hit, int currentHitpoints, int maximumHitpoints) {
		this.hit = hit;
		this.currentHitpoints = currentHitpoints;
		this.maximumHitpoints = maximumHitpoints;
	}
	
	public Hit getHit() {
		return hit;
	}

	public int getCurrentHitpoints() {
		return currentHitpoints;
	}

	public int getMaximumHitpoints() {
		return maximumHitpoints;
	}
}
