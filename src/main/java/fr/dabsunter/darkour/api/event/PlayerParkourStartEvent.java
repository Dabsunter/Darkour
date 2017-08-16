package fr.dabsunter.darkour.api.event;

import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerParkourStartEvent extends PlayerParkourEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled = false;

	public PlayerParkourStartEvent(Traceur traceur, Parkour parkour) {
		super(traceur, parkour);
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
