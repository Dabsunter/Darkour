package fr.dabsunter.darkour.commands;

import fr.dabsunter.darkour.DarkourPlugin;
import fr.dabsunter.darkour.entity.DarkTraceur;
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

public class ParkourCommand implements TabExecutor {
	private final DarkourPlugin plugin;

	public ParkourCommand(DarkourPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length < 1)
				return false;
			Player player = (Player) sender;
			switch (args[0].toLowerCase()) {
				case "join": {
					if (args.length != 2) {
						player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_BADUSAGE,
								"USAGE", "/" + label + " join <tag>"));
					} else {
						String tag = args[1];
						DarkParkour parkour;
						try {
							parkour = plugin.getParkourManager().get(tag);
						} catch (IllegalArgumentException ex) {
							player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_PARKOUR_NOTEXISTS,
									"PARKOUR_TAG", tag));
							return true;
						}
						DarkTraceur traceur = plugin.getTraceurManager().get(player);
						try {
							traceur.startParkour(parkour);
						} catch (IllegalArgumentException ex) {
							player.sendMessage(Trein.format(TreinMessages.PARKOUR_CHAT_NOPERM,
									"PARKOUR", parkour.getName()));
						} catch (IllegalStateException ex) {
							player.sendMessage(Trein.format(TreinMessages.PARKOUR_CHAT_CANTLEAVE,
									"PARKOUR", traceur.getCurrentParkour().getName()));
						}
					}
					return true;
				}
				case "leave": {
					if (args.length != 1) {
						player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_BADUSAGE,
								"USAGE", "/" + label + " leave"));
					} else {
						DarkTraceur traceur = plugin.getTraceurManager().get(player);
						if (traceur.isInParkour()) {
							traceur.stopParkour(false);
							if (traceur.isInParkour())
								player.sendMessage(Trein.format(TreinMessages.PARKOUR_CHAT_CANTLEAVE,
										"PARKOUR", traceur.getCurrentParkour().getName()));
						} else {
							player.sendMessage(Trein.format(TreinMessages.COMMAND_ERROR_TRACEUR_NOTINPARKOUR));
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
				return tabMatch(Stream.of("join", "leave", "help"), args[0]);
			case 2:
				switch (args[0].toLowerCase()) {
					case "join":
						return tabMatch(plugin.getParkourManager().getKeys().stream()
								.filter(k -> sender.hasPermission("darkour.parkour." + k)),
								args[1]);
				}
		}
		return Collections.emptyList();
	}
}
