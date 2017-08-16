package fr.dabsunter.darkour.entity;

import fr.dabsunter.darkour.DarkourUtils;
import fr.dabsunter.darkour.api.entity.Traceur;
import fr.dabsunter.darkour.api.event.PlayerParkourEndedEvent;
import fr.dabsunter.darkour.api.event.PlayerParkourReachCheckpointEvent;
import fr.dabsunter.darkour.api.event.PlayerParkourStartEvent;
import fr.dabsunter.darkour.api.event.PlayerParkourStopEvent;
import fr.dabsunter.darkour.api.parkour.Parkour;
import fr.dabsunter.darkour.api.parkour.Position;
import fr.dabsunter.darkour.parkour.DarkCheckpoint;
import fr.dabsunter.darkour.parkour.DarkParkour;
import fr.dabsunter.darkour.parkour.DarkPosition;
import fr.dabsunter.darkour.util.PlayerState;
import fr.dabsunter.darkour.util.Trein;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import static fr.dabsunter.darkour.DarkourUtils.*;
import static fr.dabsunter.darkour.util.TreinMessages.*;

public class DarkTraceur implements Traceur, Runnable {
	private final Player player;
	private final PlayerState playerState;
	private DarkParkour currentParkour;
	private DarkPosition lastValidPosition;
	private int chrono = 0;
	BukkitTask chronoTask;

	DarkTraceur(Player player) {
		this.player = player;
		this.playerState = new PlayerState(player);
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	public PlayerState getPlayerState() {
		return playerState;
	}

	@Override
	public boolean isInParkour() {
		return currentParkour != null;
	}

	@Override
	public DarkParkour getCurrentParkour() {
		return currentParkour;
	}

	@Override
	public DarkPosition getLastValidPosition() {
		return lastValidPosition;
	}

	public void setLastValidPosition(DarkPosition lastValidPosition) {
		if (lastValidPosition.getType() == Position.Type.END) {
			stopParkour(true);
		} else {
			if (lastValidPosition.getType() == Position.Type.CHECKPOINT) {
				PlayerParkourReachCheckpointEvent event =
						new PlayerParkourReachCheckpointEvent(this, currentParkour, (DarkCheckpoint) lastValidPosition);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled())
					return;
				lastValidPosition = (DarkPosition) event.getCheckpoint();
			}
			this.lastValidPosition = lastValidPosition;
		}
	}

	@Override
	public boolean isChronoRunning() {
		return chronoTask != null;
	}

	@Override
	public int getChrono() {
		return chrono;
	}

	@Override
	public void startParkour(Parkour parkour) {
		if (!player.hasPermission("darkour.parkour." + parkour.getTag()))
			throw new IllegalArgumentException("Traceur has not the permission to trace this parkour");

		if (isInParkour()) {
			stopParkour(false);
			if (isInParkour())
				throw new IllegalStateException("Traceur is already in parkour and is not allowed to leave it");
		}
		PlayerParkourStartEvent event = new PlayerParkourStartEvent(this, parkour);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
			return;

		playerState.save();
		{
			PlayerInventory pi = player.getInventory();
			pi.clear();
			pi.setItem(3, textify(new ItemStack(Material.BLAZE_POWDER), Trein.format(PARKOUR_ITEM_RETRY)));
			pi.setItem(5, textify(new ItemStack(Material.COMPASS), Trein.format(PARKOUR_ITEM_LEAVE)));
		}
		player.setGameMode(GameMode.ADVENTURE);
		player.setCollidable(false);
		DarkourUtils.teleport(this, parkour.getStart());
		currentParkour = (DarkParkour) parkour;
		lastValidPosition = currentParkour.getStart();
		player.sendMessage(Trein.format(PARKOUR_CHAT_START,
				"PLAYER", player.getName(),
				"PLAYER_DISPLAY", player.getDisplayName(),
				"PARKOUR", currentParkour.getName()));
	}

	@Override
	public void stopParkour(boolean success) {
		if (!isInParkour())
			throw new IllegalStateException("Traceur is not in parkour");
		PlayerParkourStopEvent stopEvent = new PlayerParkourStopEvent(this, currentParkour, success);
		Bukkit.getPluginManager().callEvent(stopEvent);
		if (stopEvent.isCancelled())
			return;

		int chrono = stopChrono();
		playerState.restore();
		if (stopEvent.isSuccess()) {
			PlayerParkourEndedEvent endedEvent = new PlayerParkourEndedEvent(this, currentParkour, chrono);
			Bukkit.getPluginManager().callEvent(endedEvent);
			player.sendMessage(Trein.multiline(Trein.format(PARKOUR_CHAT_SUCCESS,
					"PLAYER", player.getName(),
					"PLAYER_DISPLAY", player.getDisplayName(),
					"PARKOUR", currentParkour.getName(),
					"CHRONO", endedEvent.getFormatedChrono())));
		}
		player.sendMessage(Trein.format(PARKOUR_CHAT_STOP,
				"PLAYER", player.getName(),
				"PLAYER_DISPLAY", player.getDisplayName(),
				"PARKOUR", currentParkour.getName()));
		currentParkour = null;
		lastValidPosition = null;
	}

	@Override
	public void run() {
		chrono++;
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
				TextComponent.fromLegacyText(Trein.format(PARKOUR_ACTIONBAR,
						"PLAYER", player.getName(),
						"PLAYER_DISPLAY", player.getDisplayName(),
						"PARKOUR", currentParkour.getName(),
						"CHRONO", formatChrono(chrono),
						"CHECKPOINT", currentPos(currentParkour, lastValidPosition)
				))
		);
	}

	public int stopChrono() {
		if (isChronoRunning()) {
			chronoTask.cancel();
			chronoTask = null;
		}
		int chrono = this.chrono;
		this.chrono = 0;
		return chrono;
	}

	@Override
	public int hashCode() {
		return player.hashCode();
	}

	@Override
	public String toString() {
		return "DarkTraceur{" +
				"player=" + player +
				", currentParkour=" + currentParkour +
				", lastValidPosition=" + lastValidPosition +
				", chrono=" + chrono +
				", chronoTask=" + chronoTask +
				'}';
	}
}
