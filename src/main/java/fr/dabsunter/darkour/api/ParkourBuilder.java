package fr.dabsunter.darkour.api;

import fr.dabsunter.darkour.api.parkour.Checkpoint;
import fr.dabsunter.darkour.api.parkour.Parkour;
import fr.dabsunter.darkour.api.parkour.Position;
import fr.dabsunter.darkour.api.parkour.PreventedDamages;
import fr.dabsunter.darkour.parkour.DarkCheckpoint;
import fr.dabsunter.darkour.parkour.DarkParkour;
import fr.dabsunter.darkour.parkour.DarkPosition;
import fr.dabsunter.darkour.util.IncompleteParkourException;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ParkourBuilder is designed to easely and safely create and register new Parkours
 * in the Darkour plugin without breaking everything
 *
 * @author Dabsunter
 */
public class ParkourBuilder {

	/**
	 * Create a fresh new ParkourBuilder and set the Parkour name
	 *
	 * This is similar to :
	 * <pre><code>
	 *     new ParkourBuilder().name(name);
	 * </code></pre>
	 *
	 * @param name the name of the Parkour
	 * @return the ParkourBuilder
	 */
	public static ParkourBuilder create(String name) {
		return new ParkourBuilder().name(name);
	}

	/**
	 * Create a copy of the given ParkourBuilder
	 *
	 * This is similar to :
	 * <pre><code>
	 *     new ParkourBuilder(builder);
	 * </code></pre>
	 *
	 * @param builder the ParkourBuilder to copy
	 * @return the ParkourBuilder
	 */
	public static ParkourBuilder copy(ParkourBuilder builder) {
		return new ParkourBuilder(builder);
	}

	/**
	 * Create a copy of the given Parkour
	 *
	 * This is similar to :
	 * <pre><code>
	 *     new ParkourBuilder(parkour);
	 * </code></pre>
	 *
	 * @param parkour the Parkour to copy
	 * @return the ParkourBuilder
	 */
	public static ParkourBuilder copy(Parkour parkour) {
		return new ParkourBuilder(parkour);
	}

	/** the Parkour */
	private final DarkParkour parkour;

	/**
	 * Create a fresh new ParkourBuilder
	 */
	public ParkourBuilder() {
		Darkour.checkState();
		parkour = new DarkParkour(Darkour.plugin);
	}

	/**
	 * Create a copy of the given ParkourBuilder
	 *
	 * @param builder the ParkourBuilder to copy
	 */
	public ParkourBuilder(ParkourBuilder builder) {
		this(builder.parkour);
	}

	/**
	 * Create a copy of the given Parkour
	 *
	 * @param parkour the Parkour to copy
	 */
	public ParkourBuilder(Parkour parkour) {
		Darkour.checkState();
		this.parkour = new DarkParkour(Darkour.plugin, parkour);
	}

	/**
	 * Set the name of the Parkour
	 *
	 * @param name the name of the Parkour
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder name(String name) {
		ensureModifiable();
		parkour.setName(name);
		return this;
	}

	/**
	 * Get the name of the Parkour
	 *
	 * @return the name of the Parkour
	 */
	public String name() {
		return parkour.getName();
	}

	/**
	 * Set damages prevented by the Parkour
	 *
	 * @param preventedDamages damages prevented by the Parkour
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder preventDamage(PreventedDamages preventedDamages) {
		ensureModifiable();
		parkour.setPreventedDamages(preventedDamages);
		return this;
	}

	/**
	 * Get damages prevented by the Parkour
	 *
	 * @return damages prevented by the Parkour
	 */
	public PreventedDamages preventDamage() {
		return parkour.getPreventedDamages();
	}

