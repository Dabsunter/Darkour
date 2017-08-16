package fr.dabsunter.darkour;

import fr.dabsunter.darkour.api.parkour.Position;
import fr.dabsunter.darkour.entity.DarkTraceur;
import fr.dabsunter.darkour.parkour.DarkParkour;
import fr.dabsunter.darkour.parkour.DarkPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;

import static fr.dabsunter.darkour.DarkourUtils.nextPos;
import static fr.dabsunter.darkour.DarkourUtils.teleport;

public class EventListener implements Listener {
	private final DarkourPlugin plugin;

	EventListener(DarkourPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		DarkTraceur traceur = plugin.getTraceurManager().get(event.getPlayer());
		DarkParkour parkour;
		if (traceur.isInParkour()) {
			parkour = traceur.getCurrentParkour();
			DarkPosition position = parkour.getPositionAt(event.getTo());
			if (position == null) {
				position = traceur.getLastValidPosition();

				if (!traceur.isChronoRunning())
					plugin.getTraceurManager().startChrono(traceur);

				if (traceur.getPlayer().getLocation().getY() < position.getMinY())
					teleport(traceur, position);
			} else {
				if (!traceur.isChronoRunning() && position.getType() != Position.Type.START)
					plugin.getTraceurManager().startChrono(traceur);

				if (position == nextPos(parkour, traceur.getLastValidPosition()))
					traceur.setLastValidPosition(position);
			}

		} else if (plugin.getMakerManager().get(traceur.getPlayer()) == null
				&& (parkour = plugin.getParkourManager().getByStart(event.getTo())) != null){
			traceur.startParkour(parkour);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			DarkTraceur traceur = plugin.getTraceurManager().get((Player) event.getEntity());
			if (traceur.isInParkour()
					&& traceur.getCurrentParkour().getPreventedDamages().preventDamage(event.getCause()))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		onInventoryEvent(event.getWhoClicked(), event);
	}

	@EventHandler
	public void onInventoryInteract(InventoryDragEvent event) {
			onInventoryEvent(event.getWhoClicked(), event);
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		InventoryType type = event.getInventory().getType();
		if (type != InventoryType.CRAFTING && type != InventoryType.PLAYER)
			onInventoryEvent(event.getPlayer(), event);
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		onInventoryEvent(event.getPlayer(), event);
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		onInventoryEvent(event.getPlayer(), event);
	}

	private void onInventoryEvent(HumanEntity player, Cancellable event) {
		if (player instanceof Player) {
			DarkTraceur traceur = plugin.getTraceurManager().get((Player) player);
			if (traceur.isInParkour())
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			DarkTraceur traceur = plugin.getTraceurManager().get(event.getPlayer());
			if (traceur.isInParkour()) {
				switch (event.getMaterial()) {
					case BLAZE_POWDER:
						traceur.stopChrono();
						traceur.setLastValidPosition(traceur.getCurrentParkour().getStart());
						teleport(traceur, traceur.getCurrentParkour().getStart());
						break;
					case COMPASS:
						traceur.stopParkour(false);
				}

				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		DarkTraceur traceur = plugin.getTraceurManager().get(event.getEntity());
		if (traceur.isInParkour()) {
			event.setKeepInventory(true);
			event.setKeepLevel(true);
			traceur.getPlayer().spigot().respawn();
			teleport(traceur, traceur.getLastValidPosition());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		DarkTraceur traceur = plugin.getTraceurManager().get(event.getPlayer());
		if (traceur.isInParkour())
			traceur.stopParkour(false);
	}
}
