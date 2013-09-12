package org.apollo.game.model;

public final class Hit {
	
	public enum HitType {
		DISEASED(3), POISION(2), DAMAGE(1), MISSED_ATTACK(0);
		
		private final int type;
		
		HitType(int type) {
			this.type = type;
		}

		public int toInteger() {
			return type;
		}
	}
	
	private HitType hitType;
	private int damageReceived;
	
	public Hit(HitType hitType, int damageReceived) {
		this.hitType = hitType;
		this.damageReceived = damageReceived;
	}

	public HitType getHitType() {
		return hitType;
	}

	public int getDamageReceived() {
		return damageReceived;
	}
}
