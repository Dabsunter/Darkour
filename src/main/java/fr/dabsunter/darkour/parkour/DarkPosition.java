package fr.dabsunter.darkour.parkour;

import fr.dabsunter.darkour.api.parkour.Position;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

public class DarkPosition implements Position {
	private final DarkParkour parkour;
	private final World world;
	private final int x, y, z;
	private final double minY;

	public DarkPosition(DarkParkour parkour, Position position) {
		this(parkour, position.getWorld(), position.getX(), position.getY(), position.getZ(), position.getMinY());
	}

	public DarkPosition(DarkParkour parkour, Location location, double minY) {
		this(parkour, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), minY);
	}

	public DarkPosition(DarkParkour parkour, Block block, double minY) {
		this(parkour, block.getWorld(), block.getX(), block.getY(), block.getZ(), minY);
	}

	DarkPosition(DarkParkour parkour, ConfigurationSection section) {
		this(parkour, Bukkit.getWorld(section.getString("world")), section.getInt("x"), section.getInt("y"),
				section.getInt("z"), section.getDouble("min-y"));
	}

	public DarkPosition(DarkParkour parkour, World world, int x, int y, int z, double minY) {
		this.parkour = parkour;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.minY = minY;
	}

	@Override
	public DarkParkour getParkour() {
		return parkour;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public double getMinY() {
		return minY;
	}

	@Override
	public Location getLocation() {
		return new Location(world, x + 0.5, y, z + 0.5);
	}

	@Override
	public Block getBlock() {
		return world.getBlockAt(x, y, z);
	}

	@Override
	public Type getType() {
		if (parkour.getStart() == this)
			return Type.START;
		if (parkour.getEnd() == this)
			return Type.END;
		throw new IllegalStateException("Orphan position object: " + toString());
	}

	@Override
	public boolean isInside(Location location) {
		return world.equals(location.getWorld())
				&& x == location.getBlockX()
				&& y == location.getBlockY()
				&& z == location.getBlockZ();
	}

	@Override
	public String toString() {
		return "DarkPosition{" +
				"parkour=" + parkour.getName() +
				", world=" + world +
				", x=" + x +
				", y=" + y +
				", z=" + z +
				", minY=" + minY +
				'}';
	}

	void serialize(ConfigurationSection section) {
		section.set("world", world.getName());
		section.set("x", x);
		section.set("y", y);
		section.set("z", z);
		section.set("min-y", minY);
	}
}
