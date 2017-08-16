package fr.dabsunter.darkour;

import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.parkour.Checkpoint;
import fr.dabsunter.darkour.api.parkour.Parkour;
import fr.dabsunter.darkour.api.parkour.Position;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DarkourUtils {

	/**
	 * Rebuild unsplitted command arguments
	 *
	 * @param args command arguments
	 * @return joined args
	 */
	public static String join(String[] args) {
		return join(args, 0);
	}

	/**
	 * Rebuild unsplitted command arguments, starting at the given index
	 *
	 * @param args command arguments
	 * @param start start index
	 * @return joined args
	 */
	public static String join(String[] args, int start) {
		if (start >= args.length)
			return "";
		StringBuilder sb = new StringBuilder();
		for (; start < args.length; start++)
			sb.append(' ').append(args[start]);
		return sb.substring(1);
	}

	/**
	 * Returns a list extracted from the given string stream,
	 * filtered by a case-insensitive "String#startWith"
	 *
	 * @param completions a stream of all possible competions
	 * @param lastWord the last typed chars
	 * @return a matches list
	 */
	public static List<String> tabMatch(Stream<String> completions, String lastWord) {
		return completions
				.filter(c -> StringUtil.startsWithIgnoreCase(c, lastWord))
				.collect(Collectors.toList());
	}

	/**
	 * Returns the next specific position of the Parkour
	 *
	 * @param parkour the Parkour
	 * @param current the current Position
	 * @return the next Position
	 */
	public static Position nextPos(Parkour parkour, Position current) {
		switch (current.getType()) {
			case START:
				if (parkour.hasCheckpoints())
					return parkour.getCheckpoints().get(0);
				else
					return parkour.getEnd();
			case CHECKPOINT:
				return ((Checkpoint) current).getNext();
			default:
				return null;
		}
	}

	/**
	 * Returns a string that describe the current Position
	 *
	 * @param parkour the Parkour
	 * @param current the current Position
	 * @return checkpoint number if Position is a checkpoint, "-" otherwise
	 */
	public static String currentPos(Parkour parkour, Position current) {
		switch (current.getType()) {
			case CHECKPOINT:
				return String.valueOf(parkour.getCheckpoints().indexOf(current) + 1);
			default:
				return "-";
		}
	}

	/**
	 * Returns the previous specific position of the Parkour
	 *
	 * @param parkour the Parkour
	 * @param current the current Position
	 * @return the previous Position
	 */
	public static Position previousPos(Parkour parkour, Position current) {
		switch (current.getType()) {
			case CHECKPOINT:
				return ((Checkpoint) current).getPrevious();
			case END:
				if (parkour.hasCheckpoints()) {
					List<? extends Checkpoint> checkpoints = parkour.getCheckpoints();
					return checkpoints.get(checkpoints.size() - 1);
				} else {
					return parkour.getStart();
				}
			default:
				return null;
		}
	}

	/**
	 * Teleport the Traceur to the given Position
	 *
	 * @param traceur the Traceur
	 * @param position the Position
	 */
	public static void teleport(Traceur traceur, Position position) {
		Location from = traceur.getPlayer().getLocation();
		Location to = position.getLocation();
		to.setYaw(from.getYaw());
		to.setPitch(from.getPitch());
		traceur.getPlayer().teleport(to);
	}

	/**
	 * Stop all Traceurs in the given Parkour
	 *
	 * @param parkour the Parkour
	 */
	public static void stopEveryone(Parkour parkour) {
		for (Traceur traceur : parkour.getTraceurs())
			traceur.stopParkour(false);
	}

	/**
	 * Format the given chrono (in 1/10 seconds) to a competitive timer
	 *
	 * @param chrono the chrono in 1/10 seconds
	 * @return the formatted chrono
	 */
	public static String formatChrono(int chrono) {
		StringBuilder sb = new StringBuilder(10);

		int hours = chrono / 36000;
		if (hours > 0)
			sb.append(hours).append(':');
		chrono %= 36000;

		int minutes = chrono / 600;
		if (minutes < 10)
			sb.append('0');
		sb.append(minutes).append(':');
		chrono %= 600;

		int seconds = chrono / 10;
		if (seconds < 10)
			sb.append('0');
		sb.append(seconds).append('.');
		chrono %= 10;

		sb.append(chrono);

		return sb.toString();
	}

	/**
	 * Handy method that quickly add text on an ItemStack
	 *
	 * @param stack the ItemStack
	 * @param name the displayed name to set
	 * @param lore the lines of lore to set (optional)
	 * @return the ItemStack
	 */
	public static ItemStack textify(ItemStack stack, String name, String... lore) {
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		if (lore.length > 0)
			meta.setLore(Arrays.asList(lore));
		stack.setItemMeta(meta);
		return stack;
	}

	/**
	 * Returns the subsection of the ConfigurationSection described by the path.
	 * If it does not already exists, it will be created.
	 *
	 * @param parent the parent section
	 * @param path the relative path
	 * @return the subsection described by the path
	 */
	public static ConfigurationSection getChildSection(ConfigurationSection parent, String path) {
		ConfigurationSection child = parent.getConfigurationSection(path);
		if (child == null)
			child = parent.createSection(path);
		return child;
	}
}
