package fr.dabsunter.darkour.api.event;

import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.event.Event;

/**
 * Represents a parkour related event
 */
public abstract class ParkourEvent extends Event {
	protected Parkour parkour;

	protected ParkourEvent(Parkour parkour) {
		super();
		this.parkour = parkour;
	}

	/**
	 * Returns the parkour involved in this event
	 *
	 * @return Parkour which is involved in this event
	 */
	public Parkour getParkour() {
		return parkour;
	}
}
