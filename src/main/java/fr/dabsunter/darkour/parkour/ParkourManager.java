package fr.dabsunter.darkour.parkour;

import fr.dabsunter.darkour.DarkourPlugin;
import fr.dabsunter.darkour.DarkourUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParkourManager {
	private final HashMap<String, DarkParkour> parkours = new HashMap<>();
	private final DarkourPlugin plugin;
	private final ConfigurationSection config;

	public ParkourManager(DarkourPlugin plugin, ConfigurationSection config) {
		this.plugin = plugin;
		this.config = config;
	}

	public void load() {
		plugin.getLogger().info(":Loading parkours...");
		parkours.clear();
		for (String key : config.getKeys(false)) {
			plugin.getLogger().info("Loading '" + key + "'...");
			ConfigurationSection section = config.getConfigurationSection(key);

			if (section != null) {
				DarkParkour parkour = new DarkParkour(plugin, section);
				parkours.put(key, parkour);
				plugin.getLogger().info("Loaded " + parkour.getName() + " !");
			} else {
				plugin.getLogger().warning("'" + key + "' is not a valid section");
			}
		}
		plugin.getLogger().info(":Done.");
	}

	public void save() {
		plugin.getLogger().info(":Saving parkours...");

		for (String key : config.getKeys(false))
			if (!parkours.containsKey(key)) {
				config.set(key, null);
				plugin.getLogger().info("Cleared removed parkour '" + key + "'");
			}

		for (Map.Entry<String, DarkParkour> entry : parkours.entrySet()) {
			if (entry.getValue().isOpen()) {
				plugin.getLogger().info("Saving " + entry.getValue().getName() + "...");

				entry.getValue().serialize(config.createSection(entry.getKey()));

				plugin.getLogger().info("Saved '" + entry.getKey() + "' !");
			}
		}
		plugin.getLogger().info("Writing in file...");
		plugin.saveParkourConfig();
		plugin.getLogger().info(":Done.");
	}

	public DarkParkour get(String key) {
		DarkParkour parkour = parkours.get(key);
		if (parkour == null)
			throw new IllegalArgumentException("'" + key + "' is not a registered ParkourKey");
		return parkour;
	}

	public String getKey(DarkParkour parkour) {
		for (Map.Entry<String, DarkParkour> entry : parkours.entrySet())
			if (entry.getValue() == parkour)
				return entry.getKey();
		throw new IllegalArgumentException(parkour + " is not properly registered");
	}

	public Set<String> getKeys() {
		return parkours.keySet();
	}

	public Collection<DarkParkour> getAll() {
		return parkours.values();
	}

	public DarkParkour getByStart(Location location) {
		for (DarkParkour p : parkours.values())
			if (p.isOpen() && p.getStart().isInside(location))
				return p;
		return null;
	}

	public void register(String key, DarkParkour parkour) {
		if (parkours.containsKey(key))
			throw new IllegalArgumentException("'" + key + "' is an already registered ParkourKey");
		parkours.put(key, parkour);
		save();
	}

	public void unregister(String key) {
		DarkParkour parkour = get(key);
		DarkourUtils.stopEveryone(parkour);
		parkours.remove(key, parkour);
		save();
	}
}
