package fr.dabsunter.darkour.api;

import fr.dabsunter.darkour.DarkourPlugin;
import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.parkour.Parkour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

/**
 * This class is the main bridge to easily access Darkour data
 */
public class Darkour {
	static DarkourPlugin plugin;

	private Darkour() {}

	/**
	 * Retrieve a parkour using it's tag
	 *
	 * @param tag the Parkour tag
	 * @return the Parkour
	 * @throws IllegalArgumentException if no parkour match the given tag
	 */
	public static Parkour getParkour(String tag) {
		checkState();
		return plugin.getParkourManager().get(tag);
	}

	/**
	 * Retrieve a parkour using it's start position
	 *
	 * @param start the start position
	 * @return the Parkour if found, null otherwise
	 */
	public static Parkour getParkour(Location start) {
		checkState();
		return plugin.getParkourManager().getByStart(start);
	}

	/**
	 * Get all registered Parkours
	 * The returned Collection will reflect all further changes, no need to "reGet"
	 * if you have to stay tuned.
	 *
	 * @return a Collection of all registered Parkours
	 */
	public static Collection<? extends Parkour> getParkours() {
		checkState();
		return Collections.unmodifiableCollection(plugin.getParkourManager().getAll());
	}

	/**
	 * Retrive the Traceur assiciated to the given Player.
	 * This method never return null
	 *
	 * @param player the Player
	 * @return the associated Traceur
	 */
	public static Traceur getTraceur(Player player) {
		checkState();
		return plugin.getTraceurManager().get(player);
	}

	/**
	 * Check the state of the API link and correct this if needed
	 */
	static void checkState() {
		if (Bukkit.getPluginManager().isPluginEnabled(plugin))
			return;
		else
			plugin = (DarkourPlugin) Bukkit.getPluginManager().getPlugin("Darkour");

		if (plugin != null)
			if (plugin.isEnabled())
				plugin.getLogger().info("API has been correctly linked !");
			else
				throw new IllegalStateException("Darkour plugin is not enabled");
		else
			throw new IllegalStateException("Darkour plugin is not loaded");
	}

}
