package fr.dabsunter.darkour.api.parkour;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Represents a specific Parkour place
 */
public interface Position {

	/**
	 * Return the parkour which is holding this Position
	 *
	 * @return this position Parkour
	 */
	Parkour getParkour();

	/**
	 * Gets the world that this position resides in
	 *
	 * @return World that contains this position
	 */
	World getWorld();

	/**
	 * Gets the X-coordinate of this position
	 *
	 * @return X-coordinate
	 */
	int getX();

	/**
	 * Gets the Y-coordinate of this position
	 *
	 * @return Y-coordinate
	 */
	int getY();

	/**
	 * Gets the Z-coordinate of this position
	 *
	 * @return Z-coordinate
	 */
	int getZ();

	/**
	 * Gets the minimum Y-coordinate of this position
	 *
	 * @return minimum Y-coordinate
	 */
	double getMinY();

	/**
	 * Gets the location at the represented position
	 *
	 * @return Location at the represented position
	 */
	Location getLocation();

	/**
	 * Gets the block at the represented position
	 *
	 * @return Block at the represented position
	 */
	Block getBlock();

	/**
	 * Gets the type of this Position
	 *
	 * @return Type of this Position
	 */
	Type getType();

	/**
	 * Determine whether or not the given location is inside this position
	 *
	 * @param location the Location to test
	 * @return true if the Location is inside this Position, false otherwise
	 */
	boolean isInside(Location location);

	enum Type {
		START, CHECKPOINT, END
	}
}
