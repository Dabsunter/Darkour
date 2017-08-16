package fr.dabsunter.darkour.api.entity;

import fr.dabsunter.darkour.api.parkour.Parkour;
import fr.dabsunter.darkour.api.parkour.Position;
import org.bukkit.entity.Player;

/**
 * Represents a Darkour player
 */
public interface Traceur {

	/**
	 * Gets the Bukkit's Player
	 *
	 * @return the player
	 */
	Player getPlayer();

	/**
	 * Determine whether or not this player is in parkour
	 *
	 * @return true if this Traceur is in parkour, false otherwise
	 */
	boolean isInParkour();

	/**
	 * Gets the parkour this player is currently playing
	 *
	 * @return the current parkour if the player is in parkour, null otherwise
	 */
	Parkour getCurrentParkour();

	/**
	 * Gets the last postition validated by the player
	 * May be a checkpoint or the start position
	 *
	 * @return the last valid position if the player is in parkour, null otherwise
	 */
	Position getLastValidPosition();

	/**
	 * Determine whether or not the chrono is running
	 *
	 * @return true if the chrono is running, false otherwise
	 */
	boolean isChronoRunning();

	/**
	 * Gets the current player chrono in (1/10) seconds
	 *
	 * @return the chrono
	 */
	int getChrono();

	/**
	 * Try to setup everything to let the player trace the parkour
	 *
	 * @param parkour the parkour to trace
	 */
	void startParkour(Parkour parkour);

	/**
	 * Try to stop everything and leave the traceur mode
	 *
	 * @param success whether or not the player has finished the parkour
	 */
	void stopParkour(boolean success);
}
