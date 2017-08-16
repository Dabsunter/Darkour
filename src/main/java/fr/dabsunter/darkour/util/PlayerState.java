package fr.dabsunter.darkour.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerState {
	private final Player player;
	private ItemStack[] inventory;
	private GameMode gameMode;
	private boolean collidable;

	public PlayerState(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void save() {
		inventory = player.getInventory().getContents();
		gameMode = player.getGameMode();
		try {
			collidable = player.isCollidable();
		} catch (NoSuchMethodError e) {}
	}

	public void restore() {
		player.getInventory().setContents(inventory);
		player.setGameMode(gameMode);
		try {
			player.setCollidable(collidable);
		} catch (NoSuchMethodError e) {}
	}
}