	/**
	 * Set the start position of this Parkour
	 *
	 * @param position the start position
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder start(Position position) {
		ensureModifiable();
		parkour.setStart(new DarkPosition(parkour, position));
		return this;
	}

	/**
	 * Set the start position of this Parkour from a Block
	 *
	 * @param location the start position
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder start(Location location) {
		return start(location, lastMinY());
	}

	/**
	 * Set the start position of this Parkour from a Location and the given minimum Y
	 *
	 * @param location the start position
	 * @param minY the minimum Y-coordonate
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder start(Location location, double minY) {
		ensureModifiable();
		parkour.setStart(new DarkPosition(parkour, location, minY));
		return this;
	}

	/**
	 * Set the start position of this Parkour from a Block
	 *
	 * @param block the start position
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder start(Block block) {
		return start(block, lastMinY());
	}

	/**
	 * Set the start position of this Parkour from a Block and the given minimum Y
	 *
	 * @param block the start position
	 * @param minY the minimum Y-coordonate
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder start(Block block, double minY) {
		ensureModifiable();
		parkour.setStart(new DarkPosition(parkour, block, minY));
		return this;
	}

	/**
	 * Get the start position of this Parkour
	 *
	 * @return the start position
	 */
	public Position start() {
		return parkour.getStart();
	}

	/**
	 * Set the end position of this Parkour
	 *
	 * @param position the end position
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder end(Position position) {
		ensureModifiable();
		parkour.setEnd(new DarkPosition(parkour, position));
		return this;
	}

	/**
	 * Set the end position of this Parkour from a Location
	 *
	 * @param location the end position
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder end(Location location) {
		return end(location, lastMinY());
	}

	/**
	 * Set the end position of this Parkour from a Location and the given minimum Y
	 *
	 * @param location the end position
	 * @param minY the minimum Y-coordonate
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder end(Location location, double minY) {
		ensureModifiable();
		parkour.setEnd(new DarkPosition(parkour, location, minY));
		return this;
	}

	/**
	 * Set the end position of this Parkour from a Block
	 *
	 * @param block the end position
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder end(Block block) {
		return end(block, lastMinY());
	}

	/**
	 * Set the end position of this Parkour from a Block and the given minimum Y
	 *
	 * @param block the end position
	 * @param minY the minimum Y-coordonate
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder end(Block block, double minY) {
		ensureModifiable();
		parkour.setEnd(new DarkPosition(parkour, block, minY));
		return this;
	}

	/**
	 * Get the end position of this Parkour
	 *
	 * @return the end position
	 */
	public Position end() {
		return parkour.getEnd();
	}

	/**
	 * Add these Checkpoints to the Parkour
	 * The order is preserved
	 *
	 * @param checkpoints checkpoints to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpoints(Checkpoint... checkpoints) {
		return checkpoints(Arrays.asList(checkpoints));
	}

	/**
	 * Add Checkpoints contained by the given Iterable object
	 * The order provided by the Iterator is preserved
	 *
	 * @param checkpoints iterable of checkpoints to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpoints(Iterable<Checkpoint> checkpoints) {
		for (Checkpoint c : checkpoints)
			checkpoint(c);
		return this;
	}

	/**
	 * Add checkpoints from the given Locations
	 * The order is preserved
	 *
	 * @param locations checkpoints to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpointsFromLocation(Location... locations) {
		return checkpointsFromLocation(Arrays.asList(locations));
	}

	/**
	 * Add checpoints from Locations contained by the given Iterable object
	 * The order provided by the Iterator is preserved
	 *
	 * @param locations iterable of checkpoints to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpointsFromLocation(Iterable<Location> locations) {
		for (Location l : locations)
			checkpoint(l);
		return this;
	}

	/**
	 * Add checkpoints from the given Blocks
	 * The order is preserved
	 *
	 * @param blocks checkpoints to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpointsFromBlock(Block... blocks) {
		return checkpointsFromBlock(Arrays.asList(blocks));
	}

	/**
	 * Add checpoints from Blocks contained by the given Iterable object
	 * The order provided by the Iterator is preserved
	 *
	 * @param blocks iterable of checkpoints to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpointsFromBlock(Iterable<Block> blocks) {
		for (Block b : blocks)
			checkpoint(b);
		return this;
	}

	/**
	 * Get a representative List of this Parkour's chechpoints
	 * If the Parkour has checkpoints when this method is called,
	 * the returned List will always reflect the parkour's checkpoints
	 * even after modifications.
	 *
	 * @return a representative List of checkpoints
	 */
	public List<? extends Checkpoint> checkpoints() {
		return parkour.hasCheckpoints()
				? parkour.getCheckpoints()
				: Collections.emptyList();
	}

