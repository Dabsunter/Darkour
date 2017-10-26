package fr.dabsunter.darkour.parkour;

import fr.dabsunter.darkour.DarkourPlugin;
import fr.dabsunter.darkour.api.event.ParkourOpenStateChangeEvent;
import fr.dabsunter.darkour.api.parkour.Checkpoint;
import fr.dabsunter.darkour.api.parkour.Parkour;
import fr.dabsunter.darkour.api.parkour.PreventedDamages;
import fr.dabsunter.darkour.entity.DarkTraceur;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static fr.dabsunter.darkour.DarkourUtils.stopEveryone;

public class DarkParkour implements Parkour {
	private final DarkourPlugin plugin;
	private String name;
	private PreventedDamages preventedDamages = PreventedDamages.ALL;
	private boolean open = false;
	private DarkPosition start;
	private DarkPosition end;
	private List<DarkCheckpoint> checkpoints;

	public DarkParkour(DarkourPlugin plugin) {
		this.plugin = plugin;
	}

	public DarkParkour(DarkourPlugin plugin, Parkour parkour) {
		this(plugin);
		name = parkour.getName();
		preventedDamages = parkour.getPreventedDamages();
		start = new DarkPosition(this, parkour.getStart());
		end = new DarkPosition(this, parkour.getEnd());
		if (parkour.hasCheckpoints()) {
			checkpoints = new ArrayList<>();
			for (Checkpoint c : parkour.getCheckpoints())
				checkpoints.add(new DarkCheckpoint(this, c));
		}
	}

	DarkParkour(DarkourPlugin plugin, ConfigurationSection section) {
		this(plugin);
		name = section.getString("name");
		preventedDamages = PreventedDamages.fromString(section.getString("prevent-damage"));
		open = true;
		start = new DarkPosition(this, section.getConfigurationSection("start"));
		end = new DarkPosition(this, section.getConfigurationSection("end"));
		if (section.isConfigurationSection("checkpoint-1")) {
			checkpoints = new ArrayList<>();
			int i = 0;
			ConfigurationSection s;
			while ((s = section.getConfigurationSection("checkpoint-" + ++i)) != null)
				checkpoints.add(new DarkCheckpoint(this, s));
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public PreventedDamages getPreventedDamages() {
		return preventedDamages;
	}

	public void setPreventedDamages(PreventedDamages preventedDamages) {
		this.preventedDamages = preventedDamages;
	}

	@Override
	public String getTag() {
		return plugin.getParkourManager().getKey(this);
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		ParkourOpenStateChangeEvent event = new ParkourOpenStateChangeEvent(this, open);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;
		if (!event.isOpen())
			stopEveryone(this);
		this.open = event.isOpen();
	}

	@Override
	public DarkPosition getStart() {
		return start;
	}

	public void setStart(DarkPosition start) {
		this.start = start;
	}

	@Override
	public DarkPosition getEnd() {
		return end;
	}

	public void setEnd(DarkPosition end) {
		this.end = end;
	}

	@Override
	public boolean hasCheckpoints() {
		return checkpoints != null && !checkpoints.isEmpty();
	}

	@Override
	public List<DarkCheckpoint> getCheckpoints() {
		return Collections.unmodifiableList(checkpoints);
	}

	public List<DarkCheckpoint> getInternalCheckpoints() {
		return checkpoints;
	}

	public void setCheckpoints(List<DarkCheckpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}

	@Override
	public Collection<DarkTraceur> getTraceurs() {
		return plugin.getTraceurManager().get(this);
	}

	@Override
	public DarkPosition getPositionAt(Location location) {
		if (start != null && start.isInside(location))
			return start;
		if (end != null && end.isInside(location))
			return end;
		if (hasCheckpoints())
			for (DarkPosition pos : checkpoints)
				if (pos.isInside(location))
					return pos;
		return null;
	}

	@Override
	public String toString() {
		return "DarkParkour{" +
				"plugin=" + plugin +
				", name='" + name + '\'' +
				", preventedDamages=" + preventedDamages +
				", start=" + start +
				", end=" + end +
				", checkpoints=" + checkpoints +
				'}';
	}

	void serialize(ConfigurationSection section) {
		section.set("name", name);
		section.set("prevent-damage", preventedDamages.toString());
		start.serialize(section.createSection("start"));
		end.serialize(section.createSection("end"));
		if (hasCheckpoints())
			for (int i = 0; i < checkpoints.size();)
				checkpoints.get(i).serialize(section.createSection("checkpoint-" + ++i));
	}
}
