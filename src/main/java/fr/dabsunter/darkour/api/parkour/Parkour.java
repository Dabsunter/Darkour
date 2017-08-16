package fr.dabsunter.darkour.api.parkour;

import fr.dabsunter.darkour.api.entity.Traceur;
import org.bukkit.Location;

import java.util.Collection;
import java.util.List;

/**
 * Represents a Darkour Parkour
 */
public interface Parkour {

	/**
	 * Get the name of this Parkour
	 *
	 * @return the Parkour name
	 */
	String getName();

	/**
	 * Get the tag of this Parkour
	 *
	 * Note: Prefer to keep the tag yourself, the trick to retrieve de tag is much
	 * 		heavier than Parkour#getName() for example.
	 * 		This is not really significant, but it will depend on how many Parkours
	 * 		are registered and the position of this one.
	 *
	 * @return the Parkour tag
	 */
	String getTag();

	/**
	 * Determine whether or not this Parkour is open to public
	 *
	 * @return true if this Parkour is open to public, false if the parkour is modifiable
	 */
	boolean isOpen();

	/**
	 * Get which damages are prevented by this Parkour
	 *
	 * @return prevented damages
	 */
	PreventedDamages getPreventedDamages();

	/**
	 * Get the start position of this Parkour
	 *
	 * @return the start position
	 */
	Position getStart();

	/**
	 * Get the end position of this Parkour
	 *
	 * @return the end position
	 */
	Position getEnd();

	/**
	 * Determine whether or not, this Parkour has any checkpoint
	 *
	 * @return true if this Parkour has any checkpoint, false otherwise
	 */
	boolean hasCheckpoints();

	/**
	 * Return a list representation of this Parkour's chechpoints
	 * The returned list will reflect all further changes
	 *
	 * Note: to avoid any problems, check Parkour#hasCheckpoints() before
	 *
	 * @return the list of Checkpoints
	 * @throws NullPointerException in certain cases when there is no checkpoints
	 */
	List<? extends Checkpoint> getCheckpoints();

	/**
	 * Return a collection of traceurs who are in this Parkour
	 *
	 * @return a collection of playing traceurs
	 */
	Collection<? extends Traceur> getTraceurs();

	/**
	 * Try to find a Parkour's specific position at the given Location
	 *
	 * @param location the location to lookup
	 * @return the found position if any, null otherwise
	 */
	Position getPositionAt(Location location);

	enum MissingParameter {
		NAME, START, END
	}

}
