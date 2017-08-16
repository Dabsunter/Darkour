package fr.dabsunter.darkour.commands;

import fr.dabsunter.darkour.DarkourPlugin;
import fr.dabsunter.darkour.parkour.DarkParkour;
import fr.dabsunter.darkour.util.Trein;
import fr.dabsunter.darkour.util.TreinMessages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static fr.dabsunter.darkour.DarkourUtils.tabMatch;

public class DarkourCommand implements TabExecutor {
	private final DarkourPlugin plugin;

	public DarkourCommand(DarkourPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length < 1)
				return false;
			Player player = (Player) sender;
			switch (args[0].toLowerCase()) {
				case "create": {
					if (args.length != 2) {
						player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_BADUSAGE,
								"USAGE", "/" + label + " create <tag>"));
					} else {
						String tag = args[1];
						try {
							plugin.getMakerManager().create(player, tag);
						} catch (IllegalArgumentException ex) {
							player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_PARKOUR_ALREADYEXISTS,
									"PARKOUR_TAG", tag));
						}
					}
					return true;
				}
				case "edit": {
					if (args.length != 2) {
						player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_BADUSAGE,
								"USAGE", "/" + label + " edit <tag>"));
					} else {
						String tag = args[1];
						try {
							DarkParkour parkour = plugin.getParkourManager().get(tag);
							plugin.getMakerManager().edit(player, parkour);
						} catch (IllegalArgumentException ex) {
							player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_PARKOUR_NOTEXISTS,
									"PARKOUR_TAG", tag));
						}
					}
					return true;
				}
				case "remove": {
					if (args.length != 2) {
						player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_BADUSAGE,
								"USAGE", "/" + label + " remove <tag>"));
					} else {
						String tag = args[1];
						try {
							plugin.getParkourManager().unregister(tag);
						} catch (IllegalArgumentException ex) {
							player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_PARKOUR_NOTEXISTS,
									"PARKOUR_TAG", tag));
						}
					}
					return true;
				}
			}
			return false;
		} else {
			sender.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_NOTPLAYER));
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
			case 1:
				return tabMatch(Stream.of("create", "edit", "remove", "help"), args[0]);
			case 2:
				switch (args[0].toLowerCase()) {
					case "edit":
					case "remove":
						return tabMatch(plugin.getParkourManager().getKeys().stream(), args[1]);
				}
		}
		return Collections.emptyList();
	}
}
