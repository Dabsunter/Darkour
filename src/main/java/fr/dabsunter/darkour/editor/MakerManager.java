package fr.dabsunter.darkour.editor;

import fr.dabsunter.darkour.DarkourPlugin;
import fr.dabsunter.darkour.parkour.DarkParkour;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public class MakerManager implements Runnable {
	private final HashMap<Player, Maker> makers = new HashMap<>();
	private final DarkourPlugin plugin;

	public MakerManager(DarkourPlugin plugin) {
		this.plugin = plugin;
	}

	public Maker get(Player player) {
		return makers.get(player);
	}

	public Maker create(Player player, String tag) {
		DarkParkour parkour = new DarkParkour(plugin);
		plugin.getParkourManager().register(tag, parkour);
		return edit(player, parkour);
	}

	public Maker edit(Player player, DarkParkour parkour) {
		if (parkour.isOpen())
			parkour.setOpen(false);
		Maker maker = new Maker(this, player, parkour);
		Bukkit.getPluginManager().registerEvents(maker, plugin);
		makers.put(player, maker);
		return maker;
	}

	void done(Maker maker) {
		HandlerList.unregisterAll(maker);
		makers.remove(maker.getPlayer());
		plugin.getParkourManager().save();
	}

	@Override
	public void run() {
		for (Maker maker : makers.values())
			maker.sendActionBar();
	}
}
