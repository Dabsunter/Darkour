package fr.dabsunter.darkour.api.event;

import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Fired when a traceur is about to leave the traceur mode
 */
public class PlayerParkourStopEvent extends PlayerParkourEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean isSuccess;
	private boolean isCancelled = false;

	public PlayerParkourStopEvent(Traceur traceur, Parkour parkour, boolean isSuccess) {
		super(traceur, parkour);
		this.isSuccess = isSuccess;
	}

	/**
	 * Returns whether or not the traceur has finished the parkour
	 *
	 * @return true if the player has finished the parkour, false otherwise
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * Sets whether or not the traceur has finished the parkour
	 *
	 * @param success is success
	 */
	public void setSuccess(boolean success) {
		isSuccess = success;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
