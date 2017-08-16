package fr.dabsunter.darkour.entity;

import fr.dabsunter.darkour.DarkourPlugin;
import fr.dabsunter.darkour.parkour.DarkParkour;
import fr.dabsunter.darkour.util.Trein;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.dabsunter.darkour.util.TreinMessages.PARKOUR_ACTIONBAR;

public class TraceurManager implements Runnable {
	private final HashMap<Player, DarkTraceur> traceurs = new HashMap<>();
	private final DarkourPlugin plugin;

	public TraceurManager(DarkourPlugin plugin) {
		this.plugin = plugin;
	}

	public DarkTraceur get(Player player) {
		return traceurs.computeIfAbsent(player, DarkTraceur::new);
	}

	public Set<DarkTraceur> get(DarkParkour parkour) {
		return traceurs.values().stream()
				.filter(t -> t.getCurrentParkour() == parkour)
				.collect(Collectors.toSet());
	}

	public void startChrono(DarkTraceur traceur) {
		traceur.chronoTask = Bukkit.getScheduler().runTaskTimer(plugin, traceur, 2L, 2L);
	}

	@Override
	public void run() {
		for (DarkTraceur t : traceurs.values())
			if (t.isInParkour() && !t.isChronoRunning())
				t.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
						TextComponent.fromLegacyText(Trein.format(PARKOUR_ACTIONBAR,
								"PLAYER", t.getPlayer().getName(),
								"PLAYER_DISPLAY", t.getPlayer().getDisplayName(),
								"PARKOUR", t.getCurrentParkour().getName(),
								"CHRONO", "00:00.0",
								"CHECKPOINT", "-"
						))
				);
	}
}
