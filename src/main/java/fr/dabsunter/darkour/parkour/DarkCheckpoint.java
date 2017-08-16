package fr.dabsunter.darkour.parkour;

import fr.dabsunter.darkour.api.parkour.Checkpoint;
import fr.dabsunter.darkour.api.parkour.Position;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class DarkCheckpoint extends DarkPosition implements Checkpoint {

	public DarkCheckpoint(DarkParkour parkour, Checkpoint checkpoint) {
		super(parkour, checkpoint);
	}

	public DarkCheckpoint(DarkParkour parkour, Location location, double minY) {
		super(parkour, location, minY);
	}

	public DarkCheckpoint(DarkParkour parkour, Block block, double minY) {
		super(parkour, block, minY);
	}

	public DarkCheckpoint(DarkParkour parkour, World world, int x, int y, int z, double minY) {
		super(parkour, world, x, y, z, minY);
	}

	DarkCheckpoint(DarkParkour parkour, ConfigurationSection section) {
		super(parkour, section);
	}

	@Override
	public Position getNext() {
		List<DarkCheckpoint> checkpoints = getParkour().getCheckpoints();
		int index = checkpoints.indexOf(this);
		if (++index == checkpoints.size())
			return getParkour().getEnd();
		return checkpoints.get(index);
	}

	@Override
	public Position getPrevious() {
		List<DarkCheckpoint> checkpoints = getParkour().getCheckpoints();
		int index = checkpoints.indexOf(this);
		if (index == 0)
			return getParkour().getStart();
		return checkpoints.get(--index);
	}

	@Override
	public Type getType() {
		return Type.CHECKPOINT;
	}
}
