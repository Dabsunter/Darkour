package fr.dabsunter.darkour.editor;

import fr.dabsunter.darkour.api.parkour.Parkour;
import fr.dabsunter.darkour.api.parkour.PreventedDamages;
import fr.dabsunter.darkour.parkour.DarkCheckpoint;
import fr.dabsunter.darkour.parkour.DarkParkour;
import fr.dabsunter.darkour.parkour.DarkPosition;
import fr.dabsunter.darkour.util.IncompleteParkourException;
import fr.dabsunter.darkour.util.PlayerState;
import fr.dabsunter.darkour.util.TreinMessages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.Dye;

import java.util.ArrayList;
import java.util.List;

import static fr.dabsunter.darkour.DarkourUtils.textify;
import static fr.dabsunter.darkour.util.Trein.format;
import static fr.dabsunter.darkour.util.Trein.multiline;
import static fr.dabsunter.darkour.util.TreinMessages.*;

public class Maker implements Listener {
	private final MakerManager manager;
	private final Player player;
	private final DarkParkour parkour;
	private final PlayerState playerState;

	private boolean waitForName;
	private double minY = 0.0;

	Maker(MakerManager manager, Player player, DarkParkour parkour) {
		this.manager = manager;
		this.player = player;
		this.parkour = parkour;
		playerState = new PlayerState(player);

		playerState.save();
		player.setGameMode(GameMode.CREATIVE);
		PlayerInventory pi = player.getInventory();
		pi.clear();
		pi.setItem(0, textify(new ItemStack(Material.NAME_TAG),
				format(EDITOR_ITEM_RENAME_NAME), multiline(format(EDITOR_ITEM_RENAME_LORE))));
		pi.setItem(1, textify(new Dye(DyeColor.LIME).toItemStack(1),
				format(EDITOR_ITEM_DAMAGES_ALL_NAME), multiline(format(EDITOR_ITEM_DAMAGES_ALL_LORE))));
		pi.setItem(3, textify(new ItemStack(Material.GOLD_PLATE),
				format(EDITOR_ITEM_START_NAME), multiline(format(EDITOR_ITEM_START_LORE))));
		pi.setItem(4, textify(new ItemStack(Material.GOLD_PLATE),
				format(EDITOR_ITEM_END_NAME), multiline(format(EDITOR_ITEM_END_LORE))));
		pi.setItem(5, textify(new ItemStack(Material.IRON_PLATE),
				format(EDITOR_ITEM_CHECKPOINT_NAME), multiline(format(EDITOR_ITEM_CHECKPOINT_LORE))));
		pi.setItem(7, textify(new ItemStack(Material.FEATHER),
				format(EDITOR_ITEM_MINY_NAME), multiline(format(EDITOR_ITEM_MINY_LORE))));
		pi.setItem(8, textify(new ItemStack(Material.BOOK),
				format(EDITOR_ITEM_DONE_NAME), multiline(format(EDITOR_ITEM_DONE_LORE))));

		player.sendMessage(multiline(format(EDITOR_CHAT_WELCOME)));
	}

	public Player getPlayer() {
		return player;
	}

	public DarkParkour getParkour() {
		return parkour;
	}

