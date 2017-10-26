package fr.dabsunter.darkour.api.event;

import fr.dabsunter.darkour.DarkourUtils;
import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.event.HandlerList;

/**
 * Fired when a traceur finish a parkour
 */
public class TraceurEndedEvent extends TraceurEvent {
	private static final HandlerList handlers = new HandlerList();
	private int chrono;

	public TraceurEndedEvent(Traceur traceur, Parkour parkour, int chrono) {
		super(traceur, parkour);
		this.chrono = chrono;
	}

	/**
	 * Returns the chrono the traceur has released
	 *
	 * @return the traceur's chrono in (1/10) seconds
	 */
	public int getChrono() {
		return chrono;
	}

	/**
	 * Sets the chrono the traceur has released
	 *
	 * @param chrono the traceur's chrono in (1/10) seconds
	 */
	public void setChrono(int chrono) {
		this.chrono = chrono;
	}

	/**
	 * Returns the fancy-competitive formatted version of the chrono
	 *
	 * @return the formatted chrono
	 */
	public String getFormatedChrono() {
		return DarkourUtils.formatChrono(chrono);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