	/**
	 * Add a checkpoint to the Parkour
	 *
	 * @param checkpoint the Checkpoint to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpoint(Checkpoint checkpoint) {
		checkpointList().add(new DarkCheckpoint(parkour, checkpoint));
		return this;
	}

	/**
	 * Add a checkpoint from a Location to the Parkour
	 *
	 * @param location the checkpoint to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpoint(Location location) {
		return checkpoint(location, lastMinY());
	}

	/**
	 * Add a checkpoint from a Location and the given minimum Y to the Parkour
	 *
	 * @param location the end position
	 * @param minY the minimum Y-coordonate
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpoint(Location location, double minY) {
		checkpointList().add(new DarkCheckpoint(parkour, location, minY));
		return this;
	}

	/**
	 * Add a checkpoint from a Block to the Parkour
	 *
	 * @param block the checkpoint to add
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpoint(Block block) {
		return checkpoint(block, lastMinY());
	}

	/**
	 * Add a checkpoint from a Block and the given minimum Y to the Parkour
	 *
	 * @param block the end position
	 * @param minY the minimum Y-coordonate
	 * @return this ParkourBuilder
	 * @throws IllegalStateException if the Parkour is registered and open to public
	 */
	public ParkourBuilder checkpoint(Block block, double minY) {
		checkpointList().add(new DarkCheckpoint(parkour, block, minY));
		return this;
	}

	/**
	 * Register the built Parkour with the given tag
	 * Once upon the Parkour is registered, it will be open and accessible from public
	 *
	 * @param tag the Tag of the Parkour
	 * @return the fresh built Parkour
	 * @throws IncompleteParkourException if needed informations are missing
	 * @see IncompleteParkourException#getMissingParameter() if you want to know wich informations are missings
	 * @throws IllegalArgumentException if the given tag is already registered
	 */
	public Parkour register(String tag) {
		if (parkour.getName() == null)
			throw new IncompleteParkourException(parkour, Parkour.MissingParameter.NAME);
		if (parkour.getStart() == null)
			throw new IncompleteParkourException(parkour, Parkour.MissingParameter.START);
		if (parkour.getEnd() == null)
			throw new IncompleteParkourException(parkour, Parkour.MissingParameter.END);
		Darkour.checkState();
		Darkour.plugin.getParkourManager().register(tag, parkour);
		parkour.setOpen(true);
		return parkour;
	}

	/**
	 * Get the last minimum Y in the Parkour
	 */
	private double lastMinY() {
		if (parkour.hasCheckpoints()) {
			List<DarkCheckpoint> checkpoints = parkour.getInternalCheckpoints();
			return checkpoints.get(checkpoints.size() - 1).getMinY();
		}
		if (parkour.getStart() != null)
			return parkour.getStart().getMinY();
		return 0.0;
	}

	/**
	 * Get the modifiable Checkpoint list for modifications purpose
	 * (And also create it if it doesn't exists)
	 */
	private List<DarkCheckpoint> checkpointList() {
		ensureModifiable();
		List<DarkCheckpoint> checkpoints = parkour.getInternalCheckpoints();
		if (checkpoints == null)
			parkour.setCheckpoints((checkpoints = new ArrayList<>()));
		return checkpoints;
	}

	/**
	 * Check that there is no risks to modify internals values of the Parkour
	 */
	private void ensureModifiable() {
		if (parkour.isOpen())
			throw new IllegalStateException("Parkour is open to public, cant't modify it");
	}
}
