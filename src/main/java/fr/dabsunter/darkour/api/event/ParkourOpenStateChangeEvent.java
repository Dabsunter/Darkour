package fr.dabsunter.darkour.api.event;

import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Fired when a new parkour is about to be open or not to public
 */
public class ParkourOpenStateChangeEvent extends ParkourEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled = false;

	private boolean open;

	public ParkourOpenStateChangeEvent(Parkour parkour, boolean open) {
		super(parkour);
		this.open = open;
	}

	/**
	 * Returns whether or not the parkour will be open to public
	 *
	 * @return true if the parkour will be opened, false otherwise
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Sets whether or not the parkour will be open to public
	 *
	 * @param open will be opened
	 */
	public void setOpen(boolean open) {
		this.open = open;
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
