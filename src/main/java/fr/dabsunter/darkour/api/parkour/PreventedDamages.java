package fr.dabsunter.darkour.api.parkour;

import org.bukkit.event.entity.EntityDamageEvent;

public enum PreventedDamages {
	/**
	 * Prevent all damages
	 */
	ALL {
		@Override
		public boolean preventDamage(EntityDamageEvent.DamageCause cause) {
			return true;
		}
	},
	/**
	 * Only prevent fall damages
	 *
	 * @see EntityDamageEvent.DamageCause#FALL
	 */
	FALL {
		@Override
		public boolean preventDamage(EntityDamageEvent.DamageCause cause) {
			return cause == EntityDamageEvent.DamageCause.FALL;
		}
	},
	/**
	 * Do not prevent any damages
	 */
	NONE {
		@Override
		public boolean preventDamage(EntityDamageEvent.DamageCause cause) {
			return false;
		}
	};

	/**
	 * Determine whether or not the given cause is prevented
	 *
	 * @param cause the DamageCause to test
	 * @return true if DamageCause is prevented, false otherwise
	 */
	public abstract boolean preventDamage(EntityDamageEvent.DamageCause cause);

	@Override
	public String toString() {
		return name().toLowerCase();
	}

	public static PreventedDamages fromString(String name) {
		return valueOf(name.toUpperCase());
	}
}
