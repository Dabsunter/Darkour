package fr.dabsunter.darkour.api.event;

import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.event.player.PlayerEvent;

/**
 * Represents a parkour related event
 */
public abstract class PlayerParkourEvent extends PlayerEvent {
	protected Traceur traceur;
	protected Parkour parkour;

	protected PlayerParkourEvent(Traceur traceur, Parkour parkour) {
		super(traceur.getPlayer());
		this.traceur = traceur;
		this.parkour = parkour;
	}

	/**
	 * Returns the traceur involved in this event
	 *
	 * @return Traceur who is involved in this event
	 */
	public Traceur getTraceur() {
		return traceur;
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
