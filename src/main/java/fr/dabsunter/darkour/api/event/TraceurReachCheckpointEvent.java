package fr.dabsunter.darkour.api.event;

import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.parkour.Checkpoint;
import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Fired when a traceur reach a checkpoint
 */
public class TraceurReachCheckpointEvent extends TraceurEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Checkpoint checkpoint;
	private boolean isCancelled = false;

	public TraceurReachCheckpointEvent(Traceur traceur, Parkour parkour, Checkpoint checkpoint) {
		super(traceur, parkour);
		this.checkpoint = checkpoint;
	}

	/**
	 * Returns the checkpoint which has been reached
	 *
	 * @return reached Checkpoint
	 */
	public Checkpoint getCheckpoint() {
		return checkpoint;
	}

	/**
	 * Sets the reached checkpoint
	 *
	 * @param checkpoint reached Checkpoint
	 */
	public void setCheckpoint(Checkpoint checkpoint) {
		this.checkpoint = checkpoint;
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