	void sendActionBar() {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
				TextComponent.fromLegacyText(format(EDITOR_ACTIONBAR,
						"PARKOUR", parkour.getName(),
						"CHECKPOINTS", parkour.hasCheckpoints() ? checkpointList().size() : "0",
						"MINY", minY
				))
		);
	}

	public void done() {
		if (parkour.getName() == null)
			throw new IncompleteParkourException(parkour, Parkour.MissingParameter.NAME);
		if (parkour.getStart() == null)
			throw new IncompleteParkourException(parkour, Parkour.MissingParameter.START);
		if (parkour.getEnd() == null)
			throw new IncompleteParkourException(parkour, Parkour.MissingParameter.END);

		player.sendMessage(format(EDITOR_CHAT_DONE));

		playerState.restore();
		parkour.setOpen(true);
		manager.done(this);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (waitForName && event.getPlayer() == player) {
			parkour.setName(ChatColor.translateAlternateColorCodes('&', event.getMessage()));

			player.sendMessage(format(EDITOR_CHAT_SET_NAME));
			sendActionBar();

			waitForName = false;
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.getPlayer() == player) {
			TreinMessages message;
			switch (player.getInventory().getHeldItemSlot()) {
				case 3: // start
					if (parkour.getStart() != null)
						parkour.getStart().getBlock().setType(Material.AIR);
					parkour.setStart(new DarkPosition(parkour, event.getBlock(), minY));
					message = EDITOR_CHAT_SET_START;
					break;
				case 4: // end
					if (parkour.getEnd() != null)
						parkour.getEnd().getBlock().setType(Material.AIR);
					parkour.setEnd(new DarkPosition(parkour, event.getBlock(), minY));
					message = EDITOR_CHAT_SET_END;
					break;
				case 5: // checkpoint
					checkpointList().add(new DarkCheckpoint(parkour, event.getBlock(), minY));
					message = EDITOR_CHAT_SET_CHECKPOINT;
					sendActionBar();
					break;
				default:
					event.setCancelled(true);
					return;
			}
			player.sendMessage(format(message));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getPlayer() == player) {
			DarkPosition position = parkour.getPositionAt(event.getBlock().getLocation());

			if (position != null) {
				TreinMessages message = null;
				switch (position.getType()) {
					case START:
						parkour.setStart(null);
						message = EDITOR_CHAT_REMOVE_START;
						break;
					case END:
						parkour.setEnd(null);
						message = EDITOR_CHAT_REMOVE_END;
						break;
					case CHECKPOINT:
						checkpointList().remove(position);
						message = EDITOR_CHAT_REMOVE_CHECKPOINT;
						sendActionBar();
				}
				player.sendMessage(format(message));
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer() == player
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			PlayerInventory pi = player.getInventory();
			switch (pi.getHeldItemSlot()) {
				case 0: // name
					waitForName = true;
					player.sendMessage(format(EDITOR_CHAT_NEWNAME));
					break;
				case 1: // switch prevent damage
					switch (parkour.getPreventedDamages()) {
						case ALL:
							parkour.setPreventedDamages(PreventedDamages.FALL);
							pi.setItem(1, textify(new Dye(DyeColor.LIGHT_BLUE).toItemStack(1),
									format(EDITOR_ITEM_DAMAGES_FALL_NAME), multiline(format(EDITOR_ITEM_DAMAGES_FALL_LORE))));
							break;
						case FALL:
							parkour.setPreventedDamages(PreventedDamages.NONE);
							pi.setItem(1, textify(new Dye(DyeColor.GRAY).toItemStack(1),
									format(EDITOR_ITEM_DAMAGES_NONE_NAME), multiline(format(EDITOR_ITEM_DAMAGES_NONE_LORE))));
							break;
						default:
							parkour.setPreventedDamages(PreventedDamages.ALL);
							pi.setItem(1, textify(new Dye(DyeColor.LIME).toItemStack(1),
									format(EDITOR_ITEM_DAMAGES_ALL_NAME), multiline(format(EDITOR_ITEM_DAMAGES_ALL_LORE))));
					}
					break;
				case 7: // change minY
					minY = player.getLocation().getY();
					player.sendMessage(format(EDITOR_CHAT_SET_MINY));
					sendActionBar();
					break;
				case 8: // done
					try {
						done();
					} catch (IncompleteParkourException ex) {
						TreinMessages message = null;
						switch (ex.getMissingParameter()) {
							case NAME:
								message = EDITOR_CHAT_MISSING_NAME;
								break;
							case START:
								message = EDITOR_CHAT_MISSING_START;
								break;
							case END:
								message = EDITOR_CHAT_MISSING_END;
						}
						player.sendMessage(format(message));
					}
					break;
				default:
					return;
			}
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
		if (type != InventoryType.CRAFTING && type != InventoryType.CREATIVE && type != InventoryType.PLAYER)
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
		if (player == this.player)
			event.setCancelled(true);
	}

	private List<DarkCheckpoint> checkpointList() {
		List<DarkCheckpoint> checkpoints = parkour.getInternalCheckpoints();
		if (checkpoints == null)
			parkour.setCheckpoints((checkpoints = new ArrayList<>()));
		return checkpoints;
	}
}
