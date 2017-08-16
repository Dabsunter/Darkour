package fr.dabsunter.darkour;

import fr.dabsunter.darkour.commands.DarkourCommand;
import fr.dabsunter.darkour.commands.ParkourCommand;
import fr.dabsunter.darkour.editor.MakerManager;
import fr.dabsunter.darkour.entity.TraceurManager;
import fr.dabsunter.darkour.parkour.DarkParkour;
import fr.dabsunter.darkour.parkour.ParkourManager;
import fr.dabsunter.darkour.util.Trein;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DarkourPlugin extends JavaPlugin {

	private File parkourFile = null;
	private FileConfiguration parkourConfig = null;

	private ParkourManager parkourManager;
	private TraceurManager traceurManager;
	private MakerManager makerManager;

	@Override
	public void onLoad() {
		saveDefaultConfig();
		saveResource("en.lang", false);
		saveResource("fr.lang", false);

		parkourFile = new File(getDataFolder(), "parkours.yml");

		getLogger().info(":Loading Trein (lang module)...");
		try {
			Trein.load(this);
			getLogger().info(":Done.");
		} catch (IOException e) {
			getLogger().warning("Failed to load Trein (" + e + ')');
			e.printStackTrace();
		}
	}

	@Override
	public void onEnable() {
		getLogger().info(":Initializing managers...");
		parkourManager = new ParkourManager(this, getParkourConfig());
		traceurManager = new TraceurManager(this);
		makerManager = new MakerManager(this);
		getLogger().info(":Done.");

		getLogger().info(":Registering commands...");
		getCommand("darkour").setExecutor(new DarkourCommand(this));
		getCommand("parkour").setExecutor(new ParkourCommand(this));
		getLogger().info(":Done.");

		getLogger().info(":Registering events listener...");
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		getLogger().info(":Done.");

		parkourManager.load();

		getLogger().info(":Scheduling tasks...");
		getServer().getScheduler().runTaskTimer(this, traceurManager, 35L, 35L);
		getServer().getScheduler().runTaskTimer(this, makerManager, 35L, 35L);
		getLogger().info(":Done.");
	}

	@Override
	public void onDisable() {
		for (DarkParkour parkour : parkourManager.getAll())
			DarkourUtils.stopEveryone(parkour);
	}

	public ParkourManager getParkourManager() {
		return parkourManager;
	}

	public TraceurManager getTraceurManager() {
		return traceurManager;
	}

	public MakerManager getMakerManager() {
		return makerManager;
	}

	public FileConfiguration getParkourConfig() {
		if (parkourConfig == null)
			reloadParkourConfig();
		return parkourConfig;
	}

	public void reloadParkourConfig() {
		parkourConfig = YamlConfiguration.loadConfiguration(parkourFile);
	}

	public void saveParkourConfig() {
		try {
			getParkourConfig().save(parkourFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save parkours to " + parkourFile, ex);
		}
	}
}
