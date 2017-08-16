package fr.dabsunter.darkour.api.parkour;

/**
 * Represents a Parkour checkpoint
 */
public interface Checkpoint extends Position {

	/**
	 * Gets the position that is following this checkpoint
	 *
	 * @return the next position
	 */
	Position getNext();

	/**
	 * Gets the position that is followed by this checkpoint
	 *
	 * @return the previous position
	 */
	Position getPrevious();
}
