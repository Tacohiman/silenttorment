package org.apollo.game.sync.block;

import org.apollo.game.model.Hit;

public class SecondHitBlock extends SynchronizationBlock {
	
	private final Hit hit;
	
	SecondHitBlock(Hit hit) {
		this.hit = hit;
	}
	
	public Hit getHit() {
		return hit;
	}
}